package org.dino.resource.request;

import java.math.BigDecimal;
import java.time.LocalDate;
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
public class CadastroCobrancaRequest {
	
	Consumo consumo;
	LocalDate vencimento;
	BigDecimal valor;
	String tipoPagamento;
	boolean pago;
	boolean gerarBoletoPix;

	public Consumo getConsumo() {
		return consumo;
	}

	public void setConsumo(Consumo consumo) {
		this.consumo = consumo;
	}

	public LocalDate getVencimento() {
		return vencimento;
	}

	public void setVencimento(LocalDate vencimento) {
		this.vencimento = vencimento;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public String getTipoPagamento() {
		return tipoPagamento;
	}

	public void setTipoPagamento(String tipoPagamento) {
		this.tipoPagamento = tipoPagamento;
	}

	public boolean isPago() {
		return pago;
	}

	public void setPago(boolean pago) {
		this.pago = pago;
	}

	public boolean isGerarBoletoPix() {
		return gerarBoletoPix;
	}

	public void setGerarBoletoPix(boolean gerarBoletoPix) {
		this.gerarBoletoPix = gerarBoletoPix;
	}

	

}
