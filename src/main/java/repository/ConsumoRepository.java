package repository;

import java.math.BigDecimal;

import org.dino.model.Usina;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
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
	
	
	
}
