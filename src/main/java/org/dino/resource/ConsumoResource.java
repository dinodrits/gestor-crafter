package org.dino.resource;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dino.model.Cliente;
import org.dino.model.Configuracao;
import org.dino.model.Consumo;
import org.dino.model.Contrato;
import org.dino.resource.request.Resposta;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.panache.common.Parameters;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import repository.ConsumoRepository;
import repository.ContratoRepository;


@Path("/consumo")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConsumoResource {
	
	@Inject
	ContratoRepository contratoRepository;
	
	@Inject
	ConsumoRepository consumoRepository;
	
	@Inject
    ObjectMapper objectMapper;

	@GET
    public List<Consumo> list() {
        return Consumo.listAll();
    }

    @GET
    @Path("/{id}")
    public Consumo get(Long id) {
        return Consumo.find("cliente.id = :id",
		         Parameters.with("id", id)).singleResult();
    }
    
    @GET
    @Path("/media/{id}")
    public BigDecimal getMedia(Long id) {
        return consumoRepository.getMediaConsumo(id);
    }
    
    @GET
    @Path("/consumoAtual/{id}")
    public Consumo getConsumoAtual(Long id) {
    	try {
        return Consumo.find("cliente.id = :id and mes = month(now()) and ano = year(now())",
		         Parameters.with("id", id)).singleResult();
    	}catch (Exception e) {
			return null;
		}
    }
    
    @GET
    @Path("/valorMedio/{id}")
    public BigDecimal getValorMedia(Long id) {
        return consumoRepository.getValorMedioConsumokw(id);
    }
    
    @GET
    @Path("/fatura/{mes}/{ano}/{token}")
    public List<Consumo> getFatura(int mes,int ano,String token) {
    	List<Consumo> c = consumoRepository.getFatura(mes,ano,token);
    	
        return c;
    }
    
    
    @POST
    @Transactional
    public Response create(Consumo consumo) throws JsonProcessingException {
    	System.out.println(objectMapper.writeValueAsString(consumo));
    	Map<String, Object> params = new HashMap<>();
    	params.put("mes", consumo.getMes());
    	params.put("ano", consumo.getAno());
    	params.put("id", consumo.getCliente().getId());
    	params.put("idusina", consumo.getUsina().getId());
    	Long count = Consumo.count("mes = :mes and ano = :ano and cliente.id= :id and usina.id = :idusina", params);
    	
    	if(count > 0) {
    		Resposta resposta = new Resposta("Já existe consumo registrado para o período selecionado", 400);
            return Response.status(Response.Status.BAD_REQUEST).entity(resposta).build();
    	}
    	Configuracao  config = Configuracao.findById(1);
    	consumo.setValorUnitarioCeb(config.getValorCeb());
    	
    	consumo.setValorTotal((consumo.getInjetado().multiply(consumo.getValorKw())).subtract(consumo.getDesconto()));
    	consumo.persist();
    	
    	Cliente cliente = Cliente.findById(consumo.getCliente().getId());
    	cliente.setSaldoAcumulado((cliente.getSaldoAcumulado() != null ? cliente.getSaldoAcumulado() : 0)   + ( consumo.getCompensado().intValue() - consumo.getConsumido().intValue() ));
    	cliente.persist();
    	
    	return Response.status(Response.Status.OK).entity(consumo).build(); 
        //return Response.created(URI.create("/contrato/" + contrato.getId())).build();
        
    }
    
    
   

    @PUT
    @Path("/{id}")
    @Transactional
    public Consumo update(Long id, Consumo person) {
    	Consumo entity = Consumo.findById(id);
        if(entity == null) {
            throw new NotFoundException();
        }
        
        //atualiza saldo do cliente
        Cliente cliente = Cliente.findById(person.getCliente().getId());
    	cliente.setSaldoAcumulado((cliente.getSaldoAcumulado() != null ? cliente.getSaldoAcumulado() : 0)   - ( entity.getCompensado().intValue() - entity.getConsumido().intValue() ));
    	cliente.setSaldoAcumulado((cliente.getSaldoAcumulado() != null ? cliente.getSaldoAcumulado() : 0)   + ( person.getCompensado().intValue() - person.getConsumido().intValue() ));
    	cliente.persist();
    	
    	Configuracao  config = Configuracao.findById(1);
        
        entity.setAcumuladoMes(person.getCompensado().subtract(entity.getConsumido()));
        entity.setAno(person.getAno());
        entity.setCliente(person.getCliente());
        entity.setCompensado(person.getCompensado());
        entity.setConsumido(person.getConsumido());
        entity.setContrato(person.getContrato());
      
        entity.setDesconto(person.getDesconto());
        entity.setInjetado(person.getInjetado());
        entity.setMes(person.getMes());
        entity.setValorKw(person.getValorKw());
        entity.setValorTotal((person.getInjetado().multiply(person.getValorKw())).subtract(person.getDesconto()));
        entity.setValorUnitarioCeb(config.getValorCeb());
        entity.setVencimento(person.getVencimento());
        
        
        
        return entity;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public void delete(Long id) {
    	Consumo entity = Consumo.findById(id);
        if(entity == null) {
            throw new NotFoundException();
        }
        Cliente cliente = Cliente.findById(entity.getCliente().getId());
        cliente.setSaldoAcumulado((cliente.getSaldoAcumulado() != null ? cliente.getSaldoAcumulado() : 0)   - ( entity.getCompensado().intValue() - entity.getConsumido().intValue() ));
        entity.delete();
    }

}
