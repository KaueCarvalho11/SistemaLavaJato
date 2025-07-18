package com.ufersa.sistemalavajato.repository;

import com.ufersa.sistemalavajato.model.Veiculo;
import java.sql.SQLException;
import java.util.List;

/**
 * Repository para operações CRUD da entidade Veiculo.
 */
public class VeiculoRepository extends BaseRepository<Veiculo> {

    @Override
    public void save(Veiculo veiculo) throws SQLException {
        String sql = "INSERT INTO veiculos (num_chassi, id_cliente, modelo, quilometragem, preco, cor, ano_fabricacao, status) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        executeUpdate(sql,
                veiculo.getNumChassi(),
                veiculo.getIdCliente(),
                veiculo.getModelo(),
                veiculo.getQuilometragem(),
                veiculo.getPreco(),
                veiculo.getCor(),
                veiculo.getAnoFabricacao(),
                veiculo.getStatus());
    }

    @Override
    public Veiculo findById(String id) throws SQLException {
        int numChassi = Integer.parseInt(id);
        return findByNumChassi(numChassi);
    }

    public Veiculo findByNumChassi(int numChassi) throws SQLException {
        String sql = "SELECT * FROM veiculos WHERE num_chassi = ?";
        return findOne(sql, this::mapResultSetToVeiculo, numChassi);
    }

    @Override
    public void update(Veiculo veiculo) throws SQLException {
        String sql = "UPDATE veiculos SET id_cliente = ?, modelo = ?, quilometragem = ?, preco = ?, cor = ?, ano_fabricacao = ?, status = ? "
                +
                "WHERE num_chassi = ?";
        executeUpdate(sql,
                veiculo.getIdCliente(),
                veiculo.getModelo(),
                veiculo.getQuilometragem(),
                veiculo.getPreco(),
                veiculo.getCor(),
                veiculo.getAnoFabricacao(),
                veiculo.getStatus(),
                veiculo.getNumChassi());
    }

    @Override
    public void delete(String numChassi) throws SQLException {
        String sql = "DELETE FROM veiculos WHERE num_chassi = ?";
        executeUpdate(sql, Integer.parseInt(numChassi));
    }

    @Override
    public List<Veiculo> findAll() throws SQLException {
        String sql = "SELECT * FROM veiculos ORDER BY data_criacao DESC";
        return findMany(sql, this::mapResultSetToVeiculo);
    }

    private Veiculo mapResultSetToVeiculo(java.sql.ResultSet rs) throws SQLException {
        return new Veiculo(
                rs.getString("id_cliente"),
                rs.getString("modelo"),
                rs.getInt("num_chassi"),
                rs.getDouble("quilometragem"),
                rs.getDouble("preco"),
                rs.getString("cor"),
                rs.getInt("ano_fabricacao"),
                rs.getString("status"));
    }
    
// Adicione este método dentro da classe VeiculoRepository.java

/**
 * Conta quantos veículos um cliente específico possui.
 * @param clienteId O ID do cliente.
 * @return O número de veículos do cliente.
 * @throws SQLException
 */
public int countByClienteId(String clienteId) throws SQLException {
    String sql = "SELECT COUNT(*) FROM veiculos WHERE id_cliente = ?";
    // Supondo que sua BaseRepository tenha um método 'count' que retorna um int
    return count(sql, clienteId);
}

// Adicione este método à sua classe VeiculoRepository

public List<Veiculo> findByClienteId(String clienteId) throws SQLException {
    String sql = "SELECT * FROM veiculos WHERE id_cliente = ? ORDER BY modelo";
    return findMany(sql, this::mapResultSetToVeiculo, clienteId);
}

}


