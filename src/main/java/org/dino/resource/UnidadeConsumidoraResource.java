package org.dino.resource;

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

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.dino.model.Cliente;
import org.dino.model.Consumo;
import org.dino.model.Contrato;
import org.dino.model.UnidadeConsumidora;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.panache.common.Parameters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/unidadeConsumidora")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UnidadeConsumidoraResource {
	
	@Inject
    ObjectMapper objectMapper;

	@GET
    public List<Cliente> list() {
        return Cliente.listAll();
    }

	
//	@GET
//    @Path("consumos/{id}")
//    public List<Consumo> getConsumos(Long id) {
//		return  Consumo.find("cliente.id = :id",
//		         Parameters.with("id", id)).list();
//        
//    }
	
	
	
	
	
    
    @GET
    @Path("/{id}")
    public UnidadeConsumidora get(Long id) throws JsonProcessingException {
    	UnidadeConsumidora c = UnidadeConsumidora.findById(id);
    	System.out.println(objectMapper.writeValueAsString(c));
    	return c;
    }

    @POST
    @Transactional
    public UnidadeConsumidora create(UnidadeConsumidora cliente) throws JsonProcessingException {
    	System.out.println(objectMapper.writeValueAsString(cliente));
    	cliente.persistAndFlush();
        return cliente;
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public UnidadeConsumidora update(Long id, UnidadeConsumidora person) {
    	UnidadeConsumidora entity = UnidadeConsumidora.findById(id);
        if(entity == null) {
            throw new NotFoundException();
        }
        
        entity.setDescricao(person.getDescricao());
        entity.setCodigoCliente(person.getCodigoCliente());
        entity.setNumeroUC(person.getNumeroUC());
        entity.persist();
        System.out.println(entity.getId());
        
        return entity;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public void delete(Long id) {
        UnidadeConsumidora entity = UnidadeConsumidora.findById(id);
         //entity.getC
        if(entity == null) {
            throw new NotFoundException();
        }
        entity.delete();
    }

}
