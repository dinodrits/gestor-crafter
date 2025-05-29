package org.dino.resource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dino.model.Consumo;
import org.dino.model.Contrato;
import org.dino.model.Geracao;
import org.dino.model.MonitoramentoKw;
import org.dino.model.UnidadeConsumidoraConsumo;
import org.dino.model.UnidadeContrato;
import org.dino.model.Usina;
import org.dino.resource.request.ChartDataResponse;
import org.dino.resource.request.Resposta;

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
import jakarta.ws.rs.core.Response;
import repository.ConsumoRepository;
import repository.MonitoramentoRepository;
import repository.UsinaRepository;

@Path("/monitoramento")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MonitoramentoResource {

	@Inject
	MonitoramentoRepository monitoramentoRepository;
	
	
	@GET
    public List<MonitoramentoKw> list() {
        return MonitoramentoKw.listAll();
    }

//	@GET
//    @Path("contratos/{id}")
//    public List<Contrato> getContratos(Long id) {
//		return  Contrato.find("idCliente.id = :id",
//		         Parameters.with("id", id)).list();
//        
//    }
	

	
	
	
    @GET
    @Path("/{id}")
    public MonitoramentoKw get(Long id) {
        return Usina.findById(id);
    }

    @POST
    @Transactional
    public MonitoramentoKw create(MonitoramentoKw monitoramento) {
    	monitoramento.persist();
        return monitoramento;
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public MonitoramentoKw update(Long id, MonitoramentoKw person) {
    	MonitoramentoKw entity = MonitoramentoKw.findById(id);
        if(entity == null) {
            throw new NotFoundException();
        }
        
//        entity.setNome(person.getNome());
//        entity.setCidade(person.getCidade());
//        entity.setCapacidadeProducao(person.getCapacidadeProducao());
//        entity.setDataCriacao(person.getDataCriacao());
//        entity.setPotencia(person.getPotencia());
//        entity.setContato(person.getContato());
//        entity.setCpfCnpj(person.getCpfCnpj());
//        entity.setEmail(person.getEmail());
//        entity.setEndereco(person.getEndereco());
//        entity.setUf(person.getUf());
        
        entity.persist();
        System.out.println(entity.getId());
        
        return entity;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(Long id) {
        MonitoramentoKw entity = MonitoramentoKw.findById(id);
        
        entity.delete();
        return Response.status(Response.Status.OK).build(); 
    }
    
    @GET
	@Path("utltimosMonitoramentos")
	public ChartDataResponse getUltimasProducoes() {
		return  monitoramentoRepository.getUltimosMonitoramentos();
		
	}

}
