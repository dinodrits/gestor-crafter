package org.dino.resource;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.List;

import org.dino.model.TokenItau;
import org.dino.model.Usuario;
import org.dino.resource.request.CobrancaRequest;
import org.dino.resource.request.CobrancaResponse;

@Path("/itauApi")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ItauResource {

	@GET
	@Path("criarpix/{id}")
    public CobrancaResponse criaPix(Long id) {
		
		MultivaluedMap<String, String> formData = new MultivaluedHashMap<>();
        formData.add("client_id", "68c0c080-8f61-39a3-9ead-afcdf31b99e3");
        formData.add("client_secret", "d0652a82-0e46-41f2-8479-436a8f417e22");
        formData.add("grant_type", "client_credentials");
		
        Response response =    ClientBuilder.newClient()
                .target("https://sandbox.devportal.itau.com.br/api/oauth/jwt")
                .request()
                .post(Entity.form(formData));
        TokenItau token = response.readEntity(TokenItau.class);
        
        CobrancaRequest request = new CobrancaRequest();
        
        request.calendario = new CobrancaRequest.Calendario();
        request.calendario.dataDeVencimento = "2025-05-31";
        
        request.devedor = new CobrancaRequest.Devedor();
        request.devedor.cpf = "12345678909";
        request.devedor.nome = "Francisco da Silva";
        
        request.valor = new CobrancaRequest.Valor();
        request.valor.original = "22.50";
        
        request.chave = "5f84a4c5-c5cb-4599-9f13-7eb4d419dacc";
        
        Response response2 = ClientBuilder.newClient()
        .target("https://sandbox.devportal.itau.com.br/itau-ep9-gtw-pix-recebimentos-ext-v2/v2/cobv/944edefa654a4fecbcdc8f4638d3b110" + id)
        .request(MediaType.APPLICATION_JSON)
        .header("Authorization", token.getAccess_token())
        .put(Entity.entity(request, MediaType.APPLICATION_JSON));
        
        CobrancaResponse cobranca = response2.readEntity(CobrancaResponse.class);
        return cobranca;
	      
    }

	
	
    

}
