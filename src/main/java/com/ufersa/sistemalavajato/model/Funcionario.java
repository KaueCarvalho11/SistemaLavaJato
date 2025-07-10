package com.ufersa.sistemalavajato.model;

public class Funcionario extends Usuario {

    // Construtor
    public Funcionario(String id, String nome, String email, String senha) {
        super(id, nome, email, senha);
    }

    @Override
    public String toString() {
        return "ID: " + getId() +
                "\nNome: " + getNome() +
                "\nEmail: " + getEmail() +
                "\nSenha=" + getSenha();
    }
}