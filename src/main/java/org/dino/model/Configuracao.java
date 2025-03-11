package org.dino.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
@Table(name = "Configuracao")
public class Configuracao extends PanacheEntityBase{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idConfiguracao", nullable = false)
    private Integer id;

    @Column(name = "valorCeb", precision = 10, scale = 2)
    private BigDecimal valorCeb;
    
    @Column(name = "fatorEconomiaArvore")
    private Integer fatorEconomiaArvore;
    
    @Column(name = "fatorCarbonoEvitado")
    private Integer fatorCarbonoEvitado;
    
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

	public BigDecimal getValorCeb() {
		return valorCeb;
	}

	public void setValorCeb(BigDecimal valorCeb) {
		this.valorCeb = valorCeb;
	}

	public Integer getFatorEconomiaArvore() {
		return fatorEconomiaArvore;
	}

	public void setFatorEconomiaArvore(Integer fatorEconomiaArvore) {
		this.fatorEconomiaArvore = fatorEconomiaArvore;
	}

	public Integer getFatorCarbonoEvitado() {
		return fatorCarbonoEvitado;
	}

	public void setFatorCarbonoEvitado(Integer fatorCarbonoEvitado) {
		this.fatorCarbonoEvitado = fatorCarbonoEvitado;
	}

    

 

    
}