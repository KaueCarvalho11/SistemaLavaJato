package com.paintspray.service;

import com.paintspray.model.Usuario;
import com.paintspray.repository.UsuarioRepository;
import java.sql.SQLException;

/**
 * Service para lógica de negócio relacionada aos usuários (proprietário).
 */
public class UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioService() {
        this.repository = new UsuarioRepository();
    }

    /**
     * Realiza o login do usuário.
     */
    public Usuario realizarLogin(String email, String senha) throws SQLException {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email é obrigatório.");
        }
        if (senha == null || senha.trim().isEmpty()) {
            throw new IllegalArgumentException("Senha é obrigatória.");
        }

        Usuario usuario = repository.findByEmailAndPassword(email, senha);
        if (usuario == null) {
            throw new IllegalArgumentException("Email ou senha inválidos.");
        }

        return usuario;
    }

    /**
     * Cadastra um novo usuário.
     */
    public void cadastrarUsuario(String id, String nome, String email, String senha) throws SQLException {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID é obrigatório.");
        }
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório.");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email é obrigatório.");
        }
        if (senha == null || senha.trim().isEmpty()) {
            throw new IllegalArgumentException("Senha é obrigatória.");
        }

        if (repository.existsByEmail(email)) {
            throw new IllegalArgumentException("Já existe um usuário com este email.");
        }

        Usuario usuario = new Usuario(id, nome, email, senha);
        repository.save(usuario);
    }

    /**
     * Busca usuário por email.
     */
    public Usuario buscarPorEmail(String email) throws SQLException {
        return repository.findByEmail(email);
    }
}
