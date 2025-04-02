package org.dino.model;

import jakarta.persistence.*;

import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonBackReference;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
@Table(name = "UsinasConsumo")
@Audited
public class UsinaConsumo extends PanacheEntityBase{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idUsinaConsumo", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idCliente", nullable = false)
    @JsonBackReference("consumo-cliente")
    private Cliente cliente;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idConsumo", nullable = false)
    @JsonBackReference("usinaConsumo-consumo")
    private Consumo consumo;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idUsina", nullable = false)
    //@JsonBackReference("usinaContato-usina")
    private Usina usina;
    
  

    @Column(name = "qtdConsumida")
    private Integer qtdConsumida;



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



	public Consumo getConsumo() {
		return consumo;
	}



	public void setConsumo(Consumo consumo) {
		this.consumo = consumo;
	}



	public Usina getUsina() {
		return usina;
	}



	public void setUsina(Usina usina) {
		this.usina = usina;
	}



	public Integer getQtdConsumida() {
		return qtdConsumida;
	}



	public void setQtdConsumida(Integer qtdConsumida) {
		this.qtdConsumida = qtdConsumida;
	}
    

	
    

}