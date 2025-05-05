package org.dino.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonBackReference;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
@Table(name = "UnidadesConsumidorasConsumo")
@Audited
public class UnidadeConsumidoraConsumo extends PanacheEntityBase{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUnidadeConsumo", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idCliente", nullable = false)
    @JsonBackReference("consumo-cliente")
    private Cliente cliente;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idConsumo", nullable = false)
    @JsonBackReference("unidadeconsumidora-consumo")
    private Consumo consumo;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idUsina", nullable = false)
    //@JsonBackReference("unidadeContato-usina")
    private Usina usina;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idUnidadeConsumidora", nullable = false)
    //@JsonBackReference("usinaContato-usina")
    private UnidadeConsumidora unidadeConsumidora;

    @Column(name = "percentual", precision = 10, scale = 4)
    private BigDecimal percentual;
    
    @Column(name = "injetado", precision = 10, scale = 2)
    private BigDecimal injetado;
    
    @Column(name = "compensado", precision = 10, scale = 2)
    private BigDecimal compensado;
    
    @Column(name = "consumido", precision = 10, scale = 2)
    private BigDecimal consumido;
    
    @Column(name = "fatura", precision = 10, scale = 2)
    private BigDecimal fatura;
    
    @Column(name = "saldoAnterior")
    private Integer saldoAnterior;
    
    @Column(name = "saldo")
    private Integer saldo;
    

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	
	public UnidadeConsumidora getUnidadeConsumidora() {
		return unidadeConsumidora;
	}

	public void setUnidadeConsumidora(UnidadeConsumidora unidadeConsumidora) {
		this.unidadeConsumidora = unidadeConsumidora;
	}

	public BigDecimal getPercentual() {
		return percentual;
	}

	public void setPercentual(BigDecimal percentual) {
		this.percentual = percentual;
	}

	public Usina getUsina() {
		return usina;
	}

	public void setUsina(Usina usina) {
		this.usina = usina;
	}

	public Consumo getConsumo() {
		return consumo;
	}

	public void setConsumo(Consumo consumo) {
		this.consumo = consumo;
	}

	public BigDecimal getInjetado() {
		return injetado;
	}

	public void setInjetado(BigDecimal injetado) {
		this.injetado = injetado;
	}

	public BigDecimal getCompensado() {
		return compensado;
	}

	public void setCompensado(BigDecimal compensado) {
		this.compensado = compensado;
	}

	public BigDecimal getConsumido() {
		return consumido;
	}

	public void setConsumido(BigDecimal consumido) {
		this.consumido = consumido;
	}

	public BigDecimal getFatura() {
		return fatura;
	}

	public void setFatura(BigDecimal fatura) {
		this.fatura = fatura;
	}

	public Integer getSaldoAnterior() {
		return saldoAnterior;
	}

	public void setSaldoAnterior(Integer saldoAnterior) {
		this.saldoAnterior = saldoAnterior;
	}

	public Integer getSaldo() {
		return saldo;
	}

	public void setSaldo(Integer saldo) {
		this.saldo = saldo;
	}

	
    
    

}