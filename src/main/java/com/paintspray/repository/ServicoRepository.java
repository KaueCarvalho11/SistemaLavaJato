package com.paintspray.repository;

import com.paintspray.model.Servico;
import com.paintspray.model.Veiculo;
import com.paintspray.model.Usuario;
import com.paintspray.enums.StatusServico;
import com.paintspray.enums.TipoServico;
import com.paintspray.enums.FormaPagamento;
import java.sql.SQLException;
import java.util.List;

/**
 * Repository para operações CRUD da entidade Servico.
 */
public class ServicoRepository extends BaseRepository<Servico> {

	@Override
	public void save(Servico servico) throws SQLException {
		String sql = "INSERT INTO servicos (tipo, descricao, preco, status, forma_pagamento, " +
				"id_veiculo, id_usuario) " +
				"VALUES (?, ?, ?, ?, ?, ?, ?)";

		executeUpdate(sql,
				servico.getTipo() != null ? servico.getTipo().name() : null,
				servico.getDescricao(),
				servico.getPreco(),
				servico.getStatus() != null ? servico.getStatus().name() : StatusServico.PENDENTE.name(),
				servico.getFormaPagamento() != null ? servico.getFormaPagamento().name() : null,
				servico.getVeiculo() != null ? servico.getVeiculo().getId() : null,
				servico.getUsuario() != null ? servico.getUsuario().getId() : null);
	}

	@Override
	public Servico findById(String id) throws SQLException {
		String sql = "SELECT s.*, " +
				"v.id AS veiculo_id, " +
				"v.modelo AS veiculo_modelo, " +
				"v.cor AS veiculo_cor, " +
				"v.ano_fabricacao AS veiculo_ano, " +
				"v.id_cliente AS veiculo_id_cliente, " +
				"u.id as usuario_id, " +
				"u.nome as usuario_nome, " +
				"u.email as usuario_email " +
				"FROM servicos s " +
				"LEFT JOIN veiculos v ON s.id_veiculo = v.id " +
				"LEFT JOIN usuarios u ON s.id_usuario = u.id " +
				"WHERE s.id_servico = ?";

		return findOne(sql, this::mapResultSetToServico, Integer.parseInt(id));
	}

	@Override
	public void update(Servico servico) throws SQLException {
		String sql = "UPDATE servicos SET tipo = ?, descricao = ?, preco = ?, status = ?, " +
				"forma_pagamento = ?, id_veiculo = ?, id_usuario = ? WHERE id_servico = ?";

		executeUpdate(sql,
				servico.getTipo() != null ? servico.getTipo().name() : null,
				servico.getDescricao(),
				servico.getPreco(),
				servico.getStatus() != null ? servico.getStatus().name() : null,
				servico.getFormaPagamento() != null ? servico.getFormaPagamento().name() : null,
				servico.getVeiculo() != null ? servico.getVeiculo().getId() : null,
				servico.getUsuario() != null ? servico.getUsuario().getId() : null,
				servico.getIdServico());
	}

	@Override
	public void delete(String id) throws SQLException {
		String sql = "DELETE FROM servicos WHERE id_servico = ?";
		executeUpdate(sql, Integer.parseInt(id));
	}

	@Override
	public List<Servico> findAll() throws SQLException {
		String sql = "SELECT s.*, " +
				"v.id AS veiculo_id, " +
				"v.modelo AS veiculo_modelo, " +
				"v.cor AS veiculo_cor, " +
				"v.ano_fabricacao AS veiculo_ano, " +
				"v.id_cliente AS veiculo_id_cliente, " +
				"u.id as usuario_id, " +
				"u.nome as usuario_nome, " +
				"u.email as usuario_email " +
				"FROM servicos s " +
				"LEFT JOIN veiculos v ON s.id_veiculo = v.id " +
				"LEFT JOIN usuarios u ON s.id_usuario = u.id " +
				"ORDER BY s.id_servico DESC";

		return findMany(sql, this::mapResultSetToServico);
	}

	/**
	 * Busca serviços por status.
	 */
	public List<Servico> findByStatus(StatusServico status) throws SQLException {
		String sql = "SELECT s.*, " +
				"v.id AS veiculo_id, " +
				"v.modelo AS veiculo_modelo, " +
				"v.cor AS veiculo_cor, " +
				"v.ano_fabricacao AS veiculo_ano, " +
				"v.id_cliente AS veiculo_id_cliente, " +
				"u.id as usuario_id, " +
				"u.nome as usuario_nome, " +
				"u.email as usuario_email " +
				"FROM servicos s " +
				"LEFT JOIN veiculos v ON s.id_veiculo = v.id " +
				"LEFT JOIN usuarios u ON s.id_usuario = u.id " +
				"WHERE s.status = ? " +
				"ORDER BY s.id_servico DESC";

		return findMany(sql, this::mapResultSetToServico, status.name());
	}

