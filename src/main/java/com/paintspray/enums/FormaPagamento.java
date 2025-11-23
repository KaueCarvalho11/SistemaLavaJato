package com.paintspray.enums;

/**
 * Enum para definir as formas de pagamento aceitas pela oficina.
 */
public enum FormaPagamento {
    PIX("Pix"),
    CREDITO("Credito"),
    DEBITO("Debito"),
    CEDULA("Cedula");

    private final String descricao;

    FormaPagamento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    /**
     * Converte uma string para o enum correspondente.
     * @param texto O texto a ser convertido
     * @return O enum correspondente
     * @throws IllegalArgumentException se o texto não corresponder a nenhum enum
     */
    public static FormaPagamento fromString(String texto) {
        for (FormaPagamento forma : FormaPagamento.values()) {
            if (forma.descricao.equalsIgnoreCase(texto) || forma.name().equalsIgnoreCase(texto)) {
                return forma;
            }
        }
        throw new IllegalArgumentException("Forma de pagamento inválida: " + texto);
    }

    @Override
    public String toString() {
        return descricao;
    }
}
