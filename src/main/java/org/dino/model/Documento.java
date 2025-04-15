package org.dino.model;

import org.hibernate.envers.Audited;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Audited
@Table(name = "Documento")
public class Documento {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idDocumento", nullable = false)
    private Integer id;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] conteudo;



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public byte[] getConteudo() {
		return conteudo;
	}

	public void setConteudo(byte[] conteudo) {
		this.conteudo = conteudo;
	}
    

	
}
