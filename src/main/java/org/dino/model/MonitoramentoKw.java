package org.dino.model;

import java.math.BigDecimal;

import org.hibernate.envers.Audited;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "MonitoramentoKw")
@Audited
public class MonitoramentoKw extends PanacheEntityBase{

	
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 @Column(name = "idMonitoramentoKw", nullable = false)
	 private Integer id; 
	 
	 @Column(name = "tarifaBandeira", precision = 10, scale = 4)
	 private BigDecimal tarifaBandeira;
	 
	 @Column(name = "tarifaCompleta", precision = 10, scale = 4)
	 private BigDecimal tarifaCompleta;
	 
	 @Column(name = "icms", precision = 10, scale = 4)
	 private BigDecimal icms;
	 
	 @Column(name = "confins", precision = 10, scale = 4)
	 private BigDecimal cofins;
	 
	 @Column(name = "pis", precision = 10, scale = 4)
	 private BigDecimal pis;
	 
	 @Column(name = "tarifa", precision = 10, scale = 4)
	 private BigDecimal tarifa;

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

	public BigDecimal getTarifaBandeira() {
		return tarifaBandeira;
	}

	public void setTarifaBandeira(BigDecimal tarifaBandeira) {
		this.tarifaBandeira = tarifaBandeira;
	}

	public BigDecimal getTarifaCompleta() {
		return tarifaCompleta;
	}

	public void setTarifaCompleta(BigDecimal tarifaCompleta) {
		this.tarifaCompleta = tarifaCompleta;
	}

	public BigDecimal getIcms() {
		return icms;
	}

	public void setIcms(BigDecimal icms) {
		this.icms = icms;
	}



	public BigDecimal getCofins() {
		return cofins;
	}

	public void setCofins(BigDecimal cofins) {
		this.cofins = cofins;
	}

	public BigDecimal getPis() {
		return pis;
	}

	public void setPis(BigDecimal pis) {
		this.pis = pis;
	}

	public BigDecimal getTarifa() {
		return tarifa;
	}

	public void setTarifa(BigDecimal tarifa) {
		this.tarifa = tarifa;
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
