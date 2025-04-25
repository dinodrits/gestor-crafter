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
    
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idDocumento", nullable = false)
    private Documento documento;
    
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "idUsina", nullable = false)
//    @JsonBackReference("contrato-usina")
//    private Usina usina;
    
    @OneToMany(mappedBy = "contrato", cascade = CascadeType.ALL, orphanRemoval = true)
   // @JsonBackReference("contrato-usina-contrato")
    private List<UsinaContrato> usinas;

    @Column(name = "qtdContratada", precision = 10, scale = 4)
    private BigDecimal qtdContratada;

    @Column(name = "qtdIsencao")
    private Integer qtdIsencao;
    
    @Column(name = "valorIsencao", precision = 10, scale = 4)
    private BigDecimal valorIsencao;

    @Column(name = "diaVencimento")
    private Integer diaVencimento;
    
    @Column(name = "diaLeitura")
    private String diaLeitura;
    
    @Column(name = "modalidadeFaturamento")
    private String modalidadeFaturamento;
    


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

	
	public String getDiaLeitura() {
		return diaLeitura;
	}

	public void setDiaLeitura(String diaLeitura) {
		this.diaLeitura = diaLeitura;
	}

	public List<UsinaContrato> getUsinas() {
		return usinas;
	}

	public void setUsinas(List<UsinaContrato> usinas) {
		this.usinas = usinas;
	}

	public Documento getDocumento() {
		return documento;
	}

	public void setDocumento(Documento documento) {
		this.documento = documento;
	}

	public String getModalidadeFaturamento() {
		return modalidadeFaturamento;
	}

	public void setModalidadeFaturamento(String modalidadeFaturamento) {
		this.modalidadeFaturamento = modalidadeFaturamento;
	}
	
	

}