package com.paintspray.enums;

/**
 * Enum para definir os tipos de serviço oferecidos pela oficina de pintura.
 */
public enum TipoServico {
    PINTURA_COMPLETA("Pintura completa"),
    RETOQUE_LOCALIZADO("Retoque localizado"),
    PINTURA_DE_PECAS("Pintura de peças"),
    ENVERNIZAMENTO("Envernizamento");

    private final String descricao;

    TipoServico(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    /**
     * Converte uma string para o enum correspondente.
     * 
     * @param texto O texto a ser convertido
     * @return O enum correspondente
     * @throws IllegalArgumentException se o texto não corresponder a nenhum enum
     */
    public static TipoServico fromString(String texto) {
        for (TipoServico tipo : TipoServico.values()) {
            if (tipo.descricao.equalsIgnoreCase(texto) || tipo.name().equalsIgnoreCase(texto)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de serviço inválido: " + texto);
    }

    @Override
    public String toString() {
        return descricao;
    }
}
