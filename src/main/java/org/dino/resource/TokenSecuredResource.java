package org.dino.resource;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.util.Arrays;
import java.util.HashSet;

import org.dino.model.Contrato;
import org.dino.model.Usuario;
import org.dino.resource.request.LoginRequest;
import org.dino.resource.request.Resposta;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;

import io.quarkus.panache.common.Parameters;
import io.smallrye.jwt.build.Jwt;

@Path("/secured")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TokenSecuredResource {

	 	@Inject
	    JsonWebToken jwt; 

	    @GET
	    @Path("permit-all")
	    @PermitAll
	    @Produces(MediaType.TEXT_PLAIN)
	    public String hello(@Context SecurityContext ctx) {
	        return getResponseString(ctx);
	    }
	    
	    @POST
	    @Path("login")
	    @PermitAll
	    public Response login(LoginRequest request) {
	    	
	    	System.out.println(request.getUsername());
	    	
	    	if(request.getUsername().equals("admin") && request.getPassword().equals("123456")) {
	    		
	    		String token =
	    				Jwt.issuer("https://dinodrits.duckdns.org") 
	    				.upn("diegobessa@gmail.com") 
	    				.groups(new HashSet<>(Arrays.asList("User", "Admin"))) 
	    				.expiresAt(3600)
	    				.claim(Claims.birthdate.name(), "2001-07-13") 
	    				.sign();
	    		return Response.ok().entity("{\"message\": \"Login successful\", \"token\": \""+token+ "\", \"expireIn\": 3600 }").build();
	    		
	    	}
	    	
	    	Usuario u = Usuario.find("usuario = :usuario",
			         Parameters.with("usuario", request.getUsername())).firstResult();
	    	if(u != null && u.getSenha().equals(request.getPassword())  ) {
	    		String token =
	    				Jwt.issuer("https://dinodrits.duckdns.org") 
	    				.upn("diegobessa@gmail.com") 
	    				.groups(new HashSet<>(Arrays.asList("User", "Admin"))) 
	    				.expiresAt(3600)
	    				.claim(Claims.birthdate.name(), "2001-07-13") 
	    				.sign();
	    		return Response.ok().entity("{\"message\": \"Login successful\", \"token\": \""+token+ "\", \"expireIn\": 3600 }").build();
	    	}
	    	
	    	Resposta resposta = new Resposta("Usuário ou Senha Inválido", 400);
	    	
	    	return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(resposta)
                    .build();
	    	        
	    }

	    @GET
	    @Path("roles-allowed") 
	    @RolesAllowed({ "User", "Admin" }) 
	    @Produces(MediaType.TEXT_PLAIN)
	    public String helloRolesAllowed(@Context SecurityContext ctx) {
	        return getResponseString(ctx) + ", birthdate: " + jwt.getClaim("birthdate").toString(); 
	    }

	    private String getResponseString(SecurityContext ctx) {
	        String name;
	        if (ctx.getUserPrincipal() == null) {
	            name = "anonymous";
	        } else if (!ctx.getUserPrincipal().getName().equals(jwt.getName())) {
	            throw new InternalServerErrorException("Principal and JsonWebToken names do not match");
	        } else {
	            name = ctx.getUserPrincipal().getName();
	        }
	        return String.format("hello %s,"
	            + " isHttps: %s,"
	            + " authScheme: %s,"
	            + " hasJWT: %s",
	            name, ctx.isSecure(), ctx.getAuthenticationScheme(), hasJwt());
	    }

	    private boolean hasJwt() {
	        return jwt.getClaimNames() != null;
	    }
}