package org.dino.resource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dino.model.Cliente;
import org.dino.model.Configuracao;
import org.dino.model.Consumo;
import org.dino.model.Contrato;
import org.dino.model.MonitoramentoKw;
import org.dino.model.UnidadeConsumidoraConsumo;
import org.dino.resource.request.CadastroConsumoRequest;
import org.dino.resource.request.ChartDataResponse;
import org.dino.resource.request.ConsumoHistoricoValorResponse;
import org.dino.resource.request.ConsumoRelatorioResponse;
import org.dino.resource.request.Resposta;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
import repository.ContratoRepository;


@Path("/consumo")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConsumoResource {
	
	@Inject
	ContratoRepository contratoRepository;
	
	@Inject
	ConsumoRepository consumoRepository;
	
	@Inject
    ObjectMapper objectMapper;

	@GET
    public List<Consumo> list() {
        return Consumo.listAll();
    }

    @GET
    @Path("/{id}")
    public Consumo get(Long id) {
        return Consumo.find("cliente.id = :id",
		         Parameters.with("id", id)).singleResult();
    }
    
    @GET
    @Path("/media/{id}")
    public BigDecimal getMedia(Long id) {
        return consumoRepository.getMediaConsumo(id);
    }
    
    @GET
    @Path("/consumoAtual/{id}")
    public Consumo getConsumoAtual(Long id) {
    	
        Consumo c = Consumo.find("Select c from Consumo c where cliente.id = :id order by c.ano desc, c.mes desc limit 1",
		         Parameters.with("id", id)).singleResult();
        Map<String, Object> params = new HashMap<>();
    	params.put("mes",c.getMes());
    	params.put("ano", c.getAno());
    	
        MonitoramentoKw kw =  MonitoramentoKw.find(" ano = :ano and mes = :mes ", params).singleResult();
        c.setValorUnitarioCeb(kw.getTarifaBandeira());
        return c;
    	
    	
    }
    
    @GET
	@Path("ultimosConsumos")
	public ChartDataResponse getUltimosConsumos() {
		return  consumoRepository.getUltimasProducoes();
		
	}
    
    @GET
    @Path("/valorMedio/{id}")
    public BigDecimal getValorMedia(Long id) {
        return consumoRepository.getValorMedioConsumokw(id);
    }
    
    @GET
    @Path("/fatura/{mes}/{ano}/{token}")
    public List<ConsumoRelatorioResponse> getFatura(int mes,int ano,String token) {
    	List<ConsumoRelatorioResponse> c = consumoRepository.getFatura(mes,ano,token);
    	
        return c;
    }
    
    
    @POST
    @Transactional
    public Response create(CadastroConsumoRequest request) throws JsonProcessingException {
    	System.out.println(objectMapper.writeValueAsString(request));
    	Map<String, Object> params = new HashMap<>();
    	params.put("mes", request.getConsumo().getMes());
    	params.put("ano", request.getConsumo().getAno());
    	params.put("id", request.getConsumo().getCliente().getId());
    	Long count = Consumo.count("mes = :mes and ano = :ano and cliente.id= :id ", params);
    	
    	int mesAnterior  = request.getConsumo().getMes() - 1;
		 int anoAnterior =  request.getConsumo().getAno() - 0;
		 if(mesAnterior < 1) {
			 mesAnterior = 12;
			 anoAnterior = anoAnterior -1;
		 }
		 
    	
    	if(count > 0) {
    		Resposta resposta = new Resposta("Já existe consumo registrado para o período selecionado", 400);
            return Response.status(Response.Status.BAD_REQUEST).entity(resposta).build();
    	}
    	
    	BigDecimal descontoValor =  request.getConsumo().getValorUnitarioCeb().setScale(4, RoundingMode.HALF_DOWN).multiply(request.getConsumo().getDesconto().divide(BigDecimal.valueOf(100).setScale(4, RoundingMode.HALF_DOWN)));
    	
    	//buscar consumo anterior 
		Map<String, Object> paramsAnterior = new HashMap<>();
		paramsAnterior.put("mes", mesAnterior);
		paramsAnterior.put("ano", anoAnterior);
		paramsAnterior.put("id", request.getConsumo().getCliente().getId());
		Consumo consumoAnterior = new Consumo();
		try {
			 consumoAnterior = Consumo.find("mes = :mes and ano = :ano and cliente.id= :id ", paramsAnterior).singleResult();
			 consumoAnterior.setSaldoAnterior(new BigDecimal(consumoAnterior.getSaldoAnterior().intValue()));
		}catch (Exception e) {
			// TODO: handle exception
			consumoAnterior.setSaldoDevedor(BigDecimal.ZERO);
		}
		
		Contrato contrato = Contrato.findById(request.getConsumo().getContrato().getId()); 
    	
    	//int valorMaximo = consumoRepository.calculaFaturaMaxima(request.getConsumo().getContrato());
		BigDecimal valorMaximo = BigDecimal.ZERO;
		
		if(request.getConsumo().getValorTotalContratado() != null && request.getConsumo().getValorTotalContratado().compareTo(BigDecimal.ZERO) > 0 ) {
			valorMaximo = request.getConsumo().getValorTotalContratado().multiply(new BigDecimal(1.3));
			
		}
		
		if(contrato.getValorMedioKw() != null && contrato.getValorMedioKw().compareTo(BigDecimal.ZERO) > 0 ) {
			request.getConsumo().setValorMedioContratado(contrato.getValorMedioKw());
		}
		
		
//		if(contrato.getTotalContrato() != null ) {
//			valorMaximo = contrato.getTotalContrato().multiply(new BigDecimal(1.3));
//		}
		
		
    	
    	request.getConsumo().setValorKw(request.getConsumo().getValorUnitarioCeb().subtract(descontoValor).setScale(4, RoundingMode.HALF_DOWN));
    	if(contrato.getModalidadeFaturamento().equals("PV")) {
	    	BigDecimal totalAFaturar = request.getConsumo().getInjetado().multiply(request.getConsumo().getValorKw()).add(consumoAnterior.getSaldoDevedor());
	    	
	    	if(totalAFaturar.compareTo(valorMaximo) > 0  && valorMaximo.compareTo(BigDecimal.ZERO) > 0) {
	    		request.getConsumo().setSaldoDevedor(totalAFaturar.subtract(valorMaximo));
	    		request.getConsumo().setValorTotal(valorMaximo);
	    	}else {
	    		
	    		request.getConsumo().setValorTotal((request.getConsumo().getValorKw().setScale(4, RoundingMode.HALF_DOWN)).multiply(request.getConsumo().getInjetado()).add(consumoAnterior.getSaldoDevedor()) );
	    	}
    	}else {
    		request.getConsumo().setValorTotal(contrato.getTotalContrato());
    	}
    	
    	request.getConsumo().persist();
    	
    	List<UnidadeConsumidoraConsumo> unidades = request.getUnidadesConsumos();
    	
    	for (UnidadeConsumidoraConsumo unidadeConsumidoraConsumo : unidades) {
    		unidadeConsumidoraConsumo.setConsumo(request.getConsumo());
    		
    		unidadeConsumidoraConsumo.persist();
		}
    	  	
    	return Response.status(Response.Status.OK).entity(request.getConsumo()).build(); 
        //return Response.created(URI.create("/contrato/" + contrato.getId())).build();
        
    }
    
    
   

    @PUT
    @Path("/{id}")
    @Transactional
    public Consumo update(Long id, CadastroConsumoRequest person) throws JsonProcessingException {
    	System.out.println(objectMapper.writeValueAsString(person));
    	Consumo entity = Consumo.findById(id);
    	System.out.println(objectMapper.writeValueAsString(entity));
        if(entity == null) {
            throw new NotFoundException();
        }
        
      
    	//entity.setAcumuladoMes(person.getConsumo().getCompensado().subtract(entity.getConsumido()));
        entity.setAno(person.getConsumo().getAno());
        entity.setCliente(person.getConsumo().getCliente());
        entity.setCompensado(person.getConsumo().getCompensado());
        entity.setConsumido(person.getConsumo().getConsumido());
       // entity.setContrato(person.getConsumo().getContrato());
      
        entity.setDesconto(person.getConsumo().getDesconto());
        entity.setInjetado(person.getConsumo().getInjetado());
        entity.setMes(person.getConsumo().getMes());
        entity.setValorKw(person.getConsumo().getValorKw());
        entity.setValorUnitarioCeb(person.getConsumo().getValorUnitarioCeb().setScale(4, RoundingMode.HALF_DOWN));
        entity.setVencimento(person.getConsumo().getVencimento());
        entity.setValorTotalContratado(person.getConsumo().getValorTotalContratado());
        entity.setValorMedioContratado(person.getConsumo().getValorMedioContratado());
        
        BigDecimal descontoValor =  entity.getValorUnitarioCeb().setScale(4, RoundingMode.HALF_DOWN).multiply(entity.getDesconto().divide(BigDecimal.valueOf(100).setScale(4, RoundingMode.HALF_DOWN)));
    	
    
       // entity.setValorKw(entity.getValorUnitarioCeb().subtract(descontoValor).setScale(4, RoundingMode.HALF_DOWN));
    	
        entity.setValorTotal((entity.getValorKw().setScale(4, RoundingMode.HALF_DOWN)).multiply(entity.getInjetado()));
        
        int mesAnterior  = person.getConsumo().getMes() - 1;
		 int anoAnterior =  person.getConsumo().getAno() - 0;
		 if(mesAnterior < 1) {
			 mesAnterior = 12;
			 anoAnterior = anoAnterior -1;
		 }
      //buscar consumo anterior 
      		Map<String, Object> paramsAnterior = new HashMap<>();
      		paramsAnterior.put("mes", mesAnterior);
      		paramsAnterior.put("ano", anoAnterior);
      		paramsAnterior.put("id", person.getConsumo().getCliente().getId());
      		Consumo consumoAnterior = new Consumo();
      		try {
      			 consumoAnterior = Consumo.find("mes = :mes and ano = :ano and cliente.id= :id ", paramsAnterior).singleResult();
      			 consumoAnterior.setSaldoAnterior(new BigDecimal(consumoAnterior.getSaldoAnterior().intValue()));
      		}catch (Exception e) {
      			// TODO: handle exception
      			consumoAnterior.setSaldoDevedor(BigDecimal.ZERO);
      		}
        
      		
      	System.out.println(objectMapper.writeValueAsString(entity));	
        BigDecimal valorMaximo = entity.getValorTotalContratado().multiply(new BigDecimal(1.3));
        BigDecimal totalAFaturar = person.getConsumo().getInjetado().multiply(person.getConsumo().getValorKw()).add(consumoAnterior.getSaldoDevedor());
        
        System.out.println("valor maximo contrato: " + valorMaximo.toString());
        System.out.println("valor a faturar : " + totalAFaturar.toString());
        System.out.println("um mesno o outro : " + totalAFaturar.subtract(valorMaximo));
        
        if(totalAFaturar.compareTo(valorMaximo) > 0  && valorMaximo.compareTo(BigDecimal.ZERO) > 0) {
        	entity.setSaldoDevedor(totalAFaturar.subtract(valorMaximo));
        	
    	}else {
    		
    		person.getConsumo().setValorTotal((person.getConsumo().getValorKw().setScale(4, RoundingMode.HALF_DOWN)).multiply(person.getConsumo().getInjetado()).add(consumoAnterior.getSaldoDevedor()) );
    	}
        
//        Contrato contrato = Contrato.findById(entity.getContrato().getId());
//        
//        if(contrato.getValorMedioKw() != null && contrato.getValorMedioKw().compareTo(BigDecimal.ZERO) > 0 ) {
//			entity.setValorMedioContratado(contrato.getValorMedioKw());
//		}
        
        List<UnidadeConsumidoraConsumo> unidades = person.getUnidadesConsumos();
    	
    	for (UnidadeConsumidoraConsumo unidadeConsumidoraConsumo : unidades) {
    		
    		unidadeConsumidoraConsumo.setConsumo(person.getConsumo());
    		if(unidadeConsumidoraConsumo.getId() == null) {
    			unidadeConsumidoraConsumo.persist();
    		}else {
    			
    			UnidadeConsumidoraConsumo consumoEntity = UnidadeConsumidoraConsumo.findById(unidadeConsumidoraConsumo.getId());
    			consumoEntity.setCompensado(unidadeConsumidoraConsumo.getCompensado());
    			consumoEntity.setConsumido(unidadeConsumidoraConsumo.getConsumido());
    			consumoEntity.setFatura(unidadeConsumidoraConsumo.getFatura());
    			consumoEntity.setInjetado(unidadeConsumidoraConsumo.getInjetado());
    			consumoEntity.setSaldo(unidadeConsumidoraConsumo.getSaldo());
    			consumoEntity.setSaldoAnterior(unidadeConsumidoraConsumo.getSaldoAnterior());
    			consumoEntity.setPercentual(unidadeConsumidoraConsumo.getPercentual());
    			consumoEntity.persist();
    		}
    		
    		
		}
      
    	
    	entity.persist();
    
        
        //entity.setValorTotal((person.getInjetado().multiply(person.getValorKw())).subtract(person.getDesconto()));
        
        
        
        return entity;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public void delete(Long id) {
    	Consumo entity = Consumo.findById(id);
        if(entity == null) {
            throw new NotFoundException();
        }
        entity.delete();
    }
    
    
    @DELETE
    @Path("consumoUnidade/{id}")
    @Transactional
    public void deleteConsumoUnidade(Long id) {
    	UnidadeConsumidoraConsumo entity = UnidadeConsumidoraConsumo.findById(id);
        if(entity == null) {
            throw new NotFoundException();
        }
       
        entity.delete();
    }
    
    @GET
    @Path("/consumos/{id}/{ano}/{idUnidade}/{mes}/{qtdMes}")
    public List<ConsumoRelatorioResponse> getRelatorioCliente(Long id,int ano,Long idUnidade,int mes,int qtdMes) {
    	
    	Map<String, Object> params = new HashMap<>();
    	
    	params.put("ano", ano);
    	params.put("id", id);
    	params.put("idUnidade", idUnidade);
    	params.put("mes", mes);
    	params.put("qtd", qtdMes );
    	
    	return consumoRepository.completarConsumosAno( UnidadeConsumidoraConsumo.find("SELECT ucc FROM UnidadeConsumidoraConsumo ucc JOIN FETCH ucc.consumo c WHERE ucc.cliente.id = :id AND (c.ano * 12 + c.mes) BETWEEN (:ano * 12 + :mes) AND (:ano * 12 + :mes + :qtd - 1) and ucc.unidadeConsumidora.id = :idUnidade ORDER BY c.ano, c.mes",params).list(),ano,qtdMes);
    	
    }
    
    
    @GET
    @Path("/consumosPorAno/{id}/{ano}/{idUnidade}")
    public List<ConsumoRelatorioResponse> getRelatorioClientePorAno(Long id,int ano,Long idUnidade) {
    	
    	Map<String, Object> params = new HashMap<>();
    	
    	
    	params.put("id", id);
    	params.put("idUnidade", idUnidade);

    	
    	return consumoRepository.completarConsumosAno( UnidadeConsumidoraConsumo.find("SELECT ucc FROM UnidadeConsumidoraConsumo ucc JOIN FETCH ucc.consumo c WHERE ucc.cliente.id = :id and ucc.unidadeConsumidora.id = :idUnidade ORDER BY (c.ano * 12 + c.mes)",params).page((ano-1)*12, 12).list(),ano,12);
    	
    }
    
    @POST
    @Transactional
    @Path("/importConsumo")
    public void importConsumo(Consumo consumo) throws JsonProcessingException {
    	
    	
    }
    
    
    @GET
    @Path("/maioresConsumidores/{mes}/{ano}")
    public List<Consumo> getMaioresConsumidores(int mes,int ano) {
        //return consumoRepository.getValorMedioConsumokw(id);
    	
Map<String, Object> params = new HashMap<>();
    	
    	params.put("ano", ano);
    	params.put("mes", mes);
    	
    	
    	return Consumo.find(" ano = :ano AND mes = :mes ORDER BY compensado DESC LIMIT 5",params).list();
    	
    	
    }
    
    
    @GET
    @Path("/totalConsumo/{mes}/{ano}")
    public Integer getTotalConsumo(int mes,int ano) {
        //return consumoRepository.getValorMedioConsumokw(id);
    	
    	
    	
    	return consumoRepository.getTotalConsumoMes(mes, ano);
    	
    	
    }
    
    @GET
    @Path("/getHistoricoContrato/{id}")
    public List<ConsumoHistoricoValorResponse> getHistoricoValorContrato(Long id) {
    	//return consumoRepository.getValorMedioConsumokw(id);
    	
    	
    	
    	return consumoRepository.getHistoricoCliente(id);
    	
    	
    }
    
    
    

}
