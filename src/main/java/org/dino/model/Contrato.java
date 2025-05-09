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
    
    

    
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] arquivo;
    
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "idUsina", nullable = false)
//    @JsonBackReference("contrato-usina")
//    private Usina usina;
    
    @OneToMany(mappedBy = "contrato", cascade = CascadeType.ALL, orphanRemoval = true)
   // @JsonBackReference("contrato-usina-contrato")
    private List<UnidadeContrato> unidadesContratos;

    @Column(name = "qtdContratada", precision = 10, scale = 4)
    private BigDecimal qtdContratada;

    @Column(name = "qtdIsencao")
    private Integer qtdIsencao;
    
    @Column(name = "valorIsencao", precision = 10, scale = 4)
    private BigDecimal valorIsencao;

    
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


	public Integer getQtdIsencao() {
		return qtdIsencao;
	}

	public void setQtdIsencao(Integer qtdIsencao) {
		this.qtdIsencao = qtdIsencao;
	}

	


	public List<UnidadeContrato> getUnidadesContratos() {
		return unidadesContratos;
	}

	public void setUnidadesContratos(List<UnidadeContrato> unidadesContratos) {
		this.unidadesContratos = unidadesContratos;
	}

	public String getModalidadeFaturamento() {
		return modalidadeFaturamento;
	}

	public void setModalidadeFaturamento(String modalidadeFaturamento) {
		this.modalidadeFaturamento = modalidadeFaturamento;
	}

	public BigDecimal getQtdContratada() {
		return qtdContratada;
	}

	public void setQtdContratada(BigDecimal qtdContratada) {
		this.qtdContratada = qtdContratada;
	}

	public BigDecimal getValorIsencao() {
		return valorIsencao;
	}

	public void setValorIsencao(BigDecimal valorIsencao) {
		this.valorIsencao = valorIsencao;
	}

	public byte[] getArquivo() {
		return arquivo;
	}

	public void setArquivo(byte[] arquivo) {
		this.arquivo = arquivo;
	}


	
	
	

}