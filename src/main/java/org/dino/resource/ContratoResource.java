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
import repository.UsinaRepository;

import java.math.BigDecimal;
import java.util.List;


import org.dino.model.Contrato;
import org.dino.model.Usina;

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
    public Contrato create(Contrato contrato) throws Exception {
    	System.out.println(objectMapper.writeValueAsString(contrato));
    	Usina usina = usinasRepository.getUsinasConsumo(contrato);
    	if(usina.getUtilizado().subtract(new BigDecimal(contrato.getQtdContratada())).compareTo(BigDecimal.ZERO) > 0  ) {
    		contrato.persist();
    		
    	}else {
    		throw new Exception("Não tem kw disponível para essa contratação.");
    	}
    	
        //return Response.created(URI.create("/contrato/" + contrato.getId())).build();
        return contrato;
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
