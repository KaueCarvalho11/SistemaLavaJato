package com.paintspray.repository;

import com.paintspray.model.Cliente;
import com.paintspray.util.PasswordUtils;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Camada de Acesso a Dados (DAO/Repository) para a entidade Cliente.
 * Centraliza toda a lógica de interação com o banco de dados para clientes,
 * lidando com as tabelas 'usuarios' e 'clientes'.
 */
public class ClienteRepository extends BaseRepository<Cliente> {

    /**
     * Salva um novo cliente no banco de dados.
     * Executa inserções nas tabelas 'usuarios' e 'clientes' dentro de uma transação
     * para garantir a consistência dos dados (ou tudo funciona, ou nada é salvo).
     */
    @Override
    public void save(Cliente cliente) throws SQLException {
        String sqlUsuario = "INSERT INTO usuarios (id, nome, email, senha, senha_hash, tipo_usuario) VALUES (?, ?, ?, ?, ?, 'CLIENTE')";
        String sqlCliente = "INSERT INTO clientes (id_usuario, endereco, numero_telefone) VALUES (?, ?, ?)";

        executeTransaction(connection -> {
            // Primeiro, insere na tabela 'usuarios'
            try (PreparedStatement stmtUsuario = connection.prepareStatement(sqlUsuario)) {
                stmtUsuario.setString(1, cliente.getId());
                stmtUsuario.setString(2, cliente.getNome());
                stmtUsuario.setString(3, cliente.getEmail());
                stmtUsuario.setString(4, cliente.getSenha());
                stmtUsuario.setString(5, PasswordUtils.hashPassword(cliente.getSenha()));
                stmtUsuario.executeUpdate();
            }
            // Depois, insere na tabela 'clientes', ligando pelo mesmo ID
            try (PreparedStatement stmtCliente = connection.prepareStatement(sqlCliente)) {
                stmtCliente.setString(1, cliente.getId());
                stmtCliente.setString(2, cliente.getEndereco());
                stmtCliente.setString(3, cliente.getNumeroTelefone());
                stmtCliente.executeUpdate();
            }
        });
    }

    /**
     * Atualiza os dados de um cliente existente.
     * Também utiliza uma transação para atualizar as tabelas 'usuarios' e
     * 'clientes' de forma atômica.
     */
    @Override
    public void update(Cliente cliente) throws SQLException {
        String sqlUsuario = "UPDATE usuarios SET nome = ?, email = ?, senha = ? WHERE id = ?";
        String sqlCliente = "UPDATE clientes SET endereco = ?, numero_telefone = ? WHERE id_usuario = ?";

        executeTransaction(connection -> {
            // Atualiza os dados na tabela 'usuarios'
            try (PreparedStatement stmtUsuario = connection.prepareStatement(sqlUsuario)) {
                stmtUsuario.setString(1, cliente.getNome());
                stmtUsuario.setString(2, cliente.getEmail());
                stmtUsuario.setString(3, cliente.getSenha());
                stmtUsuario.setString(4, cliente.getId());
                stmtUsuario.executeUpdate();
            }
            // Atualiza os dados na tabela 'clientes'
            try (PreparedStatement stmtCliente = connection.prepareStatement(sqlCliente)) {
                stmtCliente.setString(1, cliente.getEndereco());
                stmtCliente.setString(2, cliente.getNumeroTelefone());
                stmtCliente.setString(3, cliente.getId());
                stmtCliente.executeUpdate();
            }
        });
    }

    /**
     * Deleta um cliente do banco de dados.
     * A operação assume que a tabela 'clientes' tem uma Foreign Key com "ON DELETE
     * CASCADE",
     * o que significa que ao deletar o usuário, o registro correspondente em
     * 'clientes' é apagado automaticamente.
     */
    @Override
    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        executeUpdate(sql, id);
    }

    /**
     * Busca um cliente pelo seu ID, juntando dados das duas tabelas.
     */
    @Override
    public Cliente findById(String id) throws SQLException {
        String sql = "SELECT u.*, c.endereco, c.numero_telefone FROM usuarios u " +
                "JOIN clientes c ON u.id = c.id_usuario " +
                "WHERE u.id = ?";
        return findOne(sql, this::mapResultSetToCliente, id);
    }

    /**
     * Busca um cliente pelo seu email, juntando dados das duas tabelas.
     */
    public Cliente findByEmail(String email) throws SQLException {
        String sql = "SELECT u.*, c.endereco, c.numero_telefone FROM usuarios u " +
                "JOIN clientes c ON u.id = c.id_usuario " +
                "WHERE u.email = ?";
        return findOne(sql, this::mapResultSetToCliente, email);
    }

    /**
     * Busca todos os clientes cadastrados.
     */
    @Override
    public List<Cliente> findAll() throws SQLException {
        String sql = "SELECT u.*, c.endereco, c.numero_telefone FROM usuarios u " +
                "JOIN clientes c ON u.id = c.id_usuario " +
                "ORDER BY u.nome";
        return findMany(sql, this::mapResultSetToCliente);
    }

    /**
     * Método auxiliar privado para "traduzir" uma linha do resultado da consulta
     * (ResultSet)
     * em um objeto Cliente completo.
     */
    private Cliente mapResultSetToCliente(ResultSet rs) throws SQLException {
        return new Cliente(
                rs.getString("id"),
                rs.getString("nome"),
                rs.getString("email"),
                null, // Senha não é carregada para o objeto por segurança
                rs.getString("endereco"),
                rs.getString("numero_telefone"));
    }
}