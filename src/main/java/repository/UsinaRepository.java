package repository;

import java.math.BigDecimal;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.dino.model.Consumo;
import org.dino.model.Contrato;
import org.dino.model.Geracao;
import org.dino.model.UnidadeConsumidoraConsumo;
import org.dino.model.UnidadeContrato;
import org.dino.model.Usina;
import org.dino.model.UsinaContrato;
import org.dino.resource.request.ChartDataResponse;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.NoResultException;

@ApplicationScoped
public class UsinaRepository implements PanacheRepository<Usina>{

	public List<Usina> getUsinasConsumo(){
		List<Object[]> resultList = getEntityManager().createNativeQuery("SELECT u.idUsina idUsina,u.nome nome, u.potencialProducao potencialProducao, u.cpfCnpj cpfCnpj, u.potencia potencia,100 - COALESCE(SUM(uc.percentual), 0) disponivel "
				+ "FROM Usinas u "
				+ "	LEFT JOIN UnidadesContratos uc ON u.idUsina = uc.idUsina "
				+ "	LEFT JOIN Contratos c ON uc.idContrato = c.idContrato "
				+ "	WHERE (c.dtInicio < NOW() AND c.dtFim > NOW() ) OR (c.dtInicio IS NULL AND c.dtFim IS NULL) "
				+ "GROUP BY u.idUsina,u.nome, u.potencialProducao, u.cpfCnpj, u.potencia").getResultList();
		
		List<Usina> usinas = resultList.stream().map(row -> 
	    new Usina(
	        Integer.parseInt( row[0].toString().trim()),  // id
	        (String) row[1].toString().trim(),  // nome
	        new BigDecimal( row[2].toString().trim()),// capacidade
	        (String) row[3].toString().trim(),// cpfCnpj
	        new BigDecimal(row[4].toString().trim()),// potencia
	        new BigDecimal(row[5].toString().trim())// utilizado
	        
	    )
	).collect(Collectors.toList());
		
		return usinas;
	}

	
	public Usina getUsinasConsumo(Integer id){
		
		Object[] result;
		try {
			result = (Object[]) getEntityManager().createNativeQuery("SELECT u.idUsina idUsina,u.nome nome, u.potencialProducao potencialProducao,u.cpfCnpj cpfCnpj, u.potencia potencia,u.potencialProducao - COALESCE(SUM(uc.qtdContratada), 0) utilizado "
					+ "FROM Usinas u  "
					+ "	LEFT JOIN UsinasContratos uc ON u.idUsina = uc.idUsina "
					+ "	LEFT JOIN Contratos c ON uc.idContrato = c.idContrato "
					+ "	WHERE ((c.dtInicio < NOW() AND c.dtFim > NOW() ) OR (c.dtInicio IS NULL AND c.dtFim IS NULL)) and uc.idUsina = :idUsina  "
					+ "GROUP BY u.idUsina,u.nome, u.potencialProducao,u.cpfCnpj, u.potencia").setParameter("idUsina", id).getSingleResult();
			Usina u = new Usina(
					Integer.parseInt( result[0].toString().trim()),  // id
					(String) result[1].toString().trim(),  // nome
					new BigDecimal( result[2].toString().trim()),// capacidade
					(String) result[3].toString().trim(),// cpfCnpj
					new BigDecimal(result[4].toString().trim()),// potencia
					new BigDecimal(result[5].toString().trim())// utilizado
					
					);
			return u;
		} catch (NoResultException e) {
			// TODO Auto-generated catch block
			System.out.println("erro");
		}
		
		return null;
		
	}
	
	
	public BigDecimal getDisponibilidade(Integer id){
		BigDecimal result;
		try {
			result = (BigDecimal) getEntityManager().createNativeQuery("SELECT 100 - COALESCE(SUM(uc.percentual), 0) disponivel FROM  UnidadesContratos uc LEFT JOIN Contratos c ON uc.idContrato = c.idContrato WHERE  uc.idUsina = :id and c.dtInicio < NOW() AND c.dtFim > NOW()").setParameter("id", id).getSingleResult();
			
			return result;
		} catch (NoResultException e) {
			// TODO Auto-generated catch block
			return BigDecimal.ZERO;
		}
		
	}
	
