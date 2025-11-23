package com.paintspray.service;

import com.paintspray.repository.ServicoRepository;
import com.paintspray.model.Servico;
import com.paintspray.model.Veiculo;
import com.paintspray.model.Usuario;
import com.paintspray.enums.StatusServico;
import com.paintspray.enums.TipoServico;
import com.paintspray.enums.FormaPagamento;
import java.sql.SQLException;
import java.util.List;

/**
 * Service para lógica de negócio relacionada a serviços (Ordens de Serviço).
 */
public class ServicoService {

    private final ServicoRepository servicoRepository;

    public ServicoService() {
        this.servicoRepository = new ServicoRepository();
    }

    /**
     * Cadastra um novo serviço.
     */
    public void cadastrarServico(TipoServico tipo, String descricao, Veiculo veiculo, Usuario usuario)
            throws SQLException {
        if (tipo == null) {
            throw new IllegalArgumentException("Tipo de serviço é obrigatório");
        }
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new IllegalArgumentException("Descrição não pode ser vazia");
        }
        if (veiculo == null) {
            throw new IllegalArgumentException("Veículo é obrigatório");
        }
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário é obrigatório");
        }

        // Cria o serviço (status PENDENTE por padrão)
        Servico servico = new Servico(tipo, descricao, veiculo, usuario);

        // Salva no banco
        servicoRepository.save(servico);
    }

    /**
     * Cadastra um novo serviço com preço e forma de pagamento.
     */
    public void cadastrarServico(TipoServico tipo, String descricao, double preco,
            FormaPagamento formaPagamento, Veiculo veiculo, Usuario usuario) throws SQLException {

        if (preco < 0) {
            throw new IllegalArgumentException("Preço não pode ser negativo");
        }

        // Cria o serviço completo
        Servico servico = new Servico(0, tipo, descricao, preco, StatusServico.PENDENTE, formaPagamento, veiculo,
                usuario);

        // Salva no banco
        servicoRepository.save(servico);
    }

    /**
     * Atualiza o status para EM_ANDAMENTO (inicia o serviço).
     */
    public void iniciarServico(int servicoId) throws SQLException {
        Servico servico = servicoRepository.findById(String.valueOf(servicoId));
        if (servico == null) {
            throw new IllegalArgumentException("Serviço não encontrado");
        }

        if (servico.getStatus() != StatusServico.PENDENTE) {
            throw new IllegalStateException("Só é possível iniciar serviços pendentes");
        }

        servicoRepository.updateStatus(servicoId, StatusServico.EM_ANDAMENTO);
    }

    /**
     * Cancela um serviço.
     */
    public void cancelarServico(int servicoId) throws SQLException {
        Servico servico = servicoRepository.findById(String.valueOf(servicoId));
        if (servico == null) {
            throw new IllegalArgumentException("Serviço não encontrado");
        }

        if (servico.getStatus() == StatusServico.FINALIZADO) {
            throw new IllegalStateException("Não é possível cancelar serviços finalizados");
        }

        servicoRepository.cancelarServico(servicoId);
    }

    /**
     * Conclui um serviço (muda status para FINALIZADO).
     */
    public void concluirServico(int servicoId) throws SQLException {
        Servico servico = servicoRepository.findById(String.valueOf(servicoId));
        if (servico == null) {
            throw new IllegalArgumentException("Serviço não encontrado");
        }

        if (servico.getStatus() != StatusServico.AGUARDANDO_PAGAMENTO &&
                servico.getStatus() != StatusServico.EM_ANDAMENTO) {
            throw new IllegalStateException("Só é possível concluir serviços em andamento ou aguardando pagamento");
        }

        servicoRepository.updateStatus(servicoId, StatusServico.FINALIZADO);
    }

    /**
     * Atualiza informações de um serviço.
     */
    public void atualizarServico(int servicoId, TipoServico tipo, String descricao,
            double preco, FormaPagamento formaPagamento) throws SQLException {

        Servico servico = servicoRepository.findById(String.valueOf(servicoId));
        if (servico == null) {
            throw new IllegalArgumentException("Serviço não encontrado");
        }

        if (servico.getStatus() == StatusServico.FINALIZADO) {
            throw new IllegalStateException("Não é possível atualizar serviços finalizados");
        }

        if (tipo != null) {
            servico.setTipo(tipo);
        }
        if (descricao != null) {
            servico.setDescricao(descricao);
        }
        if (preco >= 0) {
            servico.setPreco(preco);
        }
        if (formaPagamento != null) {
            servico.setFormaPagamento(formaPagamento);
        }

        servicoRepository.update(servico);
    }

    /**
     * Define preço de um serviço.
     */
    public void definirPreco(int servicoId, double preco) throws SQLException {
        if (preco < 0) {
            throw new IllegalArgumentException("Preço não pode ser negativo");
        }

        Servico servico = servicoRepository.findById(String.valueOf(servicoId));
        if (servico == null) {
            throw new IllegalArgumentException("Serviço não encontrado");
        }

        servico.setPreco(preco);
        servicoRepository.update(servico);
    }

    /**
     * Lista todos os serviços.
     */
    public List<Servico> listarTodosServicos() throws SQLException {
        return servicoRepository.findAll();
    }

    /**
     * Lista todos os serviços (alias).
     */
    public List<Servico> listarTodos() throws SQLException {
        return servicoRepository.findAll();
    }

    /**
     * Lista serviços finalizados.
     */
    public List<Servico> listarServicosRealizados() throws SQLException {
        return servicoRepository.findByStatus(StatusServico.FINALIZADO);
    }

    /**
     * Lista serviços finalizados (alias).
     */
    public List<Servico> listarServicosFinalizados() throws SQLException {
        return servicoRepository.findByStatus(StatusServico.FINALIZADO);
    }

    /**
     * Lista serviços em andamento.
     */
    public List<Servico> listarServicosEmAndamento() throws SQLException {
        return servicoRepository.findByStatus(StatusServico.EM_ANDAMENTO);
    }

    /**
     * Lista serviços pendentes.
     */
    public List<Servico> listarServicosPendentes() throws SQLException {
        return servicoRepository.findByStatus(StatusServico.PENDENTE);
    }

    /**
     * Lista serviços aguardando pagamento.
     */
    public List<Servico> listarServicosAguardandoPagamento() throws SQLException {
        return servicoRepository.findByStatus(StatusServico.AGUARDANDO_PAGAMENTO);
    }

    /**
     * Busca serviços por veículo.
     */
    public List<Servico> buscarServicosPorVeiculo(int veiculoId) throws SQLException {
        if (veiculoId <= 0) {
            throw new IllegalArgumentException("ID do veículo inválido");
        }
        return servicoRepository.findByVeiculo(veiculoId);
    }

    /**
     * Busca serviços por cliente (através dos veículos do cliente).
     */
    public List<Servico> buscarServicosPorCliente(String clienteId) throws SQLException {
        if (clienteId == null || clienteId.trim().isEmpty()) {
            throw new IllegalArgumentException("ID do cliente não pode ser vazio");
        }
        return servicoRepository.findByClienteId(clienteId);
    }

    /**
     * Exibe detalhes de um serviço específico.
     */
    public Servico exibirServico(int servicoId) throws SQLException {
        Servico servico = servicoRepository.findById(String.valueOf(servicoId));
        if (servico == null) {
            throw new IllegalArgumentException("Serviço não encontrado");
        }
        return servico;
    }

    /**
     * Atualiza o status de um serviço.
     */
    public void atualizarStatusServico(int servicoId, StatusServico novoStatus) throws SQLException {
        Servico servico = servicoRepository.findById(String.valueOf(servicoId));
        if (servico == null) {
            throw new IllegalArgumentException("Serviço não encontrado");
        }

        servicoRepository.updateStatus(servicoId, novoStatus);
    }

    /**
     * Remove um serviço (apenas se estiver pendente ou cancelado).
     */
    public void removerServico(int servicoId) throws SQLException {
        Servico servico = servicoRepository.findById(String.valueOf(servicoId));
        if (servico == null) {
            throw new IllegalArgumentException("Serviço não encontrado");
        }

        if (servico.getStatus() == StatusServico.EM_ANDAMENTO ||
                servico.getStatus() == StatusServico.FINALIZADO) {
            throw new IllegalStateException("Não é possível remover serviços em andamento ou finalizados");
        }

        servicoRepository.delete(String.valueOf(servicoId));
    }
}
