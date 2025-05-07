package org.dino.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.hibernate.envers.Audited;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
@Table(name = "Consumos")
@Audited
public class Consumo extends PanacheEntityBase{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idConsumo", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idContrato", nullable = false)
    private Contrato contrato;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idCliente", nullable = false)
    private Cliente cliente;
    
    @OneToMany(mappedBy = "consumo", cascade = CascadeType.ALL, orphanRemoval = true)
   // @JsonBackReference("contrato-usina-contrato")
    private List<UnidadeConsumidoraConsumo> unidadesConsumos;

 
    @Column(name = "valorTotal", precision = 10, scale = 2)
    private BigDecimal valorTotal;
    
    @Column(name = "valorKw", precision = 10, scale = 4)
    private BigDecimal valorKw;
    
    @Column(name = "injetado", precision = 10, scale = 2)
    private BigDecimal injetado;
    
    @Column(name = "compensado", precision = 10, scale = 2)
    private BigDecimal compensado;
    
    @Column(name = "consumido", precision = 10, scale = 2)
    private BigDecimal consumido;
    
    @Column(name = "faturado", precision = 10, scale = 2)
    private BigDecimal faturado;
    
    @Column(name = "acumuladoMes", precision = 10, scale = 2)
    private BigDecimal acumuladoMes;

    @Column(name = "valorUnitarioCeb", precision = 10, scale = 4)
    private BigDecimal valorUnitarioCeb;
    
    @Column(name = "desconto", precision = 10, scale = 4)
    private BigDecimal desconto;
    
    @Column(name = "saldoAnterior", precision = 10, scale = 4)
    private BigDecimal saldoAnterior;

    @Column(name = "vencimento")
    private LocalDate vencimento;

    @Column(name = "carbonoEvitado", precision = 10, scale = 2)
    private BigDecimal carbonoEvitado;

    @Column(name = "arvoresPlantadas", precision = 10, scale = 2)
    private BigDecimal arvoresPlantadas;

    @Column(name = "mes")
    private Integer mes;

    @Column(name = "ano")
    private Integer ano;
    


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }



	public Contrato getContrato() {
		return contrato;
	}

	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public BigDecimal getValorUnitarioCeb() {
        return valorUnitarioCeb;
    }

    public void setValorUnitarioCeb(BigDecimal valorUnitarioCeb) {
        this.valorUnitarioCeb = valorUnitarioCeb;
    }

    public LocalDate getVencimento() {
        return vencimento;
    }

    public void setVencimento(LocalDate vencimento) {
        this.vencimento = vencimento;
    }

    public BigDecimal getCarbonoEvitado() {
        return carbonoEvitado;
    }

    public void setCarbonoEvitado(BigDecimal carbonoEvitado) {
        this.carbonoEvitado = carbonoEvitado;
    }

    public BigDecimal getArvoresPlantadas() {
        return arvoresPlantadas;
    }

    public void setArvoresPlantadas(BigDecimal arvoresPlantadas) {
        this.arvoresPlantadas = arvoresPlantadas;
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

	public BigDecimal getInjetado() {
		return injetado;
	}

	public void setInjetado(BigDecimal injetado) {
		this.injetado = injetado;
	}

	public BigDecimal getCompensado() {
		return compensado;
	}

	public void setCompensado(BigDecimal compensado) {
		this.compensado = compensado;
	}

	public BigDecimal getConsumido() {
		return consumido;
	}

	public void setConsumido(BigDecimal consumido) {
		this.consumido = consumido;
	}

	public BigDecimal getAcumuladoMes() {
		return acumuladoMes;
	}

	public void setAcumuladoMes(BigDecimal acumuladoMes) {
		this.acumuladoMes = acumuladoMes;
	}

	public BigDecimal getDesconto() {
		return desconto;
	}

	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}

	public BigDecimal getValorKw() {
		return valorKw;
	}

	public void setValorKw(BigDecimal valorKw) {
		this.valorKw = valorKw;
	}

	public BigDecimal getFaturado() {
		return faturado;
	}

	public void setFaturado(BigDecimal faturado) {
		this.faturado = faturado;
	}

	public BigDecimal getSaldoAnterior() {
		return saldoAnterior;
	}

	public void setSaldoAnterior(BigDecimal saldoAnterior) {
		this.saldoAnterior = saldoAnterior;
	}

	public List<UnidadeConsumidoraConsumo> getUnidadesConsumos() {
		return unidadesConsumos;
	}

	public void setUnidadesConsumos(List<UnidadeConsumidoraConsumo> unidadesConsumos) {
		this.unidadesConsumos = unidadesConsumos;
	}
	
	

    
}