package org.dino.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonBackReference;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "Pagamentos")
@Audited
public class Pagamento  extends PanacheEntityBase{
    
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPagamento", nullable = false)
    private Integer id;

    @Column(name = "observacao")
    private String observacao;
    
    @Column(name = "formaPagamento")
    private String formaPagamento;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idCliente", nullable = false)
    private Cliente cliente;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idConsumo", nullable = false)
    private Consumo consumo;
    
    @Column(name = "valor", precision = 12, scale = 4, columnDefinition = "decimal(10,4) default 0.0000")
    private BigDecimal valor;
    
    @Column(name = "pago", columnDefinition = "boolean default false")
    private Boolean pago;
    
    @Column(name = "dtVencimento")
    private LocalDate dtVencimento;
    
    @Column(name = "dtPagamento")
    private LocalDate dtPagamento;
    
    @Column(name = "codigoGerado")
    private String codigoGerado;
    
    @Column(name = "txid",  unique = true)
    private String txid;
    
    @Column(name = "url")
    private String url;
    
    @Column(name = "chaveRecebimento")
    private String chaveRecebimento;
    
    @Column(name = "dtCriacao", nullable = false)
    private LocalDateTime dtCriacao;
    

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public String getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(String formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Consumo getConsumo() {
		return consumo;
	}

	public void setConsumo(Consumo consumo) {
		this.consumo = consumo;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Boolean getPago() {
		return pago;
	}

	public void setPago(Boolean pago) {
		this.pago = pago;
	}

	public LocalDate getDtVencimento() {
		return dtVencimento;
	}

	public void setDtVencimento(LocalDate dtVencimento) {
		this.dtVencimento = dtVencimento;
	}

	public LocalDate getDtPagamento() {
		return dtPagamento;
	}

	public void setDtPagamento(LocalDate dtPagamento) {
		this.dtPagamento = dtPagamento;
	}

	public String getCodigoGerado() {
		return codigoGerado;
	}

	public void setCodigoGerado(String codigoGerado) {
		this.codigoGerado = codigoGerado;
	}

	public String getTxid() {
		return txid;
	}

	public void setTxid(String txid) {
		this.txid = txid;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getChaveRecebimento() {
		return chaveRecebimento;
	}

	public void setChaveRecebimento(String chaveRecebimento) {
		this.chaveRecebimento = chaveRecebimento;
	}

	public LocalDateTime getDtCriacao() {
		return dtCriacao;
	}

	public void setDtCriacao(LocalDateTime dtCriacao) {
		this.dtCriacao = dtCriacao;
	}
	
	
    

}