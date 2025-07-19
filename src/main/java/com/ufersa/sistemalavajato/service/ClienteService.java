package com.ufersa.sistemalavajato.service;

import com.ufersa.sistemalavajato.model.Cliente;
import com.ufersa.sistemalavajato.model.Servico;
import com.ufersa.sistemalavajato.repository.ClienteRepository;
import com.ufersa.sistemalavajato.repository.ServicoRepository;
import com.ufersa.sistemalavajato.repository.VeiculoRepository; 
import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Camada de Serviço para a entidade Cliente.
 * Responsável por conter as regras de negócio, validações de dados
 * e orquestrar as operações, delegando a persistência para a camada de
 * repositório.
 */
public class ClienteService {

    // Dependências necessárias para as operações de cliente.
    private final ClienteRepository repository;
    private final ServicoRepository servicoRepository;
    private final VeiculoRepository veiculoRepository; 

    public ClienteService() {
        this.repository = new ClienteRepository();
        this.servicoRepository = new ServicoRepository();
        this.veiculoRepository = new VeiculoRepository();
    }

    /**
     * Valida os dados e cadastra um novo cliente.
     */
    public void cadastrarCliente(String id, String nome, String email, String senha, String endereco, String telefone)
            throws SQLException, IllegalArgumentException {
        // Bloco de validações dos dados de entrada
        if (id == null || id.trim().isEmpty())
            throw new IllegalArgumentException("O ID do cliente não pode ser vazio.");

        // só dígitos e não pode começar com zero
        if (!id.matches("^[1-9]\\d{0,9}$"))
            throw new IllegalArgumentException("ID deve ser número inteiro positivo sem zeros à esquerda.");

        if (nome == null || nome.trim().isEmpty())
            throw new IllegalArgumentException("O nome não pode ser vazio.");

        if (!nome.matches("^[A-Za-zÀ-ÖØ-öø-ÿ ]+$")) 
            throw new IllegalArgumentException("O nome só pode conter letras e espaços.");

        if (nome.contains("  ")) 
            throw new IllegalArgumentException("O nome não pode conter espaços duplos.");
        
        if (endereco == null || endereco.trim().isEmpty())
            throw new IllegalArgumentException("O endereço não pode ser vazio.");

        if (!endereco.matches("^[A-Za-zÀ-ÖØ-öø-ÿ0-9 ,.-]+$"))
            throw new IllegalArgumentException("O endereço só pode conter letras, números, espaços, vírgulas, pontos e hífens.");

        if (endereco.contains("  ")) 
            throw new IllegalArgumentException("O endereço não pode conter espaços duplos.");
        
        if (telefone == null || telefone.trim().isEmpty()) 
            throw new IllegalArgumentException("O telefone não pode ser vazio.");
        
        if (!telefone.matches("^\\+?\\d{8,15}$")) 
            throw new IllegalArgumentException( "Telefone inválido. Deve conter apenas dígitos (8–15 caracteres), " + "podendo começar com '+' para código de país.");

        if (email == null || !Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", email)) 
            throw new IllegalArgumentException("Formato de e-mail inválido.");

        if (senha == null || senha.length() < 6)
            throw new IllegalArgumentException("A senha deve ter no mínimo 6 caracteres.");

        // Regra de negócio: não permitir cadastro com ID ou email duplicado
        if (repository.findById(id) != null)
            throw new IllegalArgumentException("Já existe um cliente com este ID.");

        if (repository.findByEmail(email) != null)
            throw new IllegalArgumentException("Já existe um cliente com este e-mail.");

        // Se tudo estiver válido, cria o objeto e delega a persistência ao repositório
        Cliente novoCliente = new Cliente(id, nome, email, senha, endereco, telefone);
        repository.save(novoCliente);
    }
    
    /**
     * Retorna uma lista com todos os clientes.
     */
    public List<Cliente> listarTodosClientes() throws SQLException {
        return repository.findAll();
    }

    /**
     * Busca um cliente pelo ID. Lança uma exceção se não for encontrado.
     */
    public Cliente buscarClientePorId(String id) throws SQLException, IllegalArgumentException {
        Cliente cliente = repository.findById(id);
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente com o ID " + id + " não encontrado.");
        }
        return cliente;
    }

    /**
     * Valida e atualiza os dados de um cliente existente.
     */
    public void atualizarCliente(String id, String novoNome, String novoEmail, String novaSenha, String novoEndereco,
            String novoTelefone) throws SQLException, IllegalArgumentException {
        // Primeiro, busca o cliente para garantir que ele existe
        Cliente cliente = buscarClientePorId(id);


        // Atualiza o objeto com os novos dados
        cliente.setNome(novoNome);
        cliente.setEmail(novoEmail);
        cliente.setEndereco(novoEndereco);
        cliente.setNumeroTelefone(novoTelefone);
        if (novaSenha != null && !novaSenha.isEmpty()) {
            cliente.setSenha(novaSenha);
        }

        repository.update(cliente);
    }

    /**
     * Remove um cliente do sistema.
     */
    public void removerCliente(String id) throws SQLException, IllegalArgumentException, IllegalStateException {
        // Garante que o cliente existe
        buscarClientePorId(id);

        // Esta verificação é uma regra de negócio CRÍTICA para evitar a exclusão de
        // clientes com veículos ativos.

        int quantidadeVeiculos = veiculoRepository.countByClienteId(id);
        if (quantidadeVeiculos > 0) {
            throw new IllegalStateException(
                    "Não é possível remover cliente que possui " + quantidadeVeiculos + " veículo(s) cadastrado(s).");
        }

        repository.delete(id);
    }

    /**
     * Retorna uma lista de todos os serviços solicitados por um cliente específico.
     */
    public List<Servico> verServicosSolicitados(String clienteId) throws SQLException {
       
        buscarClientePorId(clienteId);

        return servicoRepository.findByClienteId(clienteId);
    }
}