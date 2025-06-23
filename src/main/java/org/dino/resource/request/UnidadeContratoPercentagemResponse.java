package org.dino.resource.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;

import org.dino.model.UnidadeContrato;

public class UnidadeContratoPercentagemResponse {

    
	private UnidadeContrato unidadeContrato;
	private int saldoAnterior;
	private int injetado;
	private BigDecimal percentual;
	private BigDecimal saldoDevedorAnterior;
	
	
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
	public int getInjetado() {
		return injetado;
	}
	public void setInjetado(int injetado) {
		this.injetado = injetado;
	}
	public BigDecimal getPercentual() {
		return percentual;
	}
	public void setPercentual(BigDecimal percentual) {
		this.percentual = percentual;
	}
	public BigDecimal getSaldoDevedorAnterior() {
		return saldoDevedorAnterior;
	}
	public void setSaldoDevedorAnterior(BigDecimal saldoDevedorAnterior) {
		this.saldoDevedorAnterior = saldoDevedorAnterior;
	}

	
	
}