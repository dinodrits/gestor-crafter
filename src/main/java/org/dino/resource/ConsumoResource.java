package org.dino.resource;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    	
    	BigDecimal descontoValor =  consumo.getValorUnitarioCeb().setScale(4, RoundingMode.HALF_DOWN).multiply(consumo.getDesconto().divide(BigDecimal.valueOf(100).setScale(4, RoundingMode.HALF_DOWN)));
    	
    	System.out.println(descontoValor);
    	System.out.println(consumo.getValorUnitarioCeb().subtract(descontoValor).setScale(4, RoundingMode.HALF_DOWN));
    	consumo.setValorKw(consumo.getValorUnitarioCeb().subtract(descontoValor).setScale(4, RoundingMode.HALF_DOWN));
    	
    	consumo.setValorTotal((consumo.getValorKw().setScale(4, RoundingMode.HALF_DOWN)).multiply(consumo.getInjetado()));
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
    public Consumo update(Long id, Consumo person) throws JsonProcessingException {
    	System.out.println(objectMapper.writeValueAsString(person));
    	Consumo entity = Consumo.findById(id);
    	System.out.println(objectMapper.writeValueAsString(entity));
        if(entity == null) {
            throw new NotFoundException();
        }
        
        //atualiza saldo do cliente
        Cliente cliente = Cliente.findById(person.getCliente().getId());
    	cliente.setSaldoAcumulado((cliente.getSaldoAcumulado() != null ? cliente.getSaldoAcumulado() : 0)   - ( entity.getCompensado().intValue() - entity.getConsumido().intValue() ));
    	cliente.setSaldoAcumulado((cliente.getSaldoAcumulado() != null ? cliente.getSaldoAcumulado() : 0)   + ( person.getCompensado().intValue() - person.getConsumido().intValue() ));
    	cliente.persist();
    	
    	entity.setAcumuladoMes(person.getCompensado().subtract(entity.getConsumido()));
        entity.setAno(person.getAno());
        entity.setCliente(person.getCliente());
        entity.setCompensado(person.getCompensado());
        entity.setConsumido(person.getConsumido());
        entity.setContrato(person.getContrato());
      
        entity.setDesconto(person.getDesconto());
        entity.setInjetado(person.getInjetado());
        entity.setMes(person.getMes());
        entity.setValorKw(person.getValorKw().setScale(4, RoundingMode.HALF_DOWN));
        entity.setValorUnitarioCeb(person.getValorUnitarioCeb().setScale(4, RoundingMode.HALF_DOWN));
        entity.setVencimento(person.getVencimento());
        BigDecimal descontoValor =  entity.getValorUnitarioCeb().setScale(4, RoundingMode.HALF_DOWN).multiply(entity.getDesconto().divide(BigDecimal.valueOf(100).setScale(4, RoundingMode.HALF_DOWN)));
    	
    
        entity.setValorKw(entity.getValorUnitarioCeb().subtract(descontoValor).setScale(4, RoundingMode.HALF_DOWN));
    	
        entity.setValorTotal((entity.getValorKw().setScale(4, RoundingMode.HALF_DOWN)).multiply(entity.getInjetado()));
        
      
    	
    	entity.persist();
    
        
        //entity.setValorTotal((person.getInjetado().multiply(person.getValorKw())).subtract(person.getDesconto()));
        
        
        
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
    
    
    @GET
    @Path("/consumos/{id}/{ano}")
    public List<Consumo> getRelatorioCliente(Long id,int ano) {
    	
    	Map<String, Object> params = new HashMap<>();
    	
    	params.put("ano", ano);
    	params.put("id", id);
    	
    	return consumoRepository.completarConsumosAno( Consumo.find("cliente.id = :id and ano=:ano",params).list(),ano);
    	
        
    }

}
