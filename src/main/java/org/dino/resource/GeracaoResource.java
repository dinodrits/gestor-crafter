package org.dino.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dino.model.Geracao;
import org.dino.resource.request.Resposta;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
import repository.ContratoRepository;


@Path("/geracao")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GeracaoResource {
	
	@Inject
	ContratoRepository contratoRepository;
	
	@Inject
    ObjectMapper objectMapper;

	@GET
    public List<Geracao> list() {
        return Geracao.listAll();
    }

    @GET
    @Path("/{id}")
    public Geracao get(Long id) {
        return Geracao.findById(id);
    }
    
    

    @POST
    @Transactional
    public Response create(Geracao geracao) throws JsonProcessingException {
    	Map<String, Object> params = new HashMap<>();
    	params.put("mes", geracao.getMes());
    	params.put("ano", geracao.getAno());
    	Long count = Geracao.count("mes = :mes and ano = :ano", params);
    	
    	if(count > 0) {
    		Resposta resposta = new Resposta("Já existe geracao registrado para o período selecionado", 400);
            return Response.status(Response.Status.BAD_REQUEST).entity(resposta).build();
    	}
    	geracao.persist();
    	return Response.status(Response.Status.OK).entity(geracao).build(); 
        //return Response.created(URI.create("/contrato/" + contrato.getId())).build();
        
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Geracao update(Long id, Geracao person) {
    	Geracao entity = Geracao.findById(id);
        if(entity == null) {
            throw new NotFoundException();
        }
        
//        entity.setDtFim(person.getDtFim());
//        entity.setDiaVencimento(person.getDiaVencimento());
//        entity.setDtInicio(person.getDtInicio());
//        entity.setIdCliente(person.getIdCliente());
//        entity.setPrazo(person.getPrazo());
//        entity.setQtdContratada(person.getQtdContratada());
//        entity.setValorAluguel(entity.getValorAluguel());
        
        
        return entity;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public void delete(Long id) {
    	Geracao entity = Geracao.findById(id);
        if(entity == null) {
            throw new NotFoundException();
        }
        entity.delete();
    }

}
