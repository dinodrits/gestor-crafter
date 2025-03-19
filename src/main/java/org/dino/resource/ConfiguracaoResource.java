package org.dino.resource;

import java.math.BigDecimal;
import java.util.List;

import org.dino.model.Cliente;
import org.dino.model.Configuracao;

import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/configuracao")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConfiguracaoResource {

//    @GET
//    public List<Configuracao> list() {
//        return Configuracao.listAll();
//    }
	
	@GET
	@Transactional
	public Configuracao getConfiguracoes() {
		Configuracao c = Configuracao.findById(1);
		if(c == null) {
			c = new Configuracao();
			c.setId(1);
			c.setFatorCarbonoEvitado(0.0F);
			c.setFatorEconomiaArvore(0.0F);
			c.setValorCeb(BigDecimal.ZERO);
			c.persist();
		}
		return c;
	}
	
	@POST
    @Transactional
    public Configuracao create(Configuracao configuracao){
		Configuracao c = Configuracao.findById(1);
		c.setValorCeb(configuracao.getValorCeb());
		c.setFatorCarbonoEvitado(configuracao.getFatorCarbonoEvitado());
		c.setFatorEconomiaArvore(configuracao.getFatorEconomiaArvore());
		c.persist();
        return c;
    }
}
