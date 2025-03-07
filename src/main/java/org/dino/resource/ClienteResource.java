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
import java.util.List;

import org.dino.model.Cliente;
import org.dino.model.Contrato;

import io.quarkus.panache.common.Parameters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/cliente")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClienteResource {
	
	@Inject
    ObjectMapper objectMapper;

	@GET
    public List<Cliente> list() {
        return Cliente.listAll();
    }

	@GET
    @Path("contratos/{id}")
    public List<Contrato> getContratos(Long id) {
		return  Contrato.find("cliente.id = :id",
		         Parameters.with("id", id)).list();
        
    }
	
    @GET
    @Path("/{id}")
    public Cliente get(Long id) {
        return Cliente.findById(id);
    }

    @POST
    @Transactional
    public Cliente create(Cliente cliente) throws JsonProcessingException {
    	System.out.println(objectMapper.writeValueAsString(cliente));
    	cliente.persist();
        return cliente;
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Cliente update(Long id, Cliente person) {
    	Cliente entity = Cliente.findById(id);
        if(entity == null) {
            throw new NotFoundException();
        }
        
        entity.setNome(person.getNome());
        entity.setClassificacao(person.getClassificacao());
        entity.setCidade(person.getCidade());
        entity.setCodigoCliente(person.getCodigoCliente());
        entity.setContato(person.getContato());
        entity.setCpfCnpj(person.getCpfCnpj());
        entity.setEmail(person.getEmail());
        entity.setEndereco(person.getEndereco());
        entity.setNumeroUC(person.getNumeroUC());
        entity.setUf(person.getUf());
        
        entity.persist();
        System.out.println(entity.getId());
        
        return entity;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public void delete(Long id) {
        Cliente entity = Cliente.findById(id);
        if(entity == null) {
            throw new NotFoundException();
        }
        entity.delete();
    }

}
