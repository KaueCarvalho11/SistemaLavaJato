package com.ufersa.sistemalavajato.auth;

import com.ufersa.sistemalavajato.enums.TipoUsuario;
import com.ufersa.sistemalavajato.model.Usuario;

/**
 * Classe para gerenciar a sessão do usuário logado.
 * Implementa padrão Singleton.
 */
public class UserSession {

    private static UserSession instance;
    private Usuario currentUser;
    private TipoUsuario userType;
    private long loginTime;

    private UserSession() {
        this.currentUser = null;
        this.userType = null;
        this.loginTime = 0;
    }

    /**
     * Obtém a instância única da sessão.
     * 
     * @return Instância da UserSession
     */
    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    /**
     * Inicia uma nova sessão para o usuário.
     * 
     * @param user Usuário que fez login
     * @param type Tipo do usuário
     */
    public void startSession(Usuario user, TipoUsuario type) {
        this.currentUser = user;
        this.userType = type;
        this.loginTime = System.currentTimeMillis();
    }

    /**
     * Encerra a sessão atual.
     */
    public void endSession() {
        this.currentUser = null;
        this.userType = null;
        this.loginTime = 0;
    }

    /**
     * Verifica se há um usuário logado.
     * 
     * @return true se há usuário logado, false caso contrário
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }

    /**
     * Obtém o usuário atual.
     * 
     * @return Usuário atual ou null se não há usuário logado
     */
    public Usuario getCurrentUser() {
        return currentUser;
    }

    /**
     * Obtém o tipo do usuário atual.
     * 
     * @return Tipo do usuário atual ou null se não há usuário logado
     */
    public TipoUsuario getUserType() {
        return userType;
    }

    /**
     * Obtém o tempo de login em milissegundos.
     * 
     * @return Tempo de login
     */
    public long getLoginTime() {
        return loginTime;
    }

    /**
     * Verifica se o usuário atual é um cliente.
     * 
     * @return true se for cliente, false caso contrário
     */
    public boolean isCliente() {
        return userType == TipoUsuario.CLIENTE;
    }

    /**
     * Verifica se o usuário atual é um funcionário.
     * 
     * @return true se for funcionário, false caso contrário
     */
    public boolean isFuncionario() {
        return userType == TipoUsuario.FUNCIONARIO;
    }

    /**
     * Obtém informações da sessão para debug.
     * 
     * @return String com informações da sessão
     */
    public String getSessionInfo() {
        if (!isLoggedIn()) {
            return "Nenhum usuário logado";
        }

        return String.format("Usuário: %s (%s) - Logado em: %s",
                currentUser.getNome(),
                userType.getDescricao(),
                new java.util.Date(loginTime));
    }
}
