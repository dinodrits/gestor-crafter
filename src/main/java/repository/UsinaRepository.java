package repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.dino.model.Consumo;
import org.dino.model.Contrato;
import org.dino.model.Geracao;
import org.dino.model.UnidadeConsumidoraConsumo;
import org.dino.model.UnidadeContrato;
import org.dino.model.Usina;
import org.dino.model.UsinaContrato;

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
			return (BigDecimal) getEntityManager().createNativeQuery("SELECT AVG(injetado) FROM Consumos WHERE idUsina = :id GROUP BY mes , ano").setParameter("id", id).getSingleResult();
		} catch (NoResultException e) {
			// TODO Auto-generated catch block
			return BigDecimal.ZERO;
		}
		
	}
	
	public BigDecimal getTotalContratos(Long id) {
		// TODO Auto-generated method stub
		try {
			return (BigDecimal) getEntityManager().createNativeQuery("SELECT SUM(uc.qtdContratada) FROM Contratos c INNER join UsinasContratos uc ON c.idContrato = uc.idContrato  WHERE uc.idUsina = :id AND NOW() BETWEEN c.dtInicio AND c.dtFim").setParameter("id", id).getSingleResult();
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
	
}
