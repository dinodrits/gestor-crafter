package org.dino.resource.request;

import java.math.BigDecimal;

import org.dino.model.Consumo;
import org.dino.model.UnidadeConsumidora;
import org.dino.model.Usina;

public class ConsumoRelatorioResponse {

	private UnidadeConsumidora unidadeConsumidora;
	private Usina usina;
	private BigDecimal percentual;
	private Integer injetado;
	private Integer compensado;
	private Integer consumido;
	private Integer fatura;
	private Integer saldo;
	private Integer mes;
	private Integer ano;
	private Integer totalContratado;
	private Consumo consumo;
	private BigDecimal valorKwCeb;
	private BigDecimal valorKw;
	private BigDecimal valorTotal;
	private BigDecimal valorContratado;
	
	
	
	public BigDecimal getValorKw() {
		return valorKw;
	}
	public void setValorKw(BigDecimal valorKw) {
		this.valorKw = valorKw;
	}
	public BigDecimal getValorTotal() {
		return valorTotal;
	}
	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}
	public UnidadeConsumidora getUnidadeConsumidora() {
		return unidadeConsumidora;
	}
	public void setUnidadeConsumidora(UnidadeConsumidora unidadeConsumidora) {
		this.unidadeConsumidora = unidadeConsumidora;
	}
	public Usina getUsina() {
		return usina;
	}
	public void setUsina(Usina usina) {
		this.usina = usina;
	}
	public BigDecimal getPercentual() {
		return percentual;
	}
	public void setPercentual(BigDecimal percentual) {
		this.percentual = percentual;
	}
	public Integer getInjetado() {
		return injetado;
	}
	public void setInjetado(Integer injetado) {
		this.injetado = injetado;
	}
	public Integer getCompensado() {
		return compensado;
	}
	public void setCompensado(Integer compensado) {
		this.compensado = compensado;
	}
	public Integer getConsumido() {
		return consumido;
	}
	public void setConsumido(Integer consumido) {
		this.consumido = consumido;
	}
	public Integer getFatura() {
		return fatura;
	}
	public void setFatura(Integer fatura) {
		this.fatura = fatura;
	}
	public Integer getSaldo() {
		return saldo;
	}
	public void setSaldo(Integer saldo) {
		this.saldo = saldo;
	}
	public Integer getMes() {
		return mes;
	}
	public void setMes(Integer mes) {
		this.mes = mes;
	}
	public Integer getAno() {
		return ano;
	}
	public void setAno(Integer ano) {
		this.ano = ano;
	}
	public Consumo getConsumo() {
		return consumo;
	}
	public void setConsumo(Consumo consumo) {
		this.consumo = consumo;
	}
	public BigDecimal getValorKwCeb() {
		return valorKwCeb;
	}
	public void setValorKwCeb(BigDecimal valorKwCeb) {
		this.valorKwCeb = valorKwCeb;
	}
	public BigDecimal getValorContratado() {
		return valorContratado;
	}
	public void setValorContratado(BigDecimal valorContratado) {
		this.valorContratado = valorContratado;
	}
	public Integer getTotalContratado() {
		return totalContratado;
	}
	public void setTotalContratado(Integer totalContratado) {
		this.totalContratado = totalContratado;
	}
	
	
	
	
	

}
