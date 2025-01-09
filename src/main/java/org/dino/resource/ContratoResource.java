package org.dino.resource;

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



import java.util.List;


import org.dino.model.Contrato;

@Path("/contrato")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ContratoResource {

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
    public Contrato create(Contrato contrato) {
    	contrato.persist();
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
        entity.setIdCliente(person.getIdCliente());
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
