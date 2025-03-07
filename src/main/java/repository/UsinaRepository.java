package repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.dino.model.Contrato;
import org.dino.model.Usina;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.NoResultException;

@ApplicationScoped
public class UsinaRepository implements PanacheRepository<Usina>{

	public List<Usina> getUsinasConsumo(){
		List<Object[]> resultList = getEntityManager().createNativeQuery("SELECT u.idUsina idUsina,u.nome nome, u.potencialProducao potencialProducao, u.cpfCnpj ,u.potencia potencia, u.potencialProducao - COALESCE(SUM(c.qtdContratada), 0) utilizado FROM Usinas u "
				+ "	LEFT JOIN Contratos c ON u.idUsina = c.idUsina "
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

	
	public Usina getUsinasConsumo(Contrato contrato){
		
		Object[] result;
		try {
			result = (Object[]) getEntityManager().createNativeQuery("SELECT u.idUsina idUsina,u.nome nome, u.potencialProducao potencialProducao, u.cpfCnpj ,u.potencia potencia, u.potencialProducao - COALESCE(SUM(c.qtdContratada), 0) utilizado FROM Usinas u "
					+ "	LEFT JOIN Contratos c ON u.idUsina = c.idUsina "
					+ "	WHERE ((c.dtInicio < :dtInicio AND c.dtFim > :dtInicio ) OR (c.dtInicio IS NULL AND c.dtFim IS NULL)) and u.idUsina = :idUsina "
					+ "GROUP BY u.idUsina,u.nome, u.potencialProducao, u.cpfCnpj, u.potencia").setParameter("idUsina", contrato.getUsina().getId()).setParameter("dtInicio", contrato.getDtInicio()).getSingleResult();
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
}
