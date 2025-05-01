package org.dino.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonBackReference;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
@Table(name = "UnidadesContratos")
@Audited
public class UnidadeContrato extends PanacheEntityBase{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUnidadeContrato", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idCliente", nullable = false)
    @JsonBackReference("contrato-cliente")
    private Cliente cliente;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idContrato", nullable = false)
    @JsonBackReference("usinaContato-contrato")
    private Contrato contrato;
    
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

	public Contrato getContrato() {
		return contrato;
	}

	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
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

    
    

}