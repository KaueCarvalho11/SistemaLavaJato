package com.paintspray.enums;

/**
 * Enum para definir os status de um serviço no sistema.
 * Representa o pipeline de trabalho da oficina.
 */
public enum StatusServico {
    PENDENTE("Pendente"),
    EM_ANDAMENTO("Em andamento"),
    AGUARDANDO_PAGAMENTO("Aguardando pagamento"),
    CANCELADO("Cancelado"),
    FINALIZADO("Finalizado");

    private final String descricao;

    StatusServico(String descricao) {
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
    public static StatusServico fromString(String texto) {
        for (StatusServico status : StatusServico.values()) {
            if (status.descricao.equalsIgnoreCase(texto) || status.name().equalsIgnoreCase(texto)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Status inválido: " + texto);
    }

    @Override
    public String toString() {
        return descricao;
    }
}
