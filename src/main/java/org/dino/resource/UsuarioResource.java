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
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.List;

import org.dino.model.Usuario;

@Path("/Usuario")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioResource {

	@GET
    public List<Usuario> list() {
        return Usuario.listAll();
    }

	
	
    @GET
    @Path("/{id}")
    public Usuario get(Long id) {
        return Usuario.findById(id);
    }

    @POST
    @Transactional
    public Response create(Usuario Usuario) {
    	Usuario.persist();
        return Response.created(URI.create("/Usuario/" + Usuario.getId())).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Usuario update(Long id, Usuario person) {
    	Usuario entity = Usuario.findById(id);
        if(entity == null) {
            throw new NotFoundException();
        }
        
        entity.setUsuario(person.getUsuario());
        entity.setAtivo(person.isAtivo());
        entity.setEmail(person.getEmail());
        entity.setIdCliente(person.getIdCliente());
        entity.setSenha(person.getSenha());
        entity.setTipoAcesso(person.getTipoAcesso());
        
        
        return entity;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public void delete(Long id) {
        Usuario entity = Usuario.findById(id);
        if(entity == null) {
            throw new NotFoundException();
        }
        entity.delete();
    }

}
