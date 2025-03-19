package repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.dino.model.Consumo;
import org.dino.model.Contrato;
import org.dino.model.Usina;
import org.dino.model.UsinaContrato;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.NoResultException;

@ApplicationScoped
public class ContratoRepository implements PanacheRepository<Usina>{

	

	
	public Contrato getContratoConsumo(Consumo consumo){
		
		Contrato result;
		try {
			
			LocalDate data = LocalDate.of(consumo.getAno(),consumo.getMes(),15);
			result = (Contrato) getEntityManager().createNativeQuery("SELECT * FROM Contratos c WHERE c.idCliente = :idCliente and  :data BETWEEN c.dtInicio AND c.dtFim").setParameter("idCliente", consumo.getCliente().getId()).setParameter("data", data).getSingleResult();
//			Usina u = new Usina(
//					Integer.parseInt( result[0].toString().trim()),  // id
//					(String) result[1].toString().trim(),  // nome
//					new BigDecimal( result[2].toString().trim()),// capacidade
//					(String) result[3].toString().trim(),// cpfCnpj
//					new BigDecimal(result[4].toString().trim()),// potencia
//					new BigDecimal(result[5].toString().trim())// utilizado
//					
//					);
//			return u;
			return result;
		} catch (NoResultException e) {
			// TODO Auto-generated catch block
			System.out.println("erro");
		}
		
		return null;
		

		
		
	}
	
	public Contrato getContratoVigente(Long id,Integer mes, Integer ano) {
		Object[] result;
		LocalDate data = LocalDate.of(ano,mes,1);
		
		try {
			result =  (Object[]) getEntityManager().createNativeQuery("SELECT * FROM Contratos c WHERE c.idCliente = :idCliente and  :data BETWEEN c.dtInicio AND c.dtFim").setParameter("idCliente", id).setParameter("data", data).getSingleResult();
			
		}catch (jakarta.persistence.NoResultException e) {
			// TODO: handle exception
			
			data = LocalDate.of(ano,mes,28);
			result = (Object[]) getEntityManager().createNativeQuery("SELECT * FROM Contratos c WHERE c.idCliente = :idCliente and  :data BETWEEN c.dtInicio AND c.dtFim").setParameter("idCliente", id).setParameter("data", data).getSingleResult();
			
		}
		Contrato c = new Contrato();
		c.setId(Integer.parseInt( result[0].toString().trim()));
		return Contrato.findById(c.getId());
		
	}
}
