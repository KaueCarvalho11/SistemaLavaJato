package com.paintspray.repository;

import com.paintspray.model.Usuario;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Repository para operações com usuários (proprietário da oficina).
 */
public class UsuarioRepository extends BaseRepository<Usuario> {

    /**
     * Busca usuário por email.
     * 
     * @param email Email do usuário
     * @return Usuario encontrado ou null
     * @throws SQLException Se houver erro na operação
     */
    public Usuario findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE email = ?";
        return findOne(sql, this::mapResultSetToUsuario, email);
    }

    /**
     * Verifica se existe usuário com o email fornecido.
     * 
     * @param email Email a ser verificado
     * @return true se existe, false caso contrário
     * @throws SQLException Se houver erro na operação
     */
    public boolean existsByEmail(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE email = ?";
        return count(sql, email) > 0;
    }

    /**
     * Busca usuário por email e senha.
     * 
     * @param email Email do usuário
     * @param senha Senha do usuário
     * @return Usuario encontrado ou null
     * @throws SQLException Se houver erro na operação
     */
    public Usuario findByEmailAndPassword(String email, String senha) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE email = ? AND senha = ?";
        return findOne(sql, this::mapResultSetToUsuario, email, senha);
    }

    @Override
    public void save(Usuario entity) throws SQLException {
        String sql = "INSERT INTO usuarios (id, nome, email, senha) VALUES (?, ?, ?, ?)";
        executeUpdate(sql,
                entity.getId(),
                entity.getNome(),
                entity.getEmail(),
                entity.getSenha());
    }

    @Override
    public Usuario findById(String id) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        return findOne(sql, this::mapResultSetToUsuario, id);
    }

    @Override
    public void update(Usuario entity) throws SQLException {
        String sql = "UPDATE usuarios SET nome = ?, email = ?, senha = ? WHERE id = ?";
        executeUpdate(sql,
                entity.getNome(),
                entity.getEmail(),
                entity.getSenha(),
                entity.getId());
    }

    @Override
    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        executeUpdate(sql, id);
    }

    @Override
    public List<Usuario> findAll() throws SQLException {
        String sql = "SELECT * FROM usuarios ORDER BY nome";
        return findMany(sql, this::mapResultSetToUsuario);
    }

    /**
     * Mapeia ResultSet para Usuario.
     * 
     * @param rs ResultSet do banco
     * @return Usuario mapeado
     * @throws SQLException Se houver erro no mapeamento
     */
    private Usuario mapResultSetToUsuario(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        String nome = rs.getString("nome");
        String email = rs.getString("email");
        String senha = rs.getString("senha");

        return new Usuario(id, nome, email, senha);
    }
}