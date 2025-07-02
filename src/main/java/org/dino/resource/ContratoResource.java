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
import repository.ConsumoRepository;
import repository.ContratoRepository;
import repository.UsinaRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Base64;
import java.util.List;


import org.dino.model.Contrato;
import org.dino.model.UnidadeContrato;
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
	
	
	@Inject
	ContratoRepository contratoRepository;

	@GET
    public List<Contrato> list() {
        return Contrato.listAll();
    }

    @GET
    @Path("/{id}")
    public Contrato get(Long id) {
        return Contrato.findById(id);
    }
    
    @GET
    @Path("/{id}/{mes}/{ano}")
    public Response get(Long id,Integer mes,Integer ano) {
    	
    	try {
    		return Response.status(Response.Status.OK).entity(contratoRepository.getContratoVigente(id,mes,ano)).build();
    		
    	}catch (jakarta.persistence.NoResultException e) {
    		Resposta resposta = new Resposta("Cliente sem contrato vigente para o mês selecionado.", 400);
            return Response.status(Response.Status.BAD_REQUEST).entity(resposta).build();
		}
        
    }

    /**
     * @param requestContrato
     * @return
     * @throws Exception
     */
    @POST
    @Transactional
    public Response create(CadastroContratoRequest requestContrato) throws Exception {
    	System.out.println(objectMapper.writeValueAsString(requestContrato));
    	//System.out.println(contrato.getUsina().getId());
    	
    	Contrato contratoPersistent = null;
    	
    	if(requestContrato.getContrato().getId() == null || requestContrato.getContrato().getId() == 0) {
	    	boolean existeSobreposicao = Contrato.find(
	                "((dtInicio <= ?1 AND dtFim >= ?1) " + // Verifica se a data de início do novo contrato está dentro de um contrato existente
	                "OR (dtInicio <= ?2 AND dtFim >= ?2) " + // Verifica se a data de fim do novo contrato está dentro de um contrato existente
	                "OR (dtInicio >= ?1 AND dtFim <= ?2)) and cliente.id = ?3", // Verifica se o novo contrato engloba completamente um contrato existente
	                requestContrato.getContrato().getDtInicio(), requestContrato.getContrato().getDtFim(),requestContrato.getCliente().getId()
	            ).count() > 0;
	        if(existeSobreposicao) {
	        	Resposta resposta = new Resposta("Já existe contrato na data inserida.", 400);
	            return Response.status(Response.Status.BAD_REQUEST).entity(resposta).build();
	        }
	        if(requestContrato.getContrato().getArquivo() != null) {
	        	try {
	        		requestContrato.getContrato().setArquivo(Base64.getDecoder().decode(requestContrato.getArquivoBase64().split(",")[1]));
	        	}catch (Exception e) {
					// TODO: handle exception
				}
	        }
	        requestContrato.getContrato().setCliente(requestContrato.getCliente());
	        requestContrato.getContrato().setValorMedioKw(requestContrato.getContrato().getTotalContrato().divide(requestContrato.getContrato().getQtdContratada()));
	        requestContrato.getContrato().persist();
	        contratoPersistent = requestContrato.getContrato();
    	}else {
    		Contrato entity = Contrato.findById(requestContrato.getContrato().getId());
    		entity.setDtFim(requestContrato.getContrato().getDtFim());
    		entity.setDtInicio(requestContrato.getContrato().getDtInicio());
    		entity.setPrazo(requestContrato.getContrato().getPrazo());
    		entity.setQtdContratada(requestContrato.getContrato().getQtdContratada());
    		entity.setQtdIsencao(requestContrato.getContrato().getQtdIsencao());
    		entity.setTotalContrato(requestContrato.getContrato().getTotalContrato());
    		entity.setValorMedioKw(entity.getTotalContrato().divide(entity.getQtdContratada(),4,RoundingMode.HALF_DOWN));
    		contratoPersistent = entity;
    		
    	}
    	
    	for(UnidadeContrato unidade : requestContrato.getUnidadesContratos()) {
    		Usina usina = usinasRepository.getUsinasConsumo(unidade.getUsina().getId());
    		
    		unidade.setContrato(contratoPersistent);
    		if(requestContrato.isVerificaDisponibilidade()) {
    			if(unidade.getId() == null) {
					if(usina == null || usina.getDisponivel().subtract(unidade.getPercentual()).compareTo(BigDecimal.ZERO) > 0  ) {
						unidade.persist();
		    		
			    	}else {
			    		Resposta resposta = new Resposta("Não tem kw disponível para essa contratação na usina "+usina.getNome(), 400);
			            return Response.status(Response.Status.BAD_REQUEST).entity(resposta).build();
			    		
			    	}
    			}else {
    				UnidadeContrato entity = UnidadeContrato.findById(unidade.getId());
        			entity.setPercentual(unidade.getPercentual());
        			entity.setUsina(unidade.getUsina());
        			entity.setDiaLeitura(unidade.getDiaLeitura());
        			entity.setDiaVencimento(unidade.getDiaVencimento());
        			entity.persist();
        		}
    		}else {
    			if(unidade.getId() == null) {
    				unidade.persist();
    			}else {
    				UnidadeContrato entity = UnidadeContrato.findById(unidade.getId());
    				entity.setPercentual(unidade.getPercentual());
        			entity.setUsina(unidade.getUsina());
        			entity.persist();
    			}
    		}
    		
    		
    	}
    	
//    	// verificar se ainda vai ser preciso varias usinas por contrato
//        for (UsinaContrato usinaContrato : requestContrato.getUsinas()) {
//        	Usina usina = usinasRepository.getUsinasConsumo(usinaContrato);
//        	usinaContrato.setContrato(requestContrato.getContrato());
//        	System.out.println(objectMapper.writeValueAsString(usinaContrato));
//        	if(requestContrato.isVerificaDisponibilidade()) {
//        		if(usinaContrato.getId() == null) {
//		        	if(usina == null || usina.getDisponivel().subtract(requestContrato.getContrato().getQtdContratada()).compareTo(BigDecimal.ZERO) > 0  ) {
//							usinaContrato.persist();
//			    		
//			    	}else {
//			    		Resposta resposta = new Resposta("Não tem kw disponível para essa contratação na usina "+usina.getNome(), 400);
//			            return Response.status(Response.Status.BAD_REQUEST).entity(resposta).build();
//			    		
//			    	}
//        		}else {
//        			UsinaContrato entity = UsinaContrato.findById(usinaContrato.getId());
//        			entity.setCliente(usinaContrato.getCliente());
//        			entity.setContrato(usinaContrato.getContrato());
//        			entity.setQtdContratada(usinaContrato.getQtdContratada());
//        			entity.persist();
//        		}
//        	}else {
//        		usinaContrato.persist();
//        	}
//        }
    	
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
       
        entity.setDtInicio(person.getDtInicio());
        entity.setCliente(person.getCliente());
        entity.setPrazo(person.getPrazo());
        entity.setQtdContratada(person.getQtdContratada());
        
        
        
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
        entity.getUnidadesContratos().clear();
        entity.delete();
    }

}
