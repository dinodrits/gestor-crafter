package repository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.dino.model.Consumo;
import org.dino.model.Geracao;
import org.dino.model.MonitoramentoKw;
import org.dino.model.UnidadeConsumidoraConsumo;
import org.dino.model.UnidadeContrato;
import org.dino.model.Usina;
import org.dino.resource.request.ConsumoRelatorioResponse;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ConsumoRepository implements PanacheRepository<Usina>{

	public BigDecimal getMediaConsumo(Long id) {
		Object result;
		result =  getEntityManager().createNativeQuery("SELECT AVG(valorTotal) FROM Consumos WHERE idCliente = :idCliente").setParameter("idCliente", id).getSingleResult();
		return (BigDecimal)result;
	}
	
	public BigDecimal getValorMedioConsumokw(Long id) {
		Object result;
		result =  getEntityManager().createNativeQuery("SELECT AVG(valorkw) FROM Consumos WHERE idCliente = :idCliente").setParameter("idCliente", id).getSingleResult();
		return (BigDecimal)result;
	}

	public List<ConsumoRelatorioResponse> getFatura(int mes, int ano, String token) {
		// TODO Auto-generated method stub
		List<ConsumoRelatorioResponse> response = new ArrayList<ConsumoRelatorioResponse>();
		List<UnidadeConsumidoraConsumo> c = getEntityManager().createNativeQuery("SELECT ucc.* FROM Consumos co LEFT JOIN Clientes c ON co.idCliente = c.idCliente inner JOIN  UnidadesConsumidorasConsumo ucc ON co.idConsumo = ucc.idConsumo WHERE c.token = :token AND co.mes = :mes AND co.ano = :ano ", UnidadeConsumidoraConsumo.class).setParameter("token", token).setParameter("mes", mes).setParameter("ano", ano).getResultList();
		
		
		//buscar valor do kw/h da ceb do mes
		Map<String, Object> params = new HashMap<>();
    	
    	params.put("ano", ano);
    	params.put("mes", mes);
		MonitoramentoKw mk = MonitoramentoKw.find("ano = :ano and mes = :mes",params).singleResult();
		
		for (Iterator iterator = c.iterator(); iterator.hasNext();) {
			UnidadeConsumidoraConsumo u = (UnidadeConsumidoraConsumo) iterator.next();
			Map<String, Object> paramsGeracao = new HashMap<>();
	    	
			paramsGeracao.put("ano", ano);
			paramsGeracao.put("mes", mes);
			paramsGeracao.put("idUsina", u.getUsina().getId());
			
			Geracao g = Geracao.find("ano = :ano and mes = :mes and usina.id = :idUsina", paramsGeracao).singleResult();
			
			
			
			ConsumoRelatorioResponse add = new ConsumoRelatorioResponse();
			add.setAno(u.getConsumo().getAno());
			add.setCompensado(u.getCompensado().intValue());
			add.setConsumido(u.getConsumido().intValue());
			add.setConsumo(u.getConsumo());
			add.setFatura(u.getFatura().intValue());
			add.setInjetado(u.getInjetado().intValue());
			add.setMes(u.getConsumo().getMes());
			add.setPercentual(u.getPercentual());
			add.setSaldo(u.getSaldo());
			add.setUnidadeConsumidora(u.getUnidadeConsumidora());
			add.setUsina(u.getUsina());
			add.setValorKwCeb(mk.getTarifaBandeira());
			add.setTotalContratado(u.getPercentual().multiply( u.getUsina().getCapacidadeProducao().divide(BigDecimal.valueOf(12),4, RoundingMode.HALF_EVEN) ).intValue() / 100);
			if(u.getConsumo().getContrato().getModalidadeFaturamento().equals("PF")) {
				add.setValorContratado(u.getConsumo().getContrato().getTotalContrato());
				add.setValorTotal(u.getConsumo().getContrato().getTotalContrato());
        		
        		if(add.getCompensado() > 0) {
        			add.setValorKw(add.getValorContratado().divide(new BigDecimal( add.getCompensado()),4, RoundingMode.HALF_EVEN));
        		}else {
        			add.setValorKw(BigDecimal.ZERO);
        		}
        	}else {
        		add.setValorKw(mk.getTarifaBandeira().subtract( mk.getTarifaBandeira().multiply(u.getConsumo().getDesconto())));
	        	add.setValorTotal(add.getValorKw().multiply( BigDecimal.valueOf( add.getCompensado()) ));
        		add.setValorContratado(add.getValorTotal());
        	}
			
			add.setValorContratado(null);
			
			response.add(add);
			
		}
		
		return response;
	}

//	public List<ItemRelatorioCliente> getRelatorioCliente(Long id) {
//		// TODO Auto-generated method stub
//		List<Consumo> consumos =  Consumo.find("cliente.id = :id",Parameters.with("id", id)).list();
//		List<ItemRelatorioCliente> items = new ArrayList<ItemRelatorioCliente>();
//		
//		for(Consumo c : consumos) {
//			ItemRelatorioCliente item1 = new ItemRelatorioCliente();
//			item1.setDescricao(c.);
//		}
//		
//		return null;
//	}
//	
	
	public int getSaldoUnidadeMes(Long id,Integer idUnidade, int mes, int ano) {
		
		Map<String, Object> params2 = new HashMap<>();
    	params2.put("id", id);
    	params2.put("mes", mes);
    	params2.put("ano", ano);
		
    	try {
    		Consumo c = Consumo.find(" cliente.id = :id and mes = :mes and ano = :ano ", params2).singleResult();
    		Map<String, Object> param = new HashMap<>();
    		param.put("id", c.getId());
    		param.put("idUnidade", idUnidade);
    		UnidadeConsumidoraConsumo unidadeConsumo =    UnidadeConsumidoraConsumo.find(" consumo.id = :id and unidadeConsumidora.id = :idUnidade", param).singleResult();
    		return unidadeConsumo.getSaldo();
    	}catch (Exception e) {
			return 0;
		}
    	
		
		
	}
	
	
	public List<ConsumoRelatorioResponse> completarConsumosAno(List<UnidadeConsumidoraConsumo> consumosExistentes, int ano) {
	    List<ConsumoRelatorioResponse> consumosCompletos = new ArrayList<>();
	    
	    // Criar um mapa para facilitar a busca por mês
	    Map<Integer, UnidadeConsumidoraConsumo> consumoPorMes = consumosExistentes.stream()
	        .collect(Collectors.toMap(uc -> uc.getConsumo().getMes(), Function.identity()));
	    
    	
	    
	   
	    
	    
	    
	    // Gerar todos os meses do ano (1 a 12)
	    for (int mes = 1; mes <= 12; mes++) {
	    	Map<String, Object> params = new HashMap<>();
	    	
	    	params.put("ano", ano);
	    	params.put("mes", mes);
	    	
	    	 MonitoramentoKw mk = MonitoramentoKw.find("ano = :ano and mes = :mes",params).singleResult();
	    	 
	    	 
	        if (consumoPorMes.containsKey(mes)) {
	            // Se existe consumo para este mês, adiciona o existente
	        	UnidadeConsumidoraConsumo ucc = consumoPorMes.get(mes);
	        	ConsumoRelatorioResponse cr = new ConsumoRelatorioResponse();
	        	cr.setConsumo(ucc.getConsumo());
	        	
	        	cr.setUsina(ucc.getUsina());
	        	cr.setUnidadeConsumidora(ucc.getUnidadeConsumidora());
	        	cr.setAno(ucc.getConsumo().getAno());
	        	cr.setMes(ucc.getConsumo().getMes());
	        	cr.setCompensado(ucc.getCompensado().intValue());
	        	cr.setConsumido(ucc.getConsumido().intValue());
	        	cr.setFatura(ucc.getFatura().intValue());
	        	cr.setInjetado(ucc.getInjetado().intValue());
	        	cr.setPercentual(ucc.getPercentual());
	        	cr.setSaldo(ucc.getSaldo());
	        	cr.setValorKwCeb(mk.getTarifaBandeira());
	        	
	        	cr.setValorKw(mk.getTarifaBandeira().subtract( mk.getTarifaBandeira().multiply(ucc.getConsumo().getDesconto())));
	        	cr.setValorTotal(cr.getValorKw().multiply( BigDecimal.valueOf( cr.getCompensado()) ));
	        	if(ucc.getConsumo().getContrato().getModalidadeFaturamento().equals("PF")) {
	        		cr.setValorContratado(ucc.getConsumo().getContrato().getTotalContrato());
	        		System.out.println(cr.getCompensado());
	        		if(cr.getCompensado() > 0) {
	        			cr.setValorKw(cr.getValorContratado().divide(new BigDecimal( cr.getCompensado()),4, RoundingMode.HALF_EVEN));
	        		}else {
	        			cr.setValorKw(BigDecimal.ZERO);
	        		}
	        	}else {
	        		cr.setValorContratado(cr.getValorTotal());
	        	}
	        	
	        	
	            consumosCompletos.add(cr);
	        } else {
	            // Se não existe, cria um novo consumo zerado
	        	ConsumoRelatorioResponse consumoZerado = new ConsumoRelatorioResponse();
	        	consumoZerado.setConsumo(new Consumo());
//	            consumoZerado.getConsumo().setMes(mes);
//	            consumoZerado.getConsumo().setAno(ano);
//	            consumoZerado.getConsumo().setValorKw(BigDecimal.ZERO);
//	            consumoZerado.getConsumo().setDesconto(BigDecimal.ZERO);
//	            consumoZerado.getConsumo().setArvoresPlantadas(BigDecimal.ZERO);
//	            consumoZerado.getConsumo().setCarbonoEvitado(BigDecimal.ZERO);
	            consumoZerado.setInjetado(0);
	            consumoZerado.setCompensado(0);
	            consumoZerado.setConsumido(0);
	           // consumoZerado.setAcumulado(BigDecimal.ZERO);
	          //  consumoZerado.setValorUnitarioCeb(BigDecimal.ZERO);
	           // consumoZerado.setDesconto(BigDecimal.ZERO);
//	            consumoZerado.setVencimento(LocalDate.of(ano, mes, 1)); // ou outra data padrão
//	            consumoZerado.setCarbonoEvitado(BigDecimal.ZERO);
//	            consumoZerado.setArvoresPlantadas(BigDecimal.ZERO);
	            
	            consumosCompletos.add(consumoZerado);
	        }
	    }
	    
	    // Ordenar por mês
	    consumosCompletos.sort(Comparator.comparingInt(uc -> uc.getConsumo().getMes()));
	    
	    return consumosCompletos;
	}
	
}
