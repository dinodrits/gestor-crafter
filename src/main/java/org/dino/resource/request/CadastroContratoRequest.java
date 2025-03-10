package org.dino.resource.request;

import java.util.List;

import org.dino.model.Contrato;
import org.dino.model.UsinaContrato;

/**
 * 
 */
public class CadastroContratoRequest {
	
	boolean verificaDisponibilidade;
	Contrato contrato;
	List<UsinaContrato> usinas;
	
	public boolean isVerificaDisponibilidade() {
		return verificaDisponibilidade;
	}
	public void setVerificaDisponibilidade(boolean verificaDisponibilidade) {
		this.verificaDisponibilidade = verificaDisponibilidade;
	}
	public Contrato getContrato() {
		return contrato;
	}
	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
	}
	public List<UsinaContrato> getUsinas() {
		return usinas;
	}
	public void setUsinas(List<UsinaContrato> usinas) {
		this.usinas = usinas;
	}
	
	
	

}
