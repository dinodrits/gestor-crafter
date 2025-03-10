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
import org.dino.model.UsinaContrato;
import org.dino.resource.request.CadastroContratoRequest;
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
    public Response create(CadastroContratoRequest requestContrato) throws Exception {
    	System.out.println(objectMapper.writeValueAsString(requestContrato));
    	//System.out.println(contrato.getUsina().getId());
    	
    	if(requestContrato.getContrato().getId() == null || requestContrato.getContrato().getId() == 0) {
	    	boolean existeSobreposicao = Contrato.find(
	                "(dtInicio <= ?1 AND dtFim >= ?1) " + // Verifica se a data de início do novo contrato está dentro de um contrato existente
	                "OR (dtInicio <= ?2 AND dtFim >= ?2) " + // Verifica se a data de fim do novo contrato está dentro de um contrato existente
	                "OR (dtInicio >= ?1 AND dtFim <= ?2)", // Verifica se o novo contrato engloba completamente um contrato existente
	                requestContrato.getContrato().getDtInicio(), requestContrato.getContrato().getDtFim()
	            ).count() > 0;
	        if(existeSobreposicao) {
	        	Resposta resposta = new Resposta("Já existe contrato na data inserida.", 400);
	            return Response.status(Response.Status.BAD_REQUEST).entity(resposta).build();
	        }
	        requestContrato.getContrato().persist();
    	}else {
    		Contrato entity = Contrato.findById(requestContrato.getContrato().getId());
    		entity.setDiaLeitura(requestContrato.getContrato().getDiaLeitura());
    		entity.setDiaVencimento(requestContrato.getContrato().getDiaVencimento());
    		entity.setDtFim(requestContrato.getContrato().getDtFim());
    		entity.setDtInicio(requestContrato.getContrato().getDtInicio());
    		entity.setPrazo(requestContrato.getContrato().getPrazo());
    		entity.setQtdContratada(requestContrato.getContrato().getQtdContratada());
    		entity.setQtdIsencao(requestContrato.getContrato().getQtdIsencao());
    		entity.setValorAluguel(requestContrato.getContrato().getValorAluguel());
    	}
        for (UsinaContrato usinaContrato : requestContrato.getUsinas()) {
        	Usina usina = usinasRepository.getUsinasConsumo(usinaContrato);
        	usinaContrato.setContrato(requestContrato.getContrato());
        	System.out.println(objectMapper.writeValueAsString(usinaContrato));
        	if(requestContrato.isVerificaDisponibilidade()) {
        		if(usinaContrato.getId() == null) {
		        	if(usina == null || usina.getDisponivel().subtract(new BigDecimal(requestContrato.getContrato().getQtdContratada())).compareTo(BigDecimal.ZERO) > 0  ) {
							usinaContrato.persist();
			    		
			    	}else {
			    		Resposta resposta = new Resposta("Não tem kw disponível para essa contratação na usina "+usina.getNome(), 400);
			            return Response.status(Response.Status.BAD_REQUEST).entity(resposta).build();
			    		
			    	}
        		}else {
        			UsinaContrato entity = UsinaContrato.findById(usinaContrato.getId());
        			entity.setCliente(usinaContrato.getCliente());
        			entity.setContrato(usinaContrato.getContrato());
        			entity.setQtdContratada(usinaContrato.getQtdContratada());
        			entity.persist();
        		}
        	}else {
        		usinaContrato.persist();
        	}
        }
    	
        //return Response.created(URI.create("/contrato/" + contrato.getId())).build();
        return Response.status(Response.Status.OK).entity(requestContrato).build();
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
