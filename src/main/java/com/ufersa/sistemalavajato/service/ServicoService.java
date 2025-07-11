package com.ufersa.sistemalavajato.service;

import com.ufersa.sistemalavajato.repository.ServicoRepository;
import com.ufersa.sistemalavajato.model.Servico;
import com.ufersa.sistemalavajato.model.Veiculo;
import com.ufersa.sistemalavajato.model.Funcionario;
import java.sql.SQLException;
import java.util.List;

/**
 * Service para lógica de negócio relacionada a serviços.
 */
public class ServicoService {

    private final ServicoRepository servicoRepository;

    public ServicoService() {
        this.servicoRepository = new ServicoRepository();
    }

    /**
     * Cadastra um novo serviço.
     */
    public void cadastrarServico(String tipo, String descricao, double preco, 
                                Veiculo veiculo, Funcionario funcionario) throws SQLException {
        // Validações
        if (tipo == null || tipo.trim().isEmpty()) {
            throw new IllegalArgumentException("Tipo do serviço não pode ser vazio");
        }
        if (preco <= 0) {
            throw new IllegalArgumentException("Preço deve ser maior que zero");
        }
        if (veiculo == null) {
            throw new IllegalArgumentException("Veículo é obrigatório");
        }
        if (funcionario == null) {
            throw new IllegalArgumentException("Funcionário é obrigatório");
        }

        // Cria o serviço
        Servico servico = new Servico(0, tipo, veiculo, funcionario); // ID será gerado pelo banco
        servico.setDescricao(descricao);
        servico.setPreco(preco);
        servico.setStatus("PENDENTE");

        // Salva no banco
        servicoRepository.save(servico);
    }

    /**
     * Inicia um serviço.
     */
    public void iniciarServico(int servicoId) throws SQLException {
        // Verifica se o serviço existe
        if (!servicoRepository.existsById(servicoId)) {
            throw new IllegalArgumentException("Serviço não encontrado");
        }

        // Verifica se pode ser iniciado
        Servico servico = servicoRepository.findById(String.valueOf(servicoId));
        if (!"PENDENTE".equals(servico.getStatus())) {
            throw new IllegalStateException("Só é possível iniciar serviços pendentes");
        }

        // Inicia o serviço
        servicoRepository.iniciarServico(servicoId);
    }

    /**
     * Cancela um serviço.
     */
    public void cancelarServico(int servicoId, String motivo) throws SQLException {
        // Verifica se o serviço existe
        if (!servicoRepository.existsById(servicoId)) {
            throw new IllegalArgumentException("Serviço não encontrado");
        }

        // Verifica se pode ser cancelado
        Servico servico = servicoRepository.findById(String.valueOf(servicoId));
        if ("CONCLUIDO".equals(servico.getStatus())) {
            throw new IllegalStateException("Não é possível cancelar serviços concluídos");
        }

        // Cancela o serviço
        servicoRepository.cancelarServico(servicoId);
    }

    /**
     * Conclui um serviço.
     */
    public void concluirServico(int servicoId) throws SQLException {
        // Verifica se o serviço existe
        if (!servicoRepository.existsById(servicoId)) {
            throw new IllegalArgumentException("Serviço não encontrado");
        }

        // Verifica se pode ser concluído
        Servico servico = servicoRepository.findById(String.valueOf(servicoId));
        if (!"EM_ANDAMENTO".equals(servico.getStatus())) {
            throw new IllegalStateException("Só é possível concluir serviços em andamento");
        }

        // Conclui o serviço
        servicoRepository.concluirServico(servicoId);
    }

    /**
     * Atualiza informações de um serviço.
     */
    public void atualizarServico(int servicoId, String tipo, String descricao, 
                                double preco, String formaPagamento) throws SQLException {
        // Busca o serviço
        Servico servico = servicoRepository.findById(String.valueOf(servicoId));
        if (servico == null) {
            throw new IllegalArgumentException("Serviço não encontrado");
        }

        // Validações
        if (tipo != null && !tipo.trim().isEmpty()) {
            servico.setTipo(tipo);
        }
        if (descricao != null) {
            servico.setDescricao(descricao);
        }
        if (preco > 0) {
            servico.setPreco(preco);
        }
        if (formaPagamento != null && !formaPagamento.trim().isEmpty()) {
            servico.setFormaPagamento(formaPagamento);
        }

        // Atualiza no banco
        servicoRepository.update(servico);
    }

