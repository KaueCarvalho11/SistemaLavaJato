package com.ufersa.sistemalavajato.model;

public class Cliente {
    // atributos
    private String endereco;
    private int numeroTelefone;

    // construtor
    public Cliente(String endereco, int numeroTelefone) {
        this.endereco = endereco;
        this.numeroTelefone = numeroTelefone;
    }

    // metodos getters e setters
    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public int getNumeroTelefone() {
        return numeroTelefone;
    }

    public void setNumeroTelefone(int numeroTelefone) {
        this.numeroTelefone = numeroTelefone;
    }

    // metodos CRUD
    public void cadastraCliente() {
    }

    public void removeCliente() {
    }

    public void atualizaCliente() {
    }

    public String listarCliente() {
        return "Cliente não encontrado";
    }

    public String exibirCliente() {
        return "Cliente nao encontrado";
    }

    // métodos auxiliares
    public String toString() {
        return "Endereço: " + endereco + " Numero de telefone: " + numeroTelefone;
    }
}