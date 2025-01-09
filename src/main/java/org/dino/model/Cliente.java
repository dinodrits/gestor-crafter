package org.dino.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "Clientes")
public class Cliente  extends PanacheEntityBase{
    
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCliente", nullable = false)
    private Integer id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "codigoUnidade", length = 45)
    private String codigoUnidade;

    @Column(name = "qtdContratado")
    private Integer qtdContratada;
    
    @Column(name = "cpfCnpj", length = 18)
    private String cpfCnpj;
    
    @Column(name = "email", length = 50)
    private String email;
    
    @Column(name = "contato", length = 45)
    private String contato;
    
    @Column(name = "endereco", length = 150)
    private String endereco;

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

    public String getCodigoUnidade() {
        return codigoUnidade;
    }

    public void setCodigoUnidade(String codigoUnidade) {
        this.codigoUnidade = codigoUnidade;
    }

    public Integer getQtdContratada() {
        return qtdContratada;
    }

    public void setQtdContratada(Integer qtdContratada) {
        this.qtdContratada = qtdContratada;
    }

	public String getCpfCnpj() {
		return cpfCnpj;
	}

	public void setCpfCnpj(String cpfCnpj) {
		this.cpfCnpj = cpfCnpj;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContato() {
		return contato;
	}

	public void setContato(String contato) {
		this.contato = contato;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
    
	
    

}