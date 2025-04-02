package repository;

import java.math.BigDecimal;
import java.util.List;

import org.dino.model.Consumo;
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

	public List<Consumo> getFatura(int mes, int ano, String token) {
		// TODO Auto-generated method stub
		List<Consumo> c = getEntityManager().createNativeQuery("SELECT co.* FROM Consumos co LEFT JOIN Clientes c ON co.idCliente = c.idCliente WHERE c.token = :token AND co.mes = :mes AND co.ano = :ano", Consumo.class).setParameter("token", token).setParameter("mes", mes).setParameter("ano", ano).getResultList();
		return c;
	}
	
	
	
}
