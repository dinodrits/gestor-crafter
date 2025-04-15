package repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.dino.model.Consumo;

import org.dino.model.Usina;

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

	public List<Consumo> getFatura(int mes, int ano, String token) {
		// TODO Auto-generated method stub
		List<Consumo> c = getEntityManager().createNativeQuery("SELECT co.* FROM Consumos co LEFT JOIN Clientes c ON co.idCliente = c.idCliente WHERE c.token = :token AND co.mes = :mes AND co.ano = :ano", Consumo.class).setParameter("token", token).setParameter("mes", mes).setParameter("ano", ano).getResultList();
		return c;
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
	
	public List<Consumo> completarConsumosAno(List<Consumo> consumosExistentes, int ano) {
	    List<Consumo> consumosCompletos = new ArrayList<>();
	    
	    // Criar um mapa para facilitar a busca por mês
	    Map<Integer, Consumo> consumoPorMes = consumosExistentes.stream()
	        .collect(Collectors.toMap(Consumo::getMes, Function.identity()));
	    
	    // Gerar todos os meses do ano (1 a 12)
	    for (int mes = 1; mes <= 12; mes++) {
	        if (consumoPorMes.containsKey(mes)) {
	            // Se existe consumo para este mês, adiciona o existente
	            consumosCompletos.add(consumoPorMes.get(mes));
	        } else {
	            // Se não existe, cria um novo consumo zerado
	            Consumo consumoZerado = new Consumo();
	            consumoZerado.setMes(mes);
	            consumoZerado.setAno(ano);
	            consumoZerado.setValorTotal(BigDecimal.ZERO);
	            consumoZerado.setValorKw(BigDecimal.ZERO);
	            consumoZerado.setInjetado(BigDecimal.ZERO);
	            consumoZerado.setCompensado(BigDecimal.ZERO);
	            consumoZerado.setConsumido(BigDecimal.ZERO);
	            consumoZerado.setAcumuladoMes(BigDecimal.ZERO);
	            consumoZerado.setValorUnitarioCeb(BigDecimal.ZERO);
	            consumoZerado.setDesconto(BigDecimal.ZERO);
	            consumoZerado.setVencimento(LocalDate.of(ano, mes, 1)); // ou outra data padrão
	            consumoZerado.setCarbonoEvitado(BigDecimal.ZERO);
	            consumoZerado.setArvoresPlantadas(BigDecimal.ZERO);
	            
	            consumosCompletos.add(consumoZerado);
	        }
	    }
	    
	    // Ordenar por mês
	    consumosCompletos.sort(Comparator.comparingInt(Consumo::getMes));
	    
	    return consumosCompletos;
	}
	
}
