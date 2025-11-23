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
    public void cadastrarVeiculo(String idCliente, String modelo, int numChassi, double quilometragem, double preco, String cor, int anoFabricacao, String status) throws SQLException {
    // Validação do ID do cliente
    if (idCliente == null || idCliente.trim().isEmpty()) 
        throw new IllegalArgumentException("ID do cliente é obrigatório.");
    
    if (!idCliente.matches("^[1-9]\\d{0,9}$")) 
        throw new IllegalArgumentException("ID do cliente deve ser número inteiro positivo sem zeros à esquerda.");
    
    // Validação do número do chassi
    if (numChassi <= 0) 
        throw new IllegalArgumentException("Número de chassi deve ser um inteiro positivo.");
    
    if (repository.findByNumChassi(numChassi) != null) 
        throw new IllegalArgumentException("Já existe veículo cadastrado com o chassi " + numChassi);

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

    // Validação da quilometragem e preço
    if (quilometragem < 0) 
        throw new IllegalArgumentException("Quilometragem não pode ser negativa.");
    
    if (preco < 0) 
        throw new IllegalArgumentException("Preço não pode ser negativo.");
    

    // Validação do status
    if (status == null || status.trim().isEmpty()) 
        throw new IllegalArgumentException("Status do veículo não pode ser vazio.");
    

    // Criação do objeto Veiculo após validações
    Veiculo veiculo = new Veiculo(idCliente, modelo, numChassi, quilometragem, preco, cor, anoFabricacao, status);
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
                    "Veículo com chassi " + numChassi + " não encontrado.");
        }
        return v;
    }

    /**
     * Atualiza os dados de um veículo existente.
     */
    public void atualizarVeiculo(int numChassi, String idCliente, String modelo, double quilometragem, double preco,
            String cor, int anoFabricacao, String status) throws SQLException {
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
                    "Veículo com chassi " + numChassi + " não encontrado.");
        }

        repository.delete(String.valueOf(numChassi));
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
