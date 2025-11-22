package com.paintspray.model;

public class Cliente {

    // Atributos específicos do Cliente
    private String id;
    private String nome;
    private String endereco;
    private String numeroTelefone;

    /**
     * Construtor da classe Cliente.
     * 
     * @param nome           Nome do usuário.
     * @param endereco       Endereço específico do cliente.
     * @param numeroTelefone Telefone específico do cliente.
     */

    public Cliente(String id, String nome, String endereco, String numeroTelefone) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.numeroTelefone = numeroTelefone;
    }

    // Getters e Setters para os atributos específicos de Cliente.

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

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNumeroTelefone() {
        return numeroTelefone;
    }

    public void setNumeroTelefone(String numeroTelefone) {
        this.numeroTelefone = numeroTelefone;
    }

    @Override
    public String toString() {
        return "Cliente: " + id + ", " + nome + ", " + endereco + ", " + numeroTelefone;
    }
}