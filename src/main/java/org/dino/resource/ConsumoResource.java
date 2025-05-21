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
import org.dino.model.UnidadeConsumidoraConsumo;
import org.dino.resource.request.CadastroConsumoRequest;
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
    public Response create(CadastroConsumoRequest request) throws JsonProcessingException {
    	System.out.println(objectMapper.writeValueAsString(request));
    	Map<String, Object> params = new HashMap<>();
    	params.put("mes", request.getConsumo().getMes());
    	params.put("ano", request.getConsumo().getAno());
    	params.put("id", request.getConsumo().getCliente().getId());
    	Long count = Consumo.count("mes = :mes and ano = :ano and cliente.id= :id ", params);
    	
    	if(count > 0) {
    		Resposta resposta = new Resposta("Já existe consumo registrado para o período selecionado", 400);
            return Response.status(Response.Status.BAD_REQUEST).entity(resposta).build();
    	}
    	
    	BigDecimal descontoValor =  request.getConsumo().getValorUnitarioCeb().setScale(4, RoundingMode.HALF_DOWN).multiply(request.getConsumo().getDesconto().divide(BigDecimal.valueOf(100).setScale(4, RoundingMode.HALF_DOWN)));
    	
    	System.out.println(descontoValor);
    	System.out.println(request.getConsumo().getValorUnitarioCeb().subtract(descontoValor).setScale(4, RoundingMode.HALF_DOWN));
    	request.getConsumo().setValorKw(request.getConsumo().getValorUnitarioCeb().subtract(descontoValor).setScale(4, RoundingMode.HALF_DOWN));
    	
    	request.getConsumo().setValorTotal((request.getConsumo().getValorKw().setScale(4, RoundingMode.HALF_DOWN)).multiply(request.getConsumo().getInjetado()));
    	request.getConsumo().persist();
    	
    	List<UnidadeConsumidoraConsumo> unidades = request.getUnidadesConsumos();
    	
    	for (UnidadeConsumidoraConsumo unidadeConsumidoraConsumo : unidades) {
    		unidadeConsumidoraConsumo.setConsumo(request.getConsumo());
    		
    		unidadeConsumidoraConsumo.persist();
		}
    	  	
    	return Response.status(Response.Status.OK).entity(request.getConsumo()).build(); 
        //return Response.created(URI.create("/contrato/" + contrato.getId())).build();
        
    }
    
    
   

    @PUT
    @Path("/{id}")
    @Transactional
    public Consumo update(Long id, CadastroConsumoRequest person) throws JsonProcessingException {
    	System.out.println(objectMapper.writeValueAsString(person));
    	Consumo entity = Consumo.findById(id);
    	System.out.println(objectMapper.writeValueAsString(entity));
        if(entity == null) {
            throw new NotFoundException();
        }
        
      
    	//entity.setAcumuladoMes(person.getConsumo().getCompensado().subtract(entity.getConsumido()));
        entity.setAno(person.getConsumo().getAno());
        entity.setCliente(person.getConsumo().getCliente());
        entity.setCompensado(person.getConsumo().getCompensado());
        entity.setConsumido(person.getConsumo().getConsumido());
        entity.setContrato(person.getConsumo().getContrato());
      
        entity.setDesconto(person.getConsumo().getDesconto());
        entity.setInjetado(person.getConsumo().getInjetado());
        entity.setMes(person.getConsumo().getMes());
        entity.setValorKw(person.getConsumo().getValorKw().setScale(4, RoundingMode.HALF_DOWN));
        entity.setValorUnitarioCeb(person.getConsumo().getValorUnitarioCeb().setScale(4, RoundingMode.HALF_DOWN));
        entity.setVencimento(person.getConsumo().getVencimento());
        
        
        BigDecimal descontoValor =  entity.getValorUnitarioCeb().setScale(4, RoundingMode.HALF_DOWN).multiply(entity.getDesconto().divide(BigDecimal.valueOf(100).setScale(4, RoundingMode.HALF_DOWN)));
    	
    
        entity.setValorKw(entity.getValorUnitarioCeb().subtract(descontoValor).setScale(4, RoundingMode.HALF_DOWN));
    	
        entity.setValorTotal((entity.getValorKw().setScale(4, RoundingMode.HALF_DOWN)).multiply(entity.getInjetado()));
        
        
        List<UnidadeConsumidoraConsumo> unidades = person.getUnidadesConsumos();
    	
    	for (UnidadeConsumidoraConsumo unidadeConsumidoraConsumo : unidades) {
    		
    		unidadeConsumidoraConsumo.setConsumo(person.getConsumo());
    		if(unidadeConsumidoraConsumo.getId() == null) {
    			unidadeConsumidoraConsumo.persist();
    		}else {
    			
    			UnidadeConsumidoraConsumo consumoEntity = UnidadeConsumidoraConsumo.findById(unidadeConsumidoraConsumo.getId());
    			consumoEntity.setCompensado(unidadeConsumidoraConsumo.getCompensado());
    			consumoEntity.setConsumido(unidadeConsumidoraConsumo.getConsumido());
    			consumoEntity.setFatura(unidadeConsumidoraConsumo.getFatura());
    			consumoEntity.setInjetado(unidadeConsumidoraConsumo.getInjetado());
    			consumoEntity.setSaldo(unidadeConsumidoraConsumo.getSaldo());
    			consumoEntity.setSaldoAnterior(unidadeConsumidoraConsumo.getSaldoAnterior());
    			consumoEntity.setPercentual(unidadeConsumidoraConsumo.getPercentual());
    			consumoEntity.persist();
    		}
    		
    		
		}
      
    	
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
        entity.delete();
    }
    
    
    @DELETE
    @Path("consumoUnidade/{id}")
    @Transactional
    public void deleteConsumoUnidade(Long id) {
    	UnidadeConsumidoraConsumo entity = UnidadeConsumidoraConsumo.findById(id);
        if(entity == null) {
            throw new NotFoundException();
        }
       
        entity.delete();
    }
    
    @GET
    @Path("/consumos/{id}/{ano}")
    public List<Consumo> getRelatorioCliente(Long id,int ano) {
    	
    	Map<String, Object> params = new HashMap<>();
    	
    	params.put("ano", ano);
    	params.put("id", id);
    	
    	return consumoRepository.completarConsumosAno( Consumo.find("cliente.id = :id and ano=:ano order by ano, mes",params).list(),ano);
    	
        
    }
    
    @POST
    @Transactional
    @Path("/importConsumo")
    public void importConsumo(Consumo consumo) throws JsonProcessingException {
    	
    	
    }
    
    
    

}
