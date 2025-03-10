package org.dino.model;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
@Table(name = "UsinasContratos")
public class UsinaContrato extends PanacheEntityBase{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUsinaContrato", nullable = false)
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
    //@JsonBackReference("usinaContato-usina")
    private Usina usina;

    @Column(name = "qtdContratada")
    private Integer qtdContratada;

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

	public Usina getUsina() {
		return usina;
	}

	public void setUsina(Usina usina) {
		this.usina = usina;
	}

	public Integer getQtdContratada() {
		return qtdContratada;
	}

	public void setQtdContratada(Integer qtdContratada) {
		this.qtdContratada = qtdContratada;
	}
    
    

}