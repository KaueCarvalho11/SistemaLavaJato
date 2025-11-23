package com.paintspray.repository;

import com.paintspray.model.Veiculo;
import java.sql.SQLException;
import java.util.List;

/**
 * Repository para operações CRUD da entidade Veiculo.
 */
public class VeiculoRepository extends BaseRepository<Veiculo> {

    @Override
    public void save(Veiculo veiculo) throws SQLException {
        String sql = "INSERT INTO veiculos (modelo, cor, ano_fabricacao, id_cliente) "
                + "VALUES (?, ?, ?, ?)";
        executeUpdate(sql,
                veiculo.getModelo(),
                veiculo.getCor(),
                veiculo.getAnoFabricacao(),
                veiculo.getIdCliente());
    }

    @Override
    public Veiculo findById(String id) throws SQLException {
        int veiculoId = Integer.parseInt(id);
        String sql = "SELECT * FROM veiculos WHERE id = ?";
        return findOne(sql, this::mapResultSetToVeiculo, veiculoId);
    }

    @Override
    public void update(Veiculo veiculo) throws SQLException {
        String sql = "UPDATE veiculos SET modelo = ?, cor = ?, ano_fabricacao = ?, id_cliente = ? "
                + "WHERE id = ?";
        executeUpdate(sql,
                veiculo.getModelo(),
                veiculo.getCor(),
                veiculo.getAnoFabricacao(),
                veiculo.getIdCliente(),
                veiculo.getId());
    }

    @Override
    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM veiculos WHERE id = ?";
        executeUpdate(sql, Integer.parseInt(id));
    }

    @Override
    public List<Veiculo> findAll() throws SQLException {
        String sql = "SELECT * FROM veiculos ORDER BY id DESC";
        return findMany(sql, this::mapResultSetToVeiculo);
    }

    private Veiculo mapResultSetToVeiculo(java.sql.ResultSet rs) throws SQLException {
        return new Veiculo(
                rs.getInt("id"),
                rs.getString("modelo"),
                rs.getString("cor"),
                rs.getInt("ano_fabricacao"),
                rs.getString("id_cliente"));
    }

    /**
     * Conta quantos veículos um cliente específico possui.
     * 
     * @param clienteId O ID do cliente.
     * @return O número de veículos do cliente.
     * @throws SQLException
     */
    public int countByClienteId(String clienteId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM veiculos WHERE id_cliente = ?";
        return count(sql, clienteId);
    }

    /*
     * Método para buscar veículos associados a um cliente específico no banco de
     * dados.
     */
    public List<Veiculo> findByClienteId(String clienteId) throws SQLException {
        String sql = "SELECT * FROM veiculos WHERE id_cliente = ? ORDER BY modelo";
        return findMany(sql, this::mapResultSetToVeiculo, clienteId);
    }

}
