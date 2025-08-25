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
import java.math.BigInteger;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.dino.model.Cliente;
import org.dino.model.Consumo;
import org.dino.model.Contrato;
import org.dino.model.UnidadeConsumidora;
import org.dino.model.UnidadeContrato;
import org.dino.resource.request.UnidadeContratoPercentagemResponse;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.panache.common.Parameters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/cliente")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClienteResource {
	
	@Inject
    ObjectMapper objectMapper;
	
	@Inject
	ContratoRepository contratoRepository;
	
	@Inject
	ConsumoRepository consumoRepository;

	@Inject
	UsinaRepository usinaRepository;

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
    @Path("consumos/{id}")
    public List<Consumo> getConsumos(Long id) {
		return  Consumo.find("cliente.id = :id",
		         Parameters.with("id", id)).list();
        
    }
	
	@GET
	@Path("unidadesConsumidoras/{id}")
	public List<UnidadeContrato> getUnidadesConsumidoras(Long id) {
		
		
		Contrato c = Contrato.find("cliente.id = :id  ORDER BY dtInicio DESC", Parameters.with("id", id))
	    .firstResult();
		
		return c.getUnidadesContratos();
		
//		return  UnidadeConsumidora.find("cliente.id = :id",
//				Parameters.with("id", id)).list();
		
	}
	
	@GET
	@Path("unidadesConsumidorasPercentual/{id}/{ano}/{mes}")
	public List<UnidadeContratoPercentagemResponse> getUnidadesConsumidorasPercentual(Long id,int ano, int mes) {
		
		List<UnidadeContratoPercentagemResponse> retorno = new ArrayList<UnidadeContratoPercentagemResponse>();
		Contrato c =  contratoRepository.getContratoVigente(id,mes,ano);
		
		Map<String, Object> params2 = new HashMap<>();
    	params2.put("idContrato", c.getId());
    	params2.put("id", id);
    	
    	
		
    	 List<UnidadeContrato> unidades = UnidadeContrato.find("cliente.id = :id and contrato.id = :idContrato",
				params2).list();
    	 
    	 for (UnidadeContrato unidadeContrato : unidades) {
    		 UnidadeContratoPercentagemResponse adicionar = new UnidadeContratoPercentagemResponse();
    		 int mesAnterior  = mes - 1;
    		 int anoAnterior = ano - 0;
    		 if(mesAnterior < 1) {
    			 mesAnterior = 12;
    			 anoAnterior = anoAnterior -1;
    		 }
    		 int saldo = consumoRepository.getSaldoUnidadeMes(id, unidadeContrato.getUnidadeConsumidora().getId(),mesAnterior, anoAnterior);
    		 int gerado = usinaRepository.getQtdGerado(unidadeContrato.getUsina().getId(), mesAnterior, anoAnterior);
    		 BigDecimal saldoDevedorAnterior = consumoRepository.getSaldoDevedorMes(id, unidadeContrato.getUnidadeConsumidora().getId(),mesAnterior, anoAnterior);
    		 adicionar.setSaldoDevedorAnterior(saldoDevedorAnterior);
    		 adicionar.setUnidadeContrato(unidadeContrato);
    		 adicionar.setInjetado(unidadeContrato.getPercentual().divide(new BigDecimal(100)).multiply(new BigDecimal(gerado)).intValue());
    		 adicionar.setSaldoAnterior(saldo);
    		 adicionar.setPercentual(unidadeContrato.getPercentual());
    		 retorno.add(adicionar);
		}
    	 
    	 return retorno;
		
	}
	
	
	
	@GET
	@Path("gerarToken/{id}")
	@Transactional
	public String gerarToken(Long id) throws JsonProcessingException {
		Cliente cliente = Cliente.findById(id);
		cliente.setToken(UUID.randomUUID().toString().replaceAll("-", ""));
		cliente.persist();
		System.out.println(cliente.getToken());
		System.out.println(objectMapper.writeValueAsString(cliente));
		
		return cliente.getToken();
	}
	
	
	
    @GET
    @Path("getClienteToken/{token}")
    public Cliente getCliente(String token) throws JsonProcessingException {
    	Cliente c = Cliente.find("token = :token",
		         Parameters.with("token", token)).firstResult();
    	System.out.println(objectMapper.writeValueAsString(c));
    	return c;
    }
    
    
    @GET
    @Path("filtrarModalidade/{modalidade}")
    public List<Cliente> filtrarModalidade(String modalidade) throws JsonProcessingException {
    	
    	if(modalidade.equals("TODOS")) {
    		return Cliente.listAll();
    	}else {
    		return Cliente.find("SELECT DISTINCT c FROM Cliente c JOIN c.contratos cont WHERE cont.modalidadeFaturamento = ?1", modalidade).list();
    	}
    	
    	
    }
    
    @GET
    @Path("/{id}")
    public Cliente get(Long id) throws JsonProcessingException {
    	Cliente c = Cliente.findById(id);
    	System.out.println(objectMapper.writeValueAsString(c));
    	return c;
    }

    @POST
    @Transactional
    public Response create(Cliente cliente) throws JsonProcessingException {
    	System.out.println(objectMapper.writeValueAsString(cliente));
    	if (Cliente.find("cpfCnpj", cliente.getCpfCnpj()).count() > 0) {
            return Response.status(Response.Status.CONFLICT)
                   .entity("CPF/CNPJ já cadastrado").build();
        }
    	cliente.setToken(UUID.randomUUID().toString().replaceAll("-", ""));
    	cliente.persistAndFlush();
        return Response
                .status(Response.Status.CREATED)
                .entity(cliente)
                .build();
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
   //     entity.setCodigoCliente(person.getCodigoCliente());
        entity.setContato(person.getContato());
        entity.setCpfCnpj(person.getCpfCnpj());
        entity.setEmail(person.getEmail());
        entity.setEndereco(person.getEndereco());
     //   entity.setNumeroUC(person.getNumeroUC());
        entity.setUf(person.getUf());
        entity.setToken(person.getToken());
        entity.persist();
        System.out.println(entity.getId());
        
        return entity;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public void delete(Long id) {
        Cliente entity = Cliente.findById(id);
        entity.getContratos().clear();
        
        Consumo.delete("cliente.id = :id",
		         Parameters.with("id", id));
         //entity.getC
        if(entity == null) {
            throw new NotFoundException();
        }
        entity.delete();
    }

}
