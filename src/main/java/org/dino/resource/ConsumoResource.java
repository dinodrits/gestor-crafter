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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dino.model.Consumo;
import org.dino.model.Contrato;

@Path("/consumo")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConsumoResource {

	@GET
    public List<Consumo> list() {
        return Consumo.listAll();
    }

    @GET
    @Path("/{id}")
    public Consumo get(Long id) {
        return Consumo.findById(id);
    }

    @POST
    @Transactional
    public Consumo create(Consumo consumo) {
    	
    	Map<String, Object> params = new HashMap<>();
    	params.put("ano", consumo.getAno());
    	params.put("mes", consumo.getMes());
    	params.put("cliente.id", consumo.getCliente().getId());
    	params.put("contrato.id", consumo.getContrato().getId());
    	Consumo.find("name = :name and status = :status", params);
    	Consumo.list("ano", consumo.getAno());
    	consumo.persist();
        //return Response.created(URI.create("/contrato/" + contrato.getId())).build();
        return consumo;
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Consumo update(Long id, Consumo person) {
    	Consumo entity = Consumo.findById(id);
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
    	Consumo entity = Consumo.findById(id);
        if(entity == null) {
            throw new NotFoundException();
        }
        entity.delete();
    }

}
