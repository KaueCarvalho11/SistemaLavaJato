package com.ufersa.sistemalavajato.service;

import com.ufersa.sistemalavajato.model.Veiculo;
import com.ufersa.sistemalavajato.repository.VeiculoRepository;
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
    public void cadastrarVeiculo(Veiculo veiculo) throws SQLException {
        if (veiculo == null) {
            throw new IllegalArgumentException("Veículo não pode ser nulo.");
        }
        int chassi = veiculo.getNumChassi();
        if (chassi <= 0) {
            throw new IllegalArgumentException("Número de chassi inválido.");
        }
        String cliente = veiculo.getIdCliente();
        if (cliente == null || cliente.trim().isEmpty()) {
            throw new IllegalArgumentException("ID do cliente é obrigatório.");
        }
        // verifica existência usando findByNumChassi
        if (repository.findByNumChassi(chassi) != null) {
            throw new IllegalArgumentException(
                "Já existe veículo cadastrado com o chassi " + chassi
            );
        }
        repository.save(veiculo);
    }

    /**
     * Busca um veículo pelo número de chassi.
     */
    public Veiculo buscarVeiculoPorChassi(int numChassi) throws SQLException {
        if (numChassi <= 0) {
            throw new IllegalArgumentException("Número de chassi inválido.");
        }
        Veiculo v = repository.findByNumChassi(numChassi);
        if (v == null) {
            throw new IllegalArgumentException(
                "Veículo com chassi " + numChassi + " não encontrado."
            );
        }
        return v;
    }

    /**
     * Atualiza os dados de um veículo existente.
     */
     public void atualizarVeiculo(int numChassi, String idCliente, String modelo, double quilometragem, double preco, String cor, int anoFabricacao, String status) throws SQLException {
        // Busca o veículo existente
        Veiculo existente = repository.findById(String.valueOf(numChassi));
        if (existente == null) {
            throw new IllegalArgumentException("Veículo com chassi " + numChassi + " não encontrado.");
        }
        
        if (idCliente != null && !idCliente.trim().isEmpty()) {
            existente.setIdCliente(idCliente);
        }
        if (modelo != null && !modelo.trim().isEmpty()) {
            existente.setModelo(modelo);
        }
        if (quilometragem >= 0) {
            existente.setQuilometragem(quilometragem);
        }
        if (preco >= 0) {
            existente.setPreco(preco);
        }
        if (cor != null && !cor.trim().isEmpty()) {
            existente.setCor(cor);
        }
        if (anoFabricacao > 0) {
            existente.setAnoFabricacao(anoFabricacao);
        }
        if (status != null && !status.trim().isEmpty()) {
            existente.setStatus(status);
        }
        repository.update(existente);
    }

    /**
     * Remove um veículo do sistema.
     */
    public void removerVeiculo(int numChassi) throws SQLException {
        Veiculo v = repository.findByNumChassi(numChassi);
        if (v == null) {
            throw new IllegalArgumentException(
                "Veículo com chassi " + numChassi + " não encontrado."
            );
        }
        
        repository.delete(String.valueOf(numChassi)); 
    }

    /**
     * Lista todos os veículos cadastrados.
     */
    public List<Veiculo> listarTodosVeiculos() throws SQLException {
        return repository.findAll();
    }

    // Adicione este método à sua classe VeiculoService

public List<Veiculo> listarVeiculosPorCliente(String clienteId) throws SQLException {
    if (clienteId == null || clienteId.trim().isEmpty()) {
        throw new IllegalArgumentException("O ID do cliente não pode ser vazio.");
    }
    return repository.findByClienteId(clienteId);
}
}