    /**
     * Lista todos os serviços.
     */
    public List<Servico> listarTodosServicos() throws SQLException {
        return servicoRepository.findAll();
    }

    /**
     * Lista serviços realizados (concluídos).
     */
    public List<Servico> listarServicosRealizados() throws SQLException {
        return servicoRepository.findByStatus("CONCLUIDO");
    }

    /**
     * Lista serviços em andamento.
     */
    public List<Servico> listarServicosEmAndamento() throws SQLException {
        return servicoRepository.findByStatus("EM_ANDAMENTO");
    }

    /**
     * Lista serviços pendentes.
     */
    public List<Servico> listarServicosPendentes() throws SQLException {
        return servicoRepository.findByStatus("PENDENTE");
    }

    /**
     * Busca serviços por tipo.
     */
    public List<Servico> buscarServicosPorTipo(String tipo) throws SQLException {
        if (tipo == null || tipo.trim().isEmpty()) {
            throw new IllegalArgumentException("Tipo não pode ser vazio");
        }
        return servicoRepository.findByTipo(tipo);
    }

    /**
     * Busca serviços por veículo.
     */
    public List<Servico> buscarServicosPorVeiculo(String veiculoId) throws SQLException {
        if (veiculoId == null || veiculoId.trim().isEmpty()) {
            throw new IllegalArgumentException("ID do veículo não pode ser vazio");
        }
        return servicoRepository.findByVeiculo(veiculoId);
    }

    /**
     * Busca serviços por funcionário.
     */
    public List<Servico> buscarServicosPorFuncionario(String funcionarioId) throws SQLException {
        if (funcionarioId == null || funcionarioId.trim().isEmpty()) {
            throw new IllegalArgumentException("ID do funcionário não pode ser vazio");
        }
        return servicoRepository.findByFuncionario(funcionarioId);
    }

    /**
     * Busca serviços por faixa de preço.
     */
    public List<Servico> buscarServicosPorPreco(double precoMin, double precoMax) throws SQLException {
        if (precoMin < 0 || precoMax < 0 || precoMin > precoMax) {
            throw new IllegalArgumentException("Faixa de preço inválida");
        }
        
        // Como não temos o método no repository, vamos buscar todos e filtrar
        List<Servico> todosServicos = servicoRepository.findAll();
        return todosServicos.stream()
                .filter(s -> s.getPreco() >= precoMin && s.getPreco() <= precoMax)
                .toList();
    }

    /**
     * Exibe detalhes de um serviço específico.
     */
    public Servico exibirServicoAtual(int servicoId) throws SQLException {
        Servico servico = servicoRepository.findById(String.valueOf(servicoId));
        if (servico == null) {
            throw new IllegalArgumentException("Serviço não encontrado");
        }
        return servico;
    }

    /**
     * Conta serviços por status.
     */
    public int contarServicosPorStatus(String status) throws SQLException {
        return servicoRepository.countByStatus(status);
    }

    /**
     * Remove um serviço (apenas se estiver pendente ou cancelado).
     */
    public void removerServico(int servicoId) throws SQLException {
        Servico servico = servicoRepository.findById(String.valueOf(servicoId));
        if (servico == null) {
            throw new IllegalArgumentException("Serviço não encontrado");
        }

        // Só permite remoção de serviços pendentes ou cancelados
        if ("EM_ANDAMENTO".equals(servico.getStatus()) || "CONCLUIDO".equals(servico.getStatus())) {
            throw new IllegalStateException("Não é possível remover serviços em andamento ou concluídos");
        }

        servicoRepository.delete(String.valueOf(servicoId));
    }
}
