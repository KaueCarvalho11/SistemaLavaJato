package com.paintspray.model;

import com.paintspray.enums.StatusServico;
import com.paintspray.enums.TipoServico;
import com.paintspray.enums.FormaPagamento;
import java.text.DecimalFormat;

public class Servico {

    private int idServico;
    private String descricao;
    private double preco;
    private StatusServico status;
    private FormaPagamento formaPagamento;
    private TipoServico tipo;
    private Veiculo veiculo;
    private Usuario usuario;
    DecimalFormat df = new DecimalFormat("0.00");

    // Construtor completo
    public Servico(int idServico, TipoServico tipo, String descricao, double preco,
            StatusServico status, FormaPagamento formaPagamento, Veiculo veiculo, Usuario usuario) {
        this.idServico = idServico;
        this.tipo = tipo;
        this.descricao = descricao;
        this.preco = preco;
        this.status = status;
        this.formaPagamento = formaPagamento;
        this.veiculo = veiculo;
        this.usuario = usuario;
    }

    // Construtor para novos serviços
    public Servico(TipoServico tipo, String descricao, Veiculo veiculo, Usuario usuario) {
        this.tipo = tipo;
        this.descricao = descricao;
        this.veiculo = veiculo;
        this.usuario = usuario;
        this.status = StatusServico.PENDENTE;
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

    public StatusServico getStatus() {
        return status;
    }

    public void setStatus(StatusServico status) {
        this.status = status;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public TipoServico getTipo() {
        return tipo;
    }

    public void setTipo(TipoServico tipo) {
        this.tipo = tipo;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "ID: " + idServico +
                "\nDescrição: " + descricao +
                "\nPreço: R$" + df.format(preco) +
                "\nStatus: " + (status != null ? status.getDescricao() : "N/A") +
                "\nForma de Pagamento: " + (formaPagamento != null ? formaPagamento.getDescricao() : "N/A") +
                "\nTipo: " + (tipo != null ? tipo.getDescricao() : "N/A") +
                "\nVeículo: " + veiculo.getModelo() +
                "\nUsuário: " + usuario.getNome();
    }
}
