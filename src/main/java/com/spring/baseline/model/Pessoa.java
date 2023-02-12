package com.spring.baseline.model;

import java.io.Serializable;
import org.hibernate.validator.constraints.Length;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "tb_pessoa")
public class Pessoa implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "O campo ''Nome'' não pode ficar vazio")
	@Length(min = 3, max = 50)
	private String nome;
	
	@NotBlank(message = "O campo ''Sobrenome'' não pode ficar vazio")
	@Length(min = 3, max = 50)
	private String sobrenome;
	
	@Valid //faz com que os seus atributos sejam validados no hasErrors
	@OneToOne(cascade = CascadeType.ALL)
	private Usuario usuario;

	public Pessoa() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}
