package com.ufersa.sistemalavajato.auth;

import com.ufersa.sistemalavajato.enums.TipoUsuario;
import com.ufersa.sistemalavajato.model.Cliente;
import com.ufersa.sistemalavajato.model.Funcionario;
import com.ufersa.sistemalavajato.model.Usuario;
import com.ufersa.sistemalavajato.repository.UsuarioRepository;
import com.ufersa.sistemalavajato.util.PasswordUtils;
import com.ufersa.sistemalavajato.util.ValidationUtils;

import java.sql.SQLException;
import java.util.UUID;

/**
 * Serviço para gerenciar autenticação de usuários.
 */
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final UserSession userSession;

    public AuthService() {
        this.usuarioRepository = new UsuarioRepository();
        this.userSession = UserSession.getInstance();
    }

    /**
     * Realiza login de um usuário.
     * 
     * @param email    Email do usuário
     * @param password Senha do usuário
     * @return true se login bem-sucedido, false caso contrário
     */
    public boolean login(String email, String password) {
        try {
            // Valida entrada
            if (!ValidationUtils.isValidEmail(email) || password == null || password.trim().isEmpty()) {
                return false;
            }

            // Busca usuário por email
            Usuario usuario = usuarioRepository.findByEmail(email.trim());
            if (usuario == null) {
                return false;
            }

            // Valida senha
            if (!PasswordUtils.validatePassword(password, usuario.getSenhaHash())) {
                return false;
            }

            // Determina tipo do usuário
            TipoUsuario tipo = (usuario instanceof Cliente) ? TipoUsuario.CLIENTE : TipoUsuario.FUNCIONARIO;

            // Inicia sessão
            userSession.startSession(usuario, tipo);

            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao realizar login: " + e.getMessage());
            return false;
        }
    }

    /**
     * Realiza logout do usuário atual.
     */
    public void logout() {
        userSession.endSession();
    }

    /**
     * Verifica se há um usuário logado.
     * 
     * @return true se há usuário logado, false caso contrário
     */
    public boolean isLoggedIn() {
        return userSession.isLoggedIn();
    }

    /**
     * Obtém o usuário atual.
     * 
     * @return Usuário atual ou null se não há usuário logado
     */
    public Usuario getCurrentUser() {
        return userSession.getCurrentUser();
    }

    /**
     * Obtém o tipo do usuário atual.
     * 
     * @return Tipo do usuário atual ou null se não há usuário logado
     */
    public TipoUsuario getCurrentUserType() {
        return userSession.getUserType();
    }

    /**
     * Verifica se o usuário atual é um cliente.
     * 
     * @return true se for cliente, false caso contrário
     */
    public boolean isCurrentUserCliente() {
        return userSession.isCliente();
    }

    /**
     * Verifica se o usuário atual é um funcionário.
     * 
     * @return true se for funcionário, false caso contrário
     */
    public boolean isCurrentUserFuncionario() {
        return userSession.isFuncionario();
    }

    /**
     * Verifica se um email já está em uso.
     * 
     * @param email Email a ser verificado
     * @return true se o email já existe, false caso contrário
     */
    public boolean emailExists(String email) {
        try {
            return usuarioRepository.existsByEmail(email);
        } catch (SQLException e) {
            System.err.println("Erro ao verificar email: " + e.getMessage());
            return false;
        }
    }
}
