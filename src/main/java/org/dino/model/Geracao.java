package org.dino.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(name = "Geracoes")
public class Geracao extends PanacheEntityBase{
    @Id
    @Column(name = "idgeracoes", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idUsina", nullable = false)
    private Usina idUsina;

    @Column(name = "mes")
    private Integer mes;

    @Column(name = "ano")
    private Integer ano;

    @Column(name = "qtdGerada")
    private Integer qtdGerada;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usina getIdUsina() {
        return idUsina;
    }

    public void setIdUsina(Usina idUsina) {
        this.idUsina = idUsina;
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

    public Integer getQtdGerada() {
        return qtdGerada;
    }

    public void setQtdGerada(Integer qtdGerada) {
        this.qtdGerada = qtdGerada;
    }

}