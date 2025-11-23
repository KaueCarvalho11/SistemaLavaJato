package com.ufersa.sistemalavajato.model;

import com.ufersa.sistemalavajato.enums.TipoUsuario;

public class Funcionario extends Usuario {

    // Construtor
    public Funcionario(String id, String nome, String email, String senha) {
        super(id, nome, email, senha);
    }

    @Override
    public TipoUsuario getTipoUsuario() {
        return TipoUsuario.FUNCIONARIO;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
