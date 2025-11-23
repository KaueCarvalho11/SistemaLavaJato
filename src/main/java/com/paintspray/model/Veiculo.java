package com.paintspray.model;

public class Veiculo {
    // Atributos
    private int id;
    private String modelo;
    private String cor;
    private int anoFabricacao;
    private String idCliente;

    // Construtor completo
    public Veiculo(int id, String modelo, String cor, int anoFabricacao, String idCliente) {
        this.id = id;
        this.modelo = modelo;
        this.cor = cor;
        this.anoFabricacao = anoFabricacao;
        this.idCliente = idCliente;
    }

    // Construtor sem ID (para novos veículos)
    public Veiculo(String modelo, String cor, int anoFabricacao, String idCliente) {
        this.modelo = modelo;
        this.cor = cor;
        this.anoFabricacao = anoFabricacao;
        this.idCliente = idCliente;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public int getAnoFabricacao() {
        return anoFabricacao;
    }

    public void setAnoFabricacao(int anoFabricacao) {
        this.anoFabricacao = anoFabricacao;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    // Método auxiliar
    @Override
    public String toString() {
        return "ID: " + id +
                "\nModelo: " + modelo +
                "\nCor: " + cor +
                "\nAno de Fabricação: " + anoFabricacao +
                "\nCliente ID: " + idCliente;
    }
}
