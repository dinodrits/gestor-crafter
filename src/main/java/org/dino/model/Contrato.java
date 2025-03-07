package org.dino.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
@Table(name = "Contratos")
public class Contrato extends PanacheEntityBase{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idContrato", nullable = false)
    private Integer id;

    @Column(name = "prazo")
    private Integer prazo;

    @Column(name = "dtInicio")
    private LocalDate dtInicio;

    @Column(name = "dtFim")
    private LocalDate dtFim;

    @Column(name = "valorAluguel", precision = 10, scale = 2)
    private BigDecimal valorAluguel;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idCliente", nullable = false)
    @JsonBackReference("contrato-cliente")
    private Cliente cliente;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idUsina", nullable = false)
    @JsonBackReference("contrato-usina")
    private Usina usina;

    @Column(name = "qtdContratada")
    private Integer qtdContratada;

    @Column(name = "qtdIsencao")
    private Integer qtdIsencao;

    @Column(name = "diaVencimento")
    private Integer diaVencimento;
    
    @Column(name = "diaLeitura")
    private Integer diaLeitura;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPrazo() {
        return prazo;
    }

    public void setPrazo(Integer prazo) {
        this.prazo = prazo;
    }

    public LocalDate getDtInicio() {
        return dtInicio;
    }

    public void setDtInicio(LocalDate dtInicio) {
        this.dtInicio = dtInicio;
    }

    public LocalDate getDtFim() {
        return dtFim;
    }

    public void setDtFim(LocalDate dtFim) {
        this.dtFim = dtFim;
    }

    public BigDecimal getValorAluguel() {
        return valorAluguel;
    }

    public void setValorAluguel(BigDecimal valorAluguel) {
        this.valorAluguel = valorAluguel;
    }



    public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Integer getQtdContratada() {
        return qtdContratada;
    }

    public void setQtdContratada(Integer qtdContratada) {
        this.qtdContratada = qtdContratada;
    }

    public Integer getDiaVencimento() {
        return diaVencimento;
    }

    public void setDiaVencimento(Integer diaVencimento) {
        this.diaVencimento = diaVencimento;
    }

	public Integer getQtdIsencao() {
		return qtdIsencao;
	}

	public void setQtdIsencao(Integer qtdIsencao) {
		this.qtdIsencao = qtdIsencao;
	}

	public Integer getDiaLeitura() {
		return diaLeitura;
	}

	public void setDiaLeitura(Integer diaLeitura) {
		this.diaLeitura = diaLeitura;
	}

	public Usina getUsina() {
		return usina;
	}

	public void setUsina(Usina usina) {
		this.usina = usina;
	}
    
    

}