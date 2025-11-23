package com.paintspray.enums;

/**
 * Enum para definir os tipos de usuário do sistema.
 */
public enum TipoUsuario {
    CLIENTE("Cliente"),
    FUNCIONARIO("Funcionário");

    private final String descricao;

    TipoUsuario(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    /**
     * Converte uma string para o enum correspondente.
     * 
     * @param tipo String representando o tipo
     * @return TipoUsuario correspondente
     */
    public static TipoUsuario fromString(String tipo) {
        if (tipo == null) {
            return null;
        }

        for (TipoUsuario tipoUsuario : TipoUsuario.values()) {
            if (tipoUsuario.name().equalsIgnoreCase(tipo)) {
                return tipoUsuario;
            }
        }

        throw new IllegalArgumentException("Tipo de usuário inválido: " + tipo);
    }
}
