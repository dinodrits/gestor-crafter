package org.dino.resource;

import java.util.List;

import org.dino.model.Configuracao;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/configuracao")
public class ConfiguracaoResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<Configuracao> list() {
        return Configuracao.listAll();
    }
}
