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
import repository.UsinaRepository;

import java.math.BigDecimal;
import java.util.List;


import org.dino.model.Contrato;
import org.dino.model.Usina;
import org.dino.resource.request.Resposta;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/contrato")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ContratoResource {
	
	@Inject
    ObjectMapper objectMapper;
	
	@Inject
	UsinaRepository usinasRepository;

	@GET
    public List<Contrato> list() {
        return Contrato.listAll();
    }

    @GET
    @Path("/{id}")
    public Contrato get(Long id) {
        return Contrato.findById(id);
    }

    @POST
    @Transactional
    public Response create(Contrato contrato) throws Exception {
    	System.out.println(objectMapper.writeValueAsString(contrato));
    	System.out.println(contrato.getUsina().getId());
    	Usina usina = usinasRepository.getUsinasConsumo(contrato);
    	System.out.println(objectMapper.writeValueAsString(usina));
    	boolean existeSobreposicao = Contrato.find(
                "(dtInicio <= ?1 AND dtFim >= ?1) " + // Verifica se a data de início do novo contrato está dentro de um contrato existente
                "OR (dtInicio <= ?2 AND dtFim >= ?2) " + // Verifica se a data de fim do novo contrato está dentro de um contrato existente
                "OR (dtInicio >= ?1 AND dtFim <= ?2)", // Verifica se o novo contrato engloba completamente um contrato existente
                contrato.getDtInicio(), contrato.getDtFim()
            ).count() > 0;
        if(existeSobreposicao) {
        	Resposta resposta = new Resposta("Já existe contrato na data inserida.", 400);
            return Response.status(Response.Status.BAD_REQUEST).entity(resposta).build();
        }
    	if(usina == null || usina.getUtilizado().subtract(new BigDecimal(contrato.getQtdContratada())).compareTo(BigDecimal.ZERO) > 0  ) {
    		contrato.persist();
    		
    	}else {
    		Resposta resposta = new Resposta("Não tem kw disponível para essa contratação.", 400);
            return Response.status(Response.Status.BAD_REQUEST).entity(resposta).build();
    		
    	}
    	
        //return Response.created(URI.create("/contrato/" + contrato.getId())).build();
        return Response.status(Response.Status.OK).entity(contrato).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Contrato update(Long id, Contrato person) {
    	Contrato entity = Contrato.findById(id);
        if(entity == null) {
            throw new NotFoundException();
        }
        
        entity.setDtFim(person.getDtFim());
        entity.setDiaVencimento(person.getDiaVencimento());
        entity.setDtInicio(person.getDtInicio());
        entity.setCliente(person.getCliente());
        entity.setPrazo(person.getPrazo());
        entity.setQtdContratada(person.getQtdContratada());
        entity.setValorAluguel(entity.getValorAluguel());
        
        
        return entity;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public void delete(Long id) {
        Contrato entity = Contrato.findById(id);
        if(entity == null) {
            throw new NotFoundException();
        }
        entity.delete();
    }

}
