package com.ufersa.sistemalavajato.repository;

import com.ufersa.sistemalavajato.config.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe base para todos os repositórios.
 * Fornece funcionalidades comuns para operações CRUD.
 * 
 * @param <T> Tipo da entidade que o repositório manipula
 */
public abstract class BaseRepository<T> {

    protected DatabaseConnection databaseConnection;

    protected BaseRepository() {
        this.databaseConnection = DatabaseConnection.getInstance();
    }

    /**
     * Obtém uma conexão com o banco de dados.
     * 
     * @return Conexão com o banco
     * @throws SQLException Se houver erro na conexão
     */
    protected Connection getConnection() throws SQLException {
        return databaseConnection.getConnection();
    }

    /**
     * Executa uma query que não retorna resultados (INSERT, UPDATE, DELETE).
     * 
     * @param sql        Query SQL a ser executada
     * @param parameters Parâmetros da query
     * @return Número de linhas afetadas
     * @throws SQLException Se houver erro na execução
     */
    protected int executeUpdate(String sql, Object... parameters) throws SQLException {
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            setParameters(statement, parameters);
            return statement.executeUpdate();
        }
    }

    /**
     * Executa uma query que retorna um único resultado.
     * 
     * @param sql        Query SQL a ser executada
     * @param mapper     Função para mapear o ResultSet para o objeto
     * @param parameters Parâmetros da query
     * @return Objeto encontrado ou null se não encontrado
     * @throws SQLException Se houver erro na execução
     */
    protected T findOne(String sql, ResultSetMapper<T> mapper, Object... parameters) throws SQLException {
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            setParameters(statement, parameters);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapper.map(resultSet);
                }
                return null;
            }
        }
    }

    /**
     * Executa uma query que retorna múltiplos resultados.
     * 
     * @param sql        Query SQL a ser executada
     * @param mapper     Função para mapear o ResultSet para o objeto
     * @param parameters Parâmetros da query
     * @return Lista de objetos encontrados
     * @throws SQLException Se houver erro na execução
     */
    protected List<T> findMany(String sql, ResultSetMapper<T> mapper, Object... parameters) throws SQLException {
        List<T> results = new ArrayList<>();

        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            setParameters(statement, parameters);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    results.add(mapper.map(resultSet));
                }
            }
        }

        return results;
    }

    /**
     * Executa uma transação com múltiplas operações.
     * 
     * @param transaction Operações a serem executadas na transação
     * @throws SQLException Se houver erro na execução
     */
    protected void executeTransaction(DatabaseTransaction transaction) throws SQLException {
        Connection connection = null;
        try {
            connection = getConnection();
            connection.setAutoCommit(false);

            transaction.execute(connection);

            connection.commit();
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    e.addSuppressed(rollbackEx);
                }
            }
            throw e;
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    // Log do erro, mas não lance exceção
                    System.err.println("Erro ao fechar conexão: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Verifica se um registro existe baseado em uma condição.
     * 
     * @param sql        Query SQL de verificação
     * @param parameters Parâmetros da query
     * @return true se existe, false caso contrário
     * @throws SQLException Se houver erro na execução
     */
    protected boolean exists(String sql, Object... parameters) throws SQLException {
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            setParameters(statement, parameters);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    /**
     * Conta o número de registros baseado em uma condição.
     * 
     * @param sql        Query SQL de contagem
     * @param parameters Parâmetros da query
     * @return Número de registros
     * @throws SQLException Se houver erro na execução
     */
    protected int count(String sql, Object... parameters) throws SQLException {
        try (Connection connection = getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            setParameters(statement, parameters);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
                return 0;
            }
        }
    }

    /**
     * Define os parâmetros do PreparedStatement.
     * 
     * @param statement  PreparedStatement a ser configurado
     * @param parameters Parâmetros a serem definidos
     * @throws SQLException Se houver erro na configuração
     */
    private void setParameters(PreparedStatement statement, Object... parameters) throws SQLException {
        for (int i = 0; i < parameters.length; i++) {
            statement.setObject(i + 1, parameters[i]);
        }
    }

    /**
     * Interface funcional para mapear ResultSet para objeto.
     * 
     * @param <T> Tipo do objeto
     */
    @FunctionalInterface
    protected interface ResultSetMapper<T> {
        T map(ResultSet resultSet) throws SQLException;
    }

    /**
     * Interface funcional para operações de transação.
     */
    @FunctionalInterface
    protected interface DatabaseTransaction {
        void execute(Connection connection) throws SQLException;
    }

    // Métodos abstratos que devem ser implementados pelas classes filhas

    /**
     * Salva uma nova entidade no banco.
     * 
     * @param entity Entidade a ser salva
     * @throws SQLException Se houver erro na operação
     */
    public abstract void save(T entity) throws SQLException;

    /**
     * Busca uma entidade por ID.
     * 
     * @param id ID da entidade
     * @return Entidade encontrada ou null
     * @throws SQLException Se houver erro na operação
     */
    public abstract T findById(String id) throws SQLException;

    /**
     * Atualiza uma entidade existente.
     * 
     * @param entity Entidade a ser atualizada
     * @throws SQLException Se houver erro na operação
     */
    public abstract void update(T entity) throws SQLException;

    /**
     * Remove uma entidade por ID.
     * 
     * @param id ID da entidade a ser removida
     * @throws SQLException Se houver erro na operação
     */
    public abstract void delete(String id) throws SQLException;

    /**
     * Lista todas as entidades.
     * 
     * @return Lista de todas as entidades
     * @throws SQLException Se houver erro na operação
     */
    public abstract List<T> findAll() throws SQLException;
}
