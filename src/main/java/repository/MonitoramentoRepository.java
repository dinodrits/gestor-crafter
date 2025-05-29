package repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.dino.model.Consumo;
import org.dino.model.Contrato;
import org.dino.model.Usina;
import org.dino.model.UsinaContrato;
import org.dino.resource.request.ChartDataResponse;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.NoResultException;

@ApplicationScoped
public class MonitoramentoRepository implements PanacheRepository<Usina>{

	

	
public ChartDataResponse getUltimosMonitoramentos(){
		
		YearMonth current = YearMonth.now();
	    YearMonth startDate = current.minusMonths(11);
	    
	    Map<String, BigDecimal> monthlyData = new LinkedHashMap<>();
	    Locale localeBR = new Locale("pt", "BR");
	    
	    
	    for (int i = 0; i < 12; i++) {
	        YearMonth month = startDate.plusMonths(i);
	        String monthName = month.getMonth().getDisplayName(TextStyle.SHORT, localeBR);
	        monthlyData.put(monthName, BigDecimal.ZERO); // Inicializa com 0
	    }
	    
	    
		List<Object[]> results  = getEntityManager().createNativeQuery("SELECT mk.tarifaBandeira,mk.mes,mk.ano FROM MonitoramentoKw mk ORDER BY mk.ano DESC, mk.mes DESC LIMIT 12").getResultList();
		
		results.forEach(row -> {
	        BigDecimal total = ((BigDecimal) row[0]);
	        int monthNumber = ((Number) row[1]).intValue();
	        
	        // Converter número para nome do mês
	        String monthName = Month.of(monthNumber).getDisplayName(TextStyle.SHORT, localeBR);
	        monthlyData.put(monthName, total);
	    });

		List<String> labels = new ArrayList<>(monthlyData.keySet());
	    List<BigDecimal> qtdGerada = new ArrayList<>(monthlyData.values());
	    
		return new ChartDataResponse(labels, qtdGerada);
	}

}
