package org.dino.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.math.BigDecimal;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
public class Usinas extends PanacheEntityBase{
    @Id
    @Column(name = "idUsina", nullable = false)
    private Integer id;

    @Column(name = "nome", length = 150)
    private String nome;

    @Column(name = "potencialProducao", precision = 10, scale = 2)
    private BigDecimal potencialProducao;

    @Column(name = "latitude", length = 45)
    private String latitude;

    @Column(name = "longitude", length = 45)
    private String longitude;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getPotencialProducao() {
        return potencialProducao;
    }

    public void setPotencialProducao(BigDecimal potencialProducao) {
        this.potencialProducao = potencialProducao;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

}