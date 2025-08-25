package org.dino.model;

import java.math.BigDecimal;

import org.dino.util.Views;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonView;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(name = "Geracoes")
@Audited
public class Geracao extends PanacheEntityBase{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Lista.class)
    @Column(name = "idgeracoes", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonView(Views.Lista.class)
    @JoinColumn(name = "idUsina", nullable = false)
    private Usina usina;

    @Column(name = "mes")
    @JsonView(Views.Lista.class)
    private Integer mes;

    @Column(name = "ano")
    @JsonView(Views.Lista.class)
    private Integer ano;

    @Column(name = "qtdGerada", precision = 10, scale = 4)
    @JsonView(Views.Lista.class)
    private BigDecimal qtdGerada;
    
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    

    public Usina getUsina() {
		return usina;
	}

	public void setUsina(Usina usina) {
		this.usina = usina;
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

	public BigDecimal getQtdGerada() {
		return qtdGerada;
	}

	public void setQtdGerada(BigDecimal qtdGerada) {
		this.qtdGerada = qtdGerada;
	}

   

}