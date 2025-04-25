package org.dino.model;

import java.math.BigDecimal;

import org.hibernate.envers.Audited;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "UnidadeConsumidoras")
@Audited
public class UnidadeConsumidora  extends PanacheEntityBase{
    
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUnidadeConsumidora", nullable = false)
	
    private Integer id;

    @Column(name = "codigoCliente", length = 45)
    private String codigoCliente;

    @Column(name = "numeroUC", length = 45)
    private String numeroUC;
    
    @Column(name = "descricao", length = 150)
    private String descricao;
    
    @Column(name = "percentual", precision = 10, scale = 4)
    private BigDecimal percentual;

   
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idCliente", nullable = false)
    private Cliente cliente;


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getCodigoCliente() {
		return codigoCliente;
	}


	public void setCodigoCliente(String codigoCliente) {
		this.codigoCliente = codigoCliente;
	}


	public String getNumeroUC() {
		return numeroUC;
	}


	public void setNumeroUC(String numeroUC) {
		this.numeroUC = numeroUC;
	}


	public Cliente getCliente() {
		return cliente;
	}


	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}


	public BigDecimal getPercentual() {
		return percentual;
	}


	public void setPercentual(BigDecimal percentual) {
		this.percentual = percentual;
	}


	public String getDescricao() {
		return descricao;
	}


	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}