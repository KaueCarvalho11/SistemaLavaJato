package com.ufersa.sistemalavajato.model;

import com.ufersa.sistemalavajato.enums.TipoUsuario;

public class Cliente extends Usuario {

    // Atributos específicos do Cliente
    private String endereco;
    private String numeroTelefone;

    /**
     * Construtor da classe Cliente.
     * Inicializa tanto os atributos de Usuário (usando super()) quanto os de
     * Cliente.
     * 
     * @param id             ID do usuário (herdado).
     * @param nome           Nome do usuário (herdado).
     * @param email          Email do usuário (herdado).
     * @param senha          Senha do usuário (herdado).
     * @param endereco       Endereço específico do cliente.
     * @param numeroTelefone Telefone específico do cliente.
     */
    public Cliente(String id, String nome, String email, String senha, String endereco, String numeroTelefone) {
        // 'super()' chama o construtor da classe pai (Usuario) para inicializar os
        // campos herdados.
        super(id, nome, email, senha);
        this.endereco = endereco;
        this.numeroTelefone = numeroTelefone;
    }

    @Override
    public TipoUsuario getTipoUsuario() {
        return TipoUsuario.CLIENTE;
    }

    // Getters e Setters para os atributos específicos de Cliente.

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

    /**
     * Sobrescreve o método toString para fornecer uma representação textual
     * completa do objeto Cliente.
     * Útil para logs e impressões rápidas.
     */
    @Override
    public String toString() {
        return "Cliente: " + getNome() + "\n" +
                "Email: " + getEmail() + "\n" +
                "Endereço: " + endereco + "\n" +
                "Telefone: " + numeroTelefone;
    }
}