package com.ufersa.sistemalavajato.model;

public class Servico {

    private int idServico;
    private String descricao;
    private double preco;
    private String status;
    private String formaPagamento;
    private String tipo;

    private Veiculo veiculo;
    private Funcionario funcionario;

    public Servico(int idServico, String tipo, Veiculo veiculo, Funcionario funcionario) {
        this.idServico = idServico;
        this.tipo = tipo;
        this.veiculo = veiculo;
        this.funcionario = funcionario;
    }


    // --- Getters e Setters ---
    public int getIdServico() {
        return idServico;
    }

    public void setIdServico(int idServico) {
        this.idServico = idServico;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    @Override
    public String toString() {
        return "Servico [idServico=" + idServico + ", descricao=" + descricao + ", preco=" + preco + ", status="
                + status + ", formaPagamento=" + formaPagamento + ", tipo=" + tipo + ", veiculo=" + veiculo
                + ", funcionario=" + funcionario + "]";
    }

}
