package org.dino.resource.request;

import java.math.BigDecimal;

public class ConsumoHistoricoValorResponse {
    public BigDecimal valorTotalContratado;
    public Integer idCliente;
    public String dtValor;
 
    
    
    
    
	public ConsumoHistoricoValorResponse(BigDecimal valorTotalContratado, Integer idCliente, String dtValor) {
		
		this.valorTotalContratado = valorTotalContratado;
		this.idCliente = idCliente;
		this.dtValor = dtValor;
	}
	public BigDecimal getValorTotalContratado() {
		return valorTotalContratado;
	}
	public void setValorTotalContratado(BigDecimal valorTotalContratado) {
		this.valorTotalContratado = valorTotalContratado;
	}
	public Integer getIdCliente() {
		return idCliente;
	}
	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}
	public String getDtValor() {
		return dtValor;
	}
	public void setDtValor(String dtValor) {
		this.dtValor = dtValor;
	}
	

	
  
}