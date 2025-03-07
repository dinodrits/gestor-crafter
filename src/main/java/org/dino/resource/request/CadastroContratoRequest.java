package org.dino.resource.request;

import org.dino.model.Contrato;

public class CadastroContratoRequest {
	
	boolean verificaDisponibilidade;
	Contrato contrato;
	
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
	
	

}
