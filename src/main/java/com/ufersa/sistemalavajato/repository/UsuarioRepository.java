package com.ufersa.sistemalavajato.repository;

import com.ufersa.sistemalavajato.enums.TipoUsuario;
import com.ufersa.sistemalavajato.model.Cliente;
import com.ufersa.sistemalavajato.model.Funcionario;
import com.ufersa.sistemalavajato.model.Usuario;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Repository para operações com usuários.
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
     * Busca usuário por email e senha hash.
     * 
     * @param email        Email do usuário
     * @param passwordHash Hash da senha
     * @return Usuario encontrado ou null
     * @throws SQLException Se houver erro na operação
     */
    public Usuario findByEmailAndPassword(String email, String passwordHash) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE email = ? AND senha_hash = ?";
        return findOne(sql, this::mapResultSetToUsuario, email, passwordHash);
    }

    @Override
    public void save(Usuario entity) throws SQLException {
        String sql = "INSERT INTO usuarios (id, nome, email, senha, senha_hash, tipo_usuario, endereco, numero_telefone) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        TipoUsuario tipo = (entity instanceof Cliente) ? TipoUsuario.CLIENTE : TipoUsuario.FUNCIONARIO;
        String endereco = null;
        String telefone = null;

        if (entity instanceof Cliente) {
            Cliente cliente = (Cliente) entity;
            endereco = cliente.getEndereco();
            telefone = cliente.getNumeroTelefone();
        }

        // Usa a senha em hash como senha padrão se não houver senha definida
        String senha = entity.getSenha() != null ? entity.getSenha() : "***";

        executeUpdate(sql,
                entity.getId(),
                entity.getNome(),
                entity.getEmail(),
                senha,
                entity.getSenhaHash(),
                tipo.name(),
                endereco,
                telefone);
    }

    @Override
    public Usuario findById(String id) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        return findOne(sql, this::mapResultSetToUsuario, id);
    }

    @Override
    public void update(Usuario entity) throws SQLException {
        String sql = "UPDATE usuarios SET nome = ?, email = ?, senha_hash = ?, endereco = ?, numero_telefone = ? WHERE id = ?";

        String endereco = null;
        String telefone = null;

        if (entity instanceof Cliente) {
            Cliente cliente = (Cliente) entity;
            endereco = cliente.getEndereco();
            telefone = cliente.getNumeroTelefone();
        }

        executeUpdate(sql,
                entity.getNome(),
                entity.getEmail(),
                entity.getSenhaHash(),
                endereco,
                telefone,
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
     * Busca todos os clientes.
     * 
     * @return Lista de clientes
     * @throws SQLException Se houver erro na operação
     */
    public List<Usuario> findAllClientes() throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE tipo_usuario = ? ORDER BY nome";
        return findMany(sql, this::mapResultSetToUsuario, TipoUsuario.CLIENTE.name());
    }

    /**
     * Busca todos os funcionários.
     * 
     * @return Lista de funcionários
     * @throws SQLException Se houver erro na operação
     */
    public List<Usuario> findAllFuncionarios() throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE tipo_usuario = ? ORDER BY nome";
        return findMany(sql, this::mapResultSetToUsuario, TipoUsuario.FUNCIONARIO.name());
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
        String senhaHash = rs.getString("senha_hash");
        TipoUsuario tipo = TipoUsuario.fromString(rs.getString("tipo_usuario"));
        String endereco = rs.getString("endereco");
        String telefone = rs.getString("numero_telefone");

        Usuario usuario;

        if (tipo == TipoUsuario.CLIENTE) {
            usuario = new Cliente(id, nome, email, null, endereco, telefone);
        } else {
            usuario = new Funcionario(id, nome, email, null);
        }

        usuario.setSenhaHash(senhaHash);
        return usuario;
    }
}