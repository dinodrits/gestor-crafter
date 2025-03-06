package org.dino.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
@Table(name = "Consumos")
public class Consumo extends PanacheEntityBase{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idConsumo", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idContrato", nullable = false)
    private Contrato idContrato;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idCliente", nullable = false)
    private Cliente idCliente;

    @Column(name = "consumo")
    private Integer consumo;

    @Column(name = "valorTotal", precision = 10, scale = 2)
    private BigDecimal valorTotal;

    @Column(name = "valorUnitarioCeb", precision = 10, scale = 2)
    private BigDecimal valorUnitarioCeb;

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

    public Contrato getIdContrato() {
        return idContrato;
    }

    public void setIdContrato(Contrato idContrato) {
        this.idContrato = idContrato;
    }

    public Cliente getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Cliente idCliente) {
        this.idCliente = idCliente;
    }

    public Integer getConsumo() {
        return consumo;
    }

    public void setConsumo(Integer consumo) {
        this.consumo = consumo;
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

}