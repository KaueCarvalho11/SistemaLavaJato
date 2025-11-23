package com.paintspray.service;

import com.paintspray.model.Veiculo;
import com.paintspray.repository.VeiculoRepository;
import java.sql.SQLException;
import java.util.List;

/**
 * Service para lógica de negócio relacionada à entidade Veiculo.
 */
public class VeiculoService {

    private final VeiculoRepository repository;

    public VeiculoService() {
        this.repository = new VeiculoRepository();
    }

    /**
     * Cadastra um novo veículo no sistema.
     */
    public void cadastrarVeiculo(String idCliente, String modelo, String cor, int anoFabricacao) throws SQLException {
        // Validação do ID do cliente
        if (idCliente == null || idCliente.trim().isEmpty())
            throw new IllegalArgumentException("ID do cliente é obrigatório.");

        // Validação do modelo
        if (modelo == null || modelo.trim().isEmpty())
            throw new IllegalArgumentException("Modelo do veículo não pode ser vazio.");

        if (!modelo.matches("^[A-Za-zÀ-ÖØ-öø-ÿ0-9 ]+$"))
            throw new IllegalArgumentException("Modelo só pode conter letras, números e espaços.");

        if (modelo.contains("  "))
            throw new IllegalArgumentException("Modelo não pode conter espaços duplos.");

        // Validação da cor
        if (cor == null || cor.trim().isEmpty())
            throw new IllegalArgumentException("Cor do veículo não pode ser vazia.");

        if (!cor.matches("^[A-Za-zÀ-ÖØ-öø-ÿ ]+$"))
            throw new IllegalArgumentException("Cor só pode conter letras e espaços.");

        if (cor.contains("  "))
            throw new IllegalArgumentException("Cor não pode conter espaços duplos.");

        // Validação do ano de fabricação
        int anoAtual = java.time.Year.now().getValue();
        if (anoFabricacao < 1900 || anoFabricacao > anoAtual)
            throw new IllegalArgumentException("Ano de fabricação inválido.");

        // Criação do objeto Veiculo após validações
        Veiculo veiculo = new Veiculo(modelo, cor, anoFabricacao, idCliente);
        repository.save(veiculo);
    }

    /**
     * Cadastra um novo veículo (versão sobrecarregada que aceita objeto Veiculo).
     */
    public void cadastrarVeiculo(Veiculo veiculo) throws SQLException {
        cadastrarVeiculo(veiculo.getIdCliente(), veiculo.getModelo(),
                veiculo.getCor(), veiculo.getAnoFabricacao());
    }

    /**
     * Busca um veículo pelo ID.
     */
    public Veiculo buscarVeiculoPorId(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("ID do veículo inválido.");
        }
        Veiculo v = repository.findById(String.valueOf(id));
        if (v == null) {
            throw new IllegalArgumentException("Veículo com ID " + id + " não encontrado.");
        }
        return v;
    }

    /**
     * Atualiza os dados de um veículo existente.
     */
    public void atualizarVeiculo(int id, String modelo, String cor, int anoFabricacao) throws SQLException {
        // Busca o veículo existente
        Veiculo existente = repository.findById(String.valueOf(id));
        if (existente == null) {
            throw new IllegalArgumentException("Veículo com ID " + id + " não encontrado.");
        }

        if (modelo != null && !modelo.trim().isEmpty()) {
            existente.setModelo(modelo);
        }
        if (cor != null && !cor.trim().isEmpty()) {
            existente.setCor(cor);
        }
        if (anoFabricacao > 0) {
            existente.setAnoFabricacao(anoFabricacao);
        }
        repository.update(existente);
    }

    /**
     * Atualiza um veículo (versão sobrecarregada que aceita objeto Veiculo).
     */
    public void atualizarVeiculo(Veiculo veiculo) throws SQLException {
        atualizarVeiculo(veiculo.getId(), veiculo.getModelo(),
                veiculo.getCor(), veiculo.getAnoFabricacao());
    }

    /**
     * Remove um veículo do sistema.
     */
    public void removerVeiculo(int id) throws SQLException {
        Veiculo v = repository.findById(String.valueOf(id));
        if (v == null) {
            throw new IllegalArgumentException("Veículo com ID " + id + " não encontrado.");
        }

        repository.delete(String.valueOf(id));
    }

    /**
     * Lista todos os veículos cadastrados.
     */
    public List<Veiculo> listarTodosVeiculos() throws SQLException {
        return repository.findAll();
    }

    /**
     * Método do serviço (Service) para listar veículos associados a um cliente
     * específico.
     */
    public List<Veiculo> listarVeiculosPorCliente(String clienteId) throws SQLException {
        if (clienteId == null || clienteId.trim().isEmpty()) {
            throw new IllegalArgumentException("O ID do cliente não pode ser vazio.");
        }
        return repository.findByClienteId(clienteId);
    }
}
