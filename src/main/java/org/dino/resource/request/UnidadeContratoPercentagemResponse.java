package org.dino.resource.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

import org.dino.model.UnidadeContrato;

public class UnidadeContratoPercentagemResponse {

    
	private UnidadeContrato unidadeContrato;
	private int saldoAnterior;
	
	
	public UnidadeContrato getUnidadeContrato() {
		return unidadeContrato;
	}
	public void setUnidadeContrato(UnidadeContrato unidadeContrato) {
		this.unidadeContrato = unidadeContrato;
	}
	public int getSaldoAnterior() {
		return saldoAnterior;
	}
	public void setSaldoAnterior(int saldoAnterior) {
		this.saldoAnterior = saldoAnterior;
	} 
	
	
	
}