package org.dino.resource;

import java.util.ArrayList;
import java.util.List;

import org.dino.model.Consumo;
import org.dino.model.Contrato;
import org.dino.model.Geracao;
import org.dino.model.Usina;

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
import repository.UsinaRepository;

@Path("/usina")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsinaResource {

	@Inject
	UsinaRepository usinasRepository;
	
	@GET
    public List<Usina> list() {
        return Usina.listAll();
    }

//	@GET
//    @Path("contratos/{id}")
//    public List<Contrato> getContratos(Long id) {
//		return  Contrato.find("idCliente.id = :id",
//		         Parameters.with("id", id)).list();
//        
//    }
	
	@GET
	@Path("/disponibilidade")
	public List<Usina> getUsinasUtilizacao(){
		
		
		return usinasRepository.getUsinasConsumo();
		
	}
	
	@GET
    @Path("geracoes/{id}")
    public List<Geracao> getGeracoes(Long id) {
		return  Geracao.find("usina.id = :id",
		         Parameters.with("id", id)).list();
        
    }
	
    @GET
    @Path("/{id}")
    public Usina get(Long id) {
        return Usina.findById(id);
    }

    @POST
    @Transactional
    public Usina create(Usina usina) {
    	System.out.println(usina);
    	usina.persist();
        return usina;
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Usina update(Long id, Usina person) {
    	Usina entity = Usina.findById(id);
        if(entity == null) {
            throw new NotFoundException();
        }
        
        entity.setNome(person.getNome());
        entity.setCidade(person.getCidade());
        entity.setCapacidadeProducao(person.getCapacidadeProducao());
        entity.setDataCriacao(person.getDataCriacao());
        entity.setPotencia(person.getPotencia());
        entity.setContato(person.getContato());
        entity.setCpfCnpj(person.getCpfCnpj());
        entity.setEmail(person.getEmail());
        entity.setEndereco(person.getEndereco());
        entity.setUf(person.getUf());
        
        entity.persist();
        System.out.println(entity.getId());
        
        return entity;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public void delete(Long id) {
        Usina entity = Usina.findById(id);
        if(entity == null) {
            throw new NotFoundException();
        }
        entity.delete();
    }

}
