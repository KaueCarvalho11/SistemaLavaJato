package com.paintspray.repository;

import com.paintspray.model.Cliente;

import java.sql.Connection;
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
     * Salva um novo cliente diretamente na tabela 'clientes'.
     */
    @Override
    public void save(Cliente cliente) throws SQLException {
       String sqlCliente = "INSERT INTO clientes (id_usuario, endereco, numero_telefone) VALUES (?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(sqlCliente)) {

            stmt.setString(1, cliente.getId());
            stmt.setString(2, cliente.getNome()); // Adicionado: Nome agora é salvo aqui
            stmt.setString(3, cliente.getEndereco());
            stmt.setString(4, cliente.getNumeroTelefone());
            
            stmt.executeUpdate();
        }
    }

    /**
     * Atualiza os dados de um cliente existente.
     */
    @Override
    public void update(Cliente cliente) throws SQLException {
        String sqlCliente = "UPDATE clientes SET nome = ?, endereco = ?, numero_telefone = ? WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(sqlCliente)) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getEndereco());
            stmt.setString(3, cliente.getNumeroTelefone());
            stmt.setString(4, cliente.getId());

            stmt.executeUpdate();
        }
    }

    /**
     * Deleta um cliente do banco de dados.
     */
    @Override
    public void delete(String id) throws SQLException {
        String sqlCliente = "DELETE FROM clientes WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(sqlCliente)) {
            
            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }

    /**
     * Busca um cliente pelo seu ID, juntando dados das duas tabelas.
     */
    @Override
    public Cliente findById(String id) throws SQLException {
        String sql = "SELECT * FROM clientes WHERE id = ?";
        return findOne(sql, this::mapResultSetToCliente, id);
    }

    /**
     * Busca todos os clientes cadastrados.
     */
    @Override
    public List<Cliente> findAll() throws SQLException {
        String sql = "SELECT * FROM clientes ORDER BY nome";
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
            rs.getString("endereco"),
            rs.getString("numero_telefone")
        );
    }
}