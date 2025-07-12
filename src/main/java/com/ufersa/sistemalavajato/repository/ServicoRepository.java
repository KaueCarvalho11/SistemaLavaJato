package com.ufersa.sistemalavajato.repository;

import com.ufersa.sistemalavajato.model.Servico;
import com.ufersa.sistemalavajato.model.Veiculo;
import com.ufersa.sistemalavajato.model.Funcionario;
import java.sql.SQLException;
import java.util.List;

/**
 * Repository para operações CRUD da entidade Servico.
 */
public class ServicoRepository extends BaseRepository<Servico> {

    @Override
    public void save(Servico servico) throws SQLException {
        String sql = "INSERT INTO servicos (tipo, descricao, preco, status, forma_pagamento, " +
                "num_chassi, id_funcionario, data_inicio, data_conclusao) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        executeUpdate(sql,
                servico.getTipo(),
                servico.getDescricao(),
                servico.getPreco(),
                servico.getStatus(),
                servico.getFormaPagamento(),
                servico.getVeiculo() != null ? servico.getVeiculo().getNumChassi() : null,
                servico.getFuncionario() != null ? servico.getFuncionario().getId() : null,
                null, // data_inicio será definida quando iniciar o serviço
                null // data_conclusao será definida quando concluir o serviço
        );
    }

    @Override
    public Servico findById(String id) throws SQLException {
        String sql = "SELECT s.*, v.num_chassi AS veiculo_num_chassi, v.modelo, v.cor, " +
                "f.id_usuario as funcionario_id, u.nome as funcionario_nome " +
                "FROM servicos s " +
                "LEFT JOIN veiculos v ON s.num_chassi = v.num_chassi " +
                "LEFT JOIN funcionarios f ON s.id_funcionario = f.id_usuario " +
                "LEFT JOIN usuarios u ON f.id_usuario = u.id " +
                "WHERE s.id_servico = ?";

        return findOne(sql, this::mapResultSetToServico, Integer.parseInt(id));
    }

    @Override
    public void update(Servico servico) throws SQLException {
        String sql = "UPDATE servicos SET tipo = ?, descricao = ?, preco = ?, status = ?, " +
                "forma_pagamento = ?, num_chassi = ?, id_funcionario = ?, " +
                "data_inicio = ?, data_conclusao = ? WHERE id_servico = ?";

        executeUpdate(sql,
                servico.getTipo(),
                servico.getDescricao(),
                servico.getPreco(),
                servico.getStatus(),
                servico.getFormaPagamento(),
                servico.getVeiculo() != null ? servico.getVeiculo().getNumChassi() : null,
                servico.getFuncionario() != null ? servico.getFuncionario().getId() : null,
                null, // data_inicio - implementar quando necessário
                null, // data_conclusao - implementar quando necessário
                servico.getIdServico());
    }

    @Override
    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM servicos WHERE id_servico = ?";
        executeUpdate(sql, Integer.parseInt(id));
    }

    @Override
    public List<Servico> findAll() throws SQLException {
        String sql = "SELECT s.*, v.num_chassi AS veiculo_num_chassi, v.modelo, v.cor, " +
                "f.id_usuario as funcionario_id, u.nome as funcionario_nome " +
                "FROM servicos s " +
                "LEFT JOIN veiculos v ON s.num_chassi = v.num_chassi " +
                "LEFT JOIN funcionarios f ON s.id_funcionario = f.id_usuario " +
                "LEFT JOIN usuarios u ON f.id_usuario = u.id " +
                "ORDER BY s.data_criacao DESC";

        return findMany(sql, this::mapResultSetToServico);
    }

    /**
     * Busca serviços por status.
     */
    public List<Servico> findByStatus(String status) throws SQLException {
        String sql = "SELECT s.*, v.num_chassi AS veiculo_num_chassi, v.modelo, v.cor, " +
                "f.id_usuario as funcionario_id, u.nome as funcionario_nome " +
                "FROM servicos s " +
                "LEFT JOIN veiculos v ON s.num_chassi = v.num_chassi " +
                "LEFT JOIN funcionarios f ON s.id_funcionario = f.id_usuario " +
                "LEFT JOIN usuarios u ON f.id_usuario = u.id " +
                "WHERE s.status = ? " +
                "ORDER BY s.data_criacao DESC";

        return findMany(sql, this::mapResultSetToServico, status);
    }

    /**
     * Busca serviços por tipo.
     */
    public List<Servico> findByTipo(String tipo) throws SQLException {
        String sql = "SELECT s.*, v.num_chassi AS veiculo_num_chassi, v.modelo, v.cor, " +
                "f.id_usuario as funcionario_id, u.nome as funcionario_nome " +
                "FROM servicos s " +
                "LEFT JOIN veiculos v ON s.num_chassi = v.num_chassi " +
                "LEFT JOIN funcionarios f ON s.id_funcionario = f.id_usuario " +
                "LEFT JOIN usuarios u ON f.id_usuario = u.id " +
                "WHERE s.tipo = ? " +
                "ORDER BY s.data_criacao DESC";

        return findMany(sql, this::mapResultSetToServico, tipo);
    }

    /**
     * Busca serviços por veículo.
     */
    public List<Servico> findByVeiculo(int numChassi) throws SQLException {
        String sql = "SELECT s.*, v.num_chassi AS veiculo_num_chassi, v.modelo, v.cor, " +
                "f.id_usuario as funcionario_id, u.nome as funcionario_nome " +
                "FROM servicos s " +
                "LEFT JOIN veiculos v ON s.num_chassi = v.num_chassi " +
                "LEFT JOIN funcionarios f ON s.id_funcionario = f.id_usuario " +
                "LEFT JOIN usuarios u ON f.id_usuario = u.id " +
                "WHERE s.num_chassi = ? " +
                "ORDER BY s.data_criacao DESC";

        return findMany(sql, this::mapResultSetToServico, numChassi);
    }

    /**
     * Busca serviços por funcionário.
     */
    public List<Servico> findByFuncionario(String funcionarioId) throws SQLException {
        String sql = "SELECT s.*, v.num_chassi AS veiculo_num_chassi, v.cor, " +
                "f.id_usuario as funcionario_id, u.nome as funcionario_nome " +
                "FROM servicos s " +
                "LEFT JOIN veiculos v ON s.num_chassi = v.num_chassi " +
                "LEFT JOIN funcionarios f ON s.id_funcionario = f.id_usuario " +
                "LEFT JOIN usuarios u ON f.id_usuario = u.id " +
                "WHERE s.id_funcionario = ? " +
                "ORDER BY s.data_criacao DESC";

        return findMany(sql, this::mapResultSetToServico, funcionarioId);
    }

    /**
     * Atualiza o status de um serviço.
     */
    public void updateStatus(int servicoId, String novoStatus) throws SQLException {
        String sql = "UPDATE servicos SET status = ? WHERE id_servico = ?";
        executeUpdate(sql, novoStatus, servicoId);
    }

    /**
     * Inicia um serviço (atualiza status e data_inicio).
     */
    public void iniciarServico(int servicoId) throws SQLException {
        String sql = "UPDATE servicos SET status = 'EM_ANDAMENTO', data_inicio = CURRENT_TIMESTAMP WHERE id_servico = ?";
        executeUpdate(sql, servicoId);
    }

    /**
     * Conclui um serviço (atualiza status e data_conclusao).
     */
    public void concluirServico(int servicoId) throws SQLException {
        String sql = "UPDATE servicos SET status = 'CONCLUIDO', data_conclusao = CURRENT_TIMESTAMP WHERE id_servico = ?";
        executeUpdate(sql, servicoId);
    }

    /**
     * Cancela um serviço.
     */
    public void cancelarServico(int servicoId) throws SQLException {
        String sql = "UPDATE servicos SET status = 'CANCELADO' WHERE id_servico = ?";
        executeUpdate(sql, servicoId);
    }

    /**
     * Conta serviços por status.
     */
    public int countByStatus(String status) throws SQLException {
        String sql = "SELECT COUNT(*) FROM servicos WHERE status = ?";
        return count(sql, status);
    }

    /**
     * Verifica se um serviço existe.
     */
    public boolean existsById(int servicoId) throws SQLException {
        String sql = "SELECT 1 FROM servicos WHERE id_servico = ?";
        return exists(sql, servicoId);
    }

    /**
     * Mapeia ResultSet para objeto Servico.
     */
    private Servico mapResultSetToServico(java.sql.ResultSet rs) throws SQLException {
        Veiculo veiculo = null;
        if (rs.getObject("veiculo_num_chassi") != null) {
            veiculo = new Veiculo(
                    rs.getString("veiculo_id_cliente"),
                    rs.getString("veiculo_modelo"),
                    rs.getInt("veiculo_num_chassi"),
                    rs.getDouble("veiculo_quilometragem"),
                    rs.getDouble("veiculo_preco"),
                    rs.getString("veiculo_cor"),
                    rs.getInt("veiculo_ano"),
                    rs.getString("veiculo_status"));
        }

        Funcionario funcionario = null;
        if (rs.getString("funcionario_id") != null) {
            funcionario = new Funcionario(
                    rs.getString("funcionario_id"),
                    rs.getString("funcionario_nome"),
                    "", // email - não está na consulta
                    "" // senha - não deveria estar na consulta
            );
        }

        // Cria o serviço
        Servico servico = new Servico(
                rs.getInt("id_servico"),
                rs.getString("tipo"),
                veiculo,
                funcionario);

        // Define outros atributos
        servico.setDescricao(rs.getString("descricao"));
        servico.setPreco(rs.getDouble("preco"));
        servico.setStatus(rs.getString("status"));
        servico.setFormaPagamento(rs.getString("forma_pagamento"));

        return servico;
    }
}
