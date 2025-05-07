package org.dino.model;

import java.util.List;

import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonBackReference;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;

@Entity
@Table(name = "Clientes")
@Audited
public class Cliente  extends PanacheEntityBase{
    
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCliente", nullable = false)
    private Integer id;

    @Column(name = "nome")
    private String nome;



    
    @Column(name = "cpfCnpj", length = 18, unique = true)
    private String cpfCnpj;
    
    @Column(name = "email", length = 50)
    private String email;
    
    @Column(name = "contato", length = 45)
    private String contato;
    
    @Column(name = "endereco", length = 150)
    private String endereco;
    
    @Column(name = "cidade", length = 150)
    private String cidade;
    
    @Column(name = "cep", length = 10)
    private String cep;
    
    @Column(name = "uf", length = 2)
    private String uf;
    
    @Column(name = "classificacao", length = 2)
    private String classificacao;
    
    @Column(name = "saldoAcumulado")
    private Integer saldoAcumulado;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference("contrato")
    public List<Contrato> contratos;
    
    
    @Column(name = "token", length = 150)
    private String token;
    
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

	public String getClassificacao() {
		return classificacao;
	}

	public void setClassificacao(String classificacao) {
		this.classificacao = classificacao;
	}

	public List<Contrato> getContratos() {
		return contratos;
	}

	public void setContratos(List<Contrato> contratos) {
		this.contratos = contratos;
	}

	public Integer getSaldoAcumulado() {
		return saldoAcumulado;
	}

	public void setSaldoAcumulado(Integer saldoAcumulado) {
		this.saldoAcumulado = saldoAcumulado;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}
    
	
	
    

}