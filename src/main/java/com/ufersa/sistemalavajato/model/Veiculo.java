package com.ufersa.sistemalavajato.model;

public class Veiculo {
    // Atributos
    private String modelo;
    private int numChassi;
    private double quilometragem;
    private double preco;
    private String cor;
    private int anoFabricacao;
    private String status;

    // Construtor
    public Veiculo(String modelo, int numChassi, double quilometragem, double preco, String cor, int anoFabricacao,
            String status) {
        this.modelo = modelo;
        this.numChassi = numChassi;
        this.quilometragem = quilometragem;
        this.preco = preco;
        this.cor = cor;
        this.anoFabricacao = anoFabricacao;
        this.status = status;
    }

    // Getters e Setters
    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getNumChassi() {
        return numChassi;
    }

    public void setNumChassi(int numChassi) {
        this.numChassi = numChassi;
    }

    public double getQuilometragem() {
        return quilometragem;
    }

    public void setQuilometragem(double quilometragem) {
        this.quilometragem = quilometragem;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Método auxiliar
    @Override
    public String toString() {
        return "Veiculo{modelo = " + modelo + ", numChassi = " + numChassi + ", quilometragem = " + quilometragem
                + ", preco = " + preco + ", cor = " + cor + ", anoFabricacao = " + anoFabricacao + ", status = "
                + status + "}";
    }
}
