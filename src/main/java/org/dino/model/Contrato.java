package org.dino.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonBackReference;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
@Audited
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



    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idCliente", nullable = false)
    @JsonBackReference("contrato-cliente")
    private Cliente cliente;
    
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "idUsina", nullable = false)
//    @JsonBackReference("contrato-usina")
//    private Usina usina;
    
    @OneToMany(mappedBy = "contrato", cascade = CascadeType.ALL, orphanRemoval = true)
   // @JsonBackReference("contrato-usina-contrato")
    private List<UsinaContrato> usinas;

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

	public List<UsinaContrato> getUsinas() {
		return usinas;
	}

	public void setUsinas(List<UsinaContrato> usinas) {
		this.usinas = usinas;
	}
	
	

}