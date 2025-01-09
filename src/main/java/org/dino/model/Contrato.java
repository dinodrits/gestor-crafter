package org.dino.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
@Table(name = "contratos")
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
    private Cliente idCliente;

    @Column(name = "qtdContratada")
    private Integer qtdContratada;

    @Column(name = "diaVencimento")
    private Integer diaVencimento;

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

    public Cliente getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Cliente idCliente) {
        this.idCliente = idCliente;
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

}