package org.dino.resource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dino.model.Geracao;
import org.dino.resource.request.ChartDataResponse;
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
    	params.put("id", geracao.getUsina().getId());
    	Long count = Geracao.count("mes = :mes and ano = :ano and usina.id=:id", params);
    	
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
    public Geracao update(Long id, Geracao person) throws JsonProcessingException {
    	System.out.println(objectMapper.writeValueAsString(person));
    	Geracao entity = Geracao.findById(id);
        if(entity == null) {
            throw new NotFoundException();
        }
        
        entity.setAno(person.getAno());
        entity.setMes(person.getMes());
        entity.setQtdGerada(person.getQtdGerada());
        entity.setUsina(person.getUsina());
        
        
        
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
    
    
    
    @GET
    @Path("/getGraficoGeracoes/{mes}/{ano}")
    public ChartDataResponse getGeracoesDashboard(Integer mes,Integer ano) {
    	
    	
    	Map<String, Object> params = new HashMap<>();
    	params.put("mes", mes);
    	params.put("ano", ano);
        List<Geracao> geracoes =   Geracao.find(" ano = :ano and mes = :mes",params).list();
        
        List<String> labels = new ArrayList<>();
	    List<BigDecimal> qtdGerada = new ArrayList<>();
        
        for (Geracao geracao : geracoes) {
        	labels.add(geracao.getUsina().getNome());
        	qtdGerada.add(geracao.getQtdGerada());
		}
        
        return new ChartDataResponse(labels, qtdGerada);
    }

}
