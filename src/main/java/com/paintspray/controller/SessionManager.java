package com.paintspray.controller;

import com.paintspray.model.Usuario;

/**
 * Gerenciador de sessão do usuário logado
 */
public class SessionManager {

    private static Usuario usuarioLogado;

    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public static void setUsuarioLogado(Usuario usuario) {
        usuarioLogado = usuario;
    }

    public static void logout() {
        usuarioLogado = null;
    }

    public static boolean isLogado() {
        return usuarioLogado != null;
    }
}
