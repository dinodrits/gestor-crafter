package org.dino.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.math.BigDecimal;
import java.time.LocalDate;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
@Table(name = "Usinas")
public class Usina extends PanacheEntityBase{
    @Id
    @Column(name = "idUsina", nullable = false)
    private Integer id;

    @Column(name = "nome", length = 150)
    private String nome;

    @Column(name = "potencialProducao", precision = 10, scale = 2)
    private BigDecimal capacidadeProducao;

    @Column(name = "endereco", length = 150)
    private String endereco;
    
    @Column(name = "cidade", length = 150)
    private String cidade;
    
    @Column(name = "uf", length = 2)
    private String uf;
    
    @Column(name = "cpfCnpj", length = 18)
    private String cpfCnpj;
    
    @Column(name = "contato", length = 50)
    private String contato;
    
    @Column(name = "email", length = 150)
    private String email;
    
    @Column(name = "potencia", precision = 10, scale = 2)
    private BigDecimal potencia;
    
    @Column(name = "dataCriacao")
    private LocalDate dataCriacao;
    
    @Transient
    private BigDecimal utilizado;
    
    
    public Usina() {
    	
    }

    public Usina(Integer id, String nome, BigDecimal capacidadeProducao, String cpfCnpj, BigDecimal potencia,
    		BigDecimal utilizado) {
		
		this.id = id;
		this.nome = nome;
		this.capacidadeProducao = capacidadeProducao;
		this.cpfCnpj = cpfCnpj;
		this.potencia = potencia;
		this.utilizado = utilizado;
	}

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

	public BigDecimal getCapacidadeProducao() {
		return capacidadeProducao;
	}

	public void setCapacidadeProducao(BigDecimal capacidadeProducao) {
		this.capacidadeProducao = capacidadeProducao;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getCpfCnpj() {
		return cpfCnpj;
	}

	public void setCpfCnpj(String cpfCnpj) {
		this.cpfCnpj = cpfCnpj;
	}

	public String getContato() {
		return contato;
	}

	public void setContato(String contato) {
		this.contato = contato;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public BigDecimal getPotencia() {
		return potencia;
	}

	public void setPotencia(BigDecimal potencia) {
		this.potencia = potencia;
	}

	public LocalDate getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(LocalDate dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public BigDecimal getUtilizado() {
		return utilizado;
	}

	public void setUtilizado(BigDecimal utilizado) {
		this.utilizado = utilizado;
	}
    
    

}