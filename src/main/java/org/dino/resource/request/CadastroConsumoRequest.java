package org.dino.resource.request;

import java.util.List;

import org.dino.model.Cliente;
import org.dino.model.Consumo;
import org.dino.model.Contrato;
import org.dino.model.UnidadeConsumidoraConsumo;
import org.dino.model.UnidadeContrato;
import org.dino.model.UsinaContrato;

/**
 * 
 */
public class CadastroConsumoRequest {
	
	
	Consumo consumo;
	
	List<UnidadeConsumidoraConsumo> unidadesConsumos;

	public Consumo getConsumo() {
		return consumo;
	}

	public void setConsumo(Consumo consumo) {
		this.consumo = consumo;
	}

	public List<UnidadeConsumidoraConsumo> getUnidadesConsumos() {
		return unidadesConsumos;
	}

	public void setUnidadesConsumos(List<UnidadeConsumidoraConsumo> unidadesConsumos) {
		this.unidadesConsumos = unidadesConsumos;
	}
	
	
	
	
	
	
	

}