	/**
	 * Atualiza o status de um serviço.
	 */
	public void updateStatus(int servicoId, StatusServico novoStatus) throws SQLException {
		String sql = "UPDATE servicos SET status = ? WHERE id_servico = ?";
		executeUpdate(sql, novoStatus.name(), servicoId);
	}

	/**
	 * Cancela um serviço.
	 */
	public void cancelarServico(int servicoId) throws SQLException {
		String sql = "UPDATE servicos SET status = ? WHERE id_servico = ?";
		executeUpdate(sql, StatusServico.CANCELADO.name(), servicoId);
	}

	/**
	 * Conta serviços por status.
	 */
	public int countByStatus(StatusServico status) throws SQLException {
		String sql = "SELECT COUNT(*) FROM servicos WHERE status = ?";
		return count(sql, status.name());
	}

	/**
	 * Busca serviços por veículo.
	 */
	public List<Servico> findByVeiculo(int veiculoId) throws SQLException {
		String sql = "SELECT s.*, " +
				"v.id AS veiculo_id, " +
				"v.modelo AS veiculo_modelo, " +
				"v.cor AS veiculo_cor, " +
				"v.ano_fabricacao AS veiculo_ano, " +
				"v.id_cliente AS veiculo_id_cliente, " +
				"u.id as usuario_id, " +
				"u.nome as usuario_nome, " +
				"u.email as usuario_email " +
				"FROM servicos s " +
				"LEFT JOIN veiculos v ON s.id_veiculo = v.id " +
				"LEFT JOIN usuarios u ON s.id_usuario = u.id " +
				"WHERE s.id_veiculo = ? " +
				"ORDER BY s.id_servico DESC";

		return findMany(sql, this::mapResultSetToServico, veiculoId);
	}

	private Servico mapResultSetToServico(java.sql.ResultSet rs) throws SQLException {
		Veiculo veiculo = null;
		if (rs.getObject("veiculo_id") != null) {
			veiculo = new Veiculo(
					rs.getInt("veiculo_id"),
					rs.getString("veiculo_modelo"),
					rs.getString("veiculo_cor"),
					rs.getInt("veiculo_ano"),
					rs.getString("veiculo_id_cliente"));
		}

		Usuario usuario = null;
		if (rs.getString("usuario_id") != null) {
			usuario = new Usuario(
					rs.getString("usuario_id"),
					rs.getString("usuario_nome"),
					rs.getString("usuario_email"),
					"" // senha não deve estar na consulta
			);
		}

		// Converte strings do banco para ENUMs
		TipoServico tipo = rs.getString("tipo") != null ? TipoServico.valueOf(rs.getString("tipo")) : null;
		StatusServico status = rs.getString("status") != null ? StatusServico.valueOf(rs.getString("status")) : null;
		FormaPagamento formaPagamento = rs.getString("forma_pagamento") != null
				? FormaPagamento.valueOf(rs.getString("forma_pagamento"))
				: null;

		// Cria o serviço
		Servico servico = new Servico(
				rs.getInt("id_servico"),
				tipo,
				rs.getString("descricao"),
				rs.getDouble("preco"),
				status,
				formaPagamento,
				veiculo,
				usuario);

		return servico;
	}

	/**
	 * Busca todos os serviços associados aos veículos de um cliente específico.
	 */
	public List<Servico> findByClienteId(String clienteId) throws SQLException {
		String sql = "SELECT s.*, " +
				"v.id AS veiculo_id, " +
				"v.modelo AS veiculo_modelo, " +
				"v.cor AS veiculo_cor, " +
				"v.ano_fabricacao AS veiculo_ano, " +
				"v.id_cliente AS veiculo_id_cliente, " +
				"u.id as usuario_id, " +
				"u.nome as usuario_nome, " +
				"u.email as usuario_email " +
				"FROM servicos s " +
				"LEFT JOIN veiculos v ON s.id_veiculo = v.id " +
				"LEFT JOIN usuarios u ON s.id_usuario = u.id " +
				"WHERE v.id_cliente = ? " +
				"ORDER BY s.id_servico DESC";
		return findMany(sql, this::mapResultSetToServico, clienteId);
	}
}