	public BigDecimal getQtdInjetada(Long id, int mes, int ano){
		
		BigDecimal result;
		try {
			result = (BigDecimal) getEntityManager().createNativeQuery("SELECT SUM(injetado) FROM Consumos WHERE idUsina = :id AND mes = :mes AND ano = :ano").setParameter("id", id).setParameter("mes", mes).setParameter("ano", ano).getSingleResult();
			
			return result;
		} catch (NoResultException e) {
			// TODO Auto-generated catch block
			return BigDecimal.ZERO;
		}
		
		
		
	}


	public BigDecimal getMediaGeracao(Long id) {
		// TODO Auto-generated method stub
		try {
			return (BigDecimal) getEntityManager().createNativeQuery("SELECT AVG(qtdGerada) FROM Geracoes WHERE idUsina = :id ").setParameter("id", id).getSingleResult();
		} catch (NoResultException e) {
			// TODO Auto-generated catch block
			return BigDecimal.ZERO;
		}
		
	}
	public BigDecimal getMediaInjetada(Long id) {
		// TODO Auto-generated method stub
		try {
			return (BigDecimal) getEntityManager().createNativeQuery("SELECT AVG(ucc.injetado) FROM UnidadesConsumidorasConsumo ucc inner join Consumos c on c.idConsumo = ucc.idConsumo WHERE idUsina = :id ").setParameter("id", id).getSingleResult();
		} catch (NoResultException e) {
			// TODO Auto-generated catch block
			return BigDecimal.ZERO;
		}
		
	}
	
	public BigDecimal getTotalContratos(Long id) {
		// TODO Auto-generated method stub
		try {
			return (BigDecimal) getEntityManager().createNativeQuery("SELECT SUM(uc.percentual) FROM Contratos c INNER join UnidadesContratos uc ON c.idContrato = uc.idContrato  WHERE uc.idUsina = :id AND NOW() BETWEEN c.dtInicio AND c.dtFim").setParameter("id", id).getSingleResult();
		} catch (NoResultException e) {
			// TODO Auto-generated catch block
			return BigDecimal.ZERO;
		}
		
	}
	
	public int getQtdGerado(Integer id, int mes, int ano) {
		
		Map<String, Object> params2 = new HashMap<>();
    	params2.put("mes", mes);
    	params2.put("ano", ano);
    	params2.put("id", id);
    	
    	try {
    		Geracao g = Geracao.find("usina.id = :id and mes = :mes and ano = : ano", params2).singleResult();
    		return g.getQtdGerada().intValue();
    	}catch (Exception e) {
			// TODO: handle exception
		}
		
		return 0;
	}
	
	
	public List<UnidadeContrato> getContratosVigentes(Long id) {
		// TODO Auto-generated method stub
		List<UnidadeContrato> c = getEntityManager().createNativeQuery("SELECT uc.* FROM  UnidadesContratos  uc LEFT JOIN Contratos c ON uc.idContrato = c.idContrato WHERE  uc.idUsina = :id and c.dtInicio < NOW() AND c.dtFim > NOW()", UnidadeContrato.class).setParameter("id", id).getResultList();
		return c;
	}
	
	
	public ChartDataResponse getUltimasProducoes(){
		
		YearMonth current = YearMonth.now();
	    YearMonth startDate = current.minusMonths(11);
	    
	    Map<String, Integer> monthlyData = new LinkedHashMap<>();
	    Locale localeBR = new Locale("pt", "BR");
	    
	    
	    for (int i = 0; i < 12; i++) {
	        YearMonth month = startDate.plusMonths(i);
	        String monthName = month.getMonth().getDisplayName(TextStyle.SHORT, localeBR);
	        monthlyData.put(monthName, 0); // Inicializa com 0
	    }
	    
	    
		List<Object[]> results  = getEntityManager().createNativeQuery("SELECT sum(g.qtdGerada),g.mes,g.ano from  Geracoes g GROUP BY g.mes,g.ano   ORDER BY g.ano desc ,g.mes DESC ").getResultList();
		
		results.forEach(row -> {
	        int total = ((Number) row[0]).intValue();
	        int monthNumber = ((Number) row[1]).intValue();
	        
	        // Converter número para nome do mês
	        String monthName = Month.of(monthNumber).getDisplayName(TextStyle.SHORT, localeBR);
	        monthlyData.put(monthName, total);
	    });

		List<String> labels = new ArrayList<>(monthlyData.keySet());
	    List<Integer> qtdGerada = new ArrayList<>(monthlyData.values());
	    
		return new ChartDataResponse(labels, qtdGerada);
	}
	
}
