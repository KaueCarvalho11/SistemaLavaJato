package com.paintspray.model;

public class Usuario {

	private String id;
	private String nome;
	private String email;
	private String senha;
	private String senhaHash;

	public Usuario() {
    }

	/**
     * Construtor para criação de novo usuário (Proprietário).
     */
	public Usuario(String id, String nome, String email, String senha) {
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.senha = senha;
	}

	/**
     * Construtor completo (ex: carregamento do banco de dados).
     */
	public Usuario(String id, String nome, String email, String senha, String senhaHash) {
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.senha = senha;
		this.senhaHash = senhaHash;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getSenhaHash() {
		return senhaHash;
	}

	public void setSenhaHash(String senhaHash) {
		this.senhaHash = senhaHash;
	}

	@Override
	public String toString() {
		return "Usuário (Proprietário): " + nome + " [" + email + "]";
	}

}
