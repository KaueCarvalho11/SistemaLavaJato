package com.paintspray.service;

import com.paintspray.model.Funcionario;
import com.paintspray.model.Servico;
import com.paintspray.repository.FuncionarioRepository;
import com.paintspray.repository.ServicoRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

//Service para a lógica de negócio da entidade Funcionario.
public class FuncionarioService {

    private final FuncionarioRepository repository;
    private final ServicoRepository servicoRepository;

    public FuncionarioService() {
        this.repository = new FuncionarioRepository();
        this.servicoRepository = new ServicoRepository();
    }

    //Cadastra um novo funcionário após validar os dados.
    public void cadastrarFuncionario(String id, String nome, String email, String senha) throws SQLException {
        if (id == null || id.trim().isEmpty()) 
            throw new IllegalArgumentException("O ID do funcionário não pode ser vazio.");

        if (!id.matches("^[1-9]\\d{0,9}$"))
            throw new IllegalArgumentException("ID deve ser número inteiro positivo sem zeros à esquerda.");

        if (nome == null || nome.trim().isEmpty()) 
            throw new IllegalArgumentException("O nome do funcionário não pode ser vazio.");

        if (!nome.matches("^[A-Za-zÀ-ÖØ-öø-ÿ ]+$")) 
            throw new IllegalArgumentException("O nome só pode conter letras e espaços.");
        
        if (nome.contains("  ")) 
            throw new IllegalArgumentException("O nome não pode conter espaços duplos.");
        
        if (email == null || !Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", email)) {
            throw new IllegalArgumentException("Formato de e-mail inválido.");
        }
        if (senha == null || senha.length() < 6) {
            throw new IllegalArgumentException("A senha deve ter no mínimo 6 caracteres.");
        }

        if (repository.findById(id) != null) {
            throw new IllegalArgumentException("Já existe um funcionário com este ID.");
        }

        if (repository.EncontrarPorEmail(email) != null) {
            throw new IllegalArgumentException("Já existe um funcionário com este e-mail.");
        }

        Funcionario novoFuncionario = new Funcionario(id, nome, email, senha);
        repository.save(novoFuncionario);
    }

    //Lista todos os funcionários cadastrados.
    public List<Funcionario> listarTodosFuncionarios() throws SQLException {
        return repository.findAll();
    }

    //Busca um funcionário pelo seu ID.
    public Funcionario buscarFuncionarioPorId(String id) throws SQLException {
        Funcionario funcionario = repository.findById(id);
        if (funcionario == null) {
            throw new IllegalArgumentException("Funcionário com o ID " + id + " não encontrado.");
        }
        return funcionario;
    }

    
    //Atualiza os dados de um funcionário.
    public void atualizarFuncionario(String id, String novoNome, String novoEmail, String novaSenha) throws SQLException {
        Funcionario funcionario = buscarFuncionarioPorId(id);

        if (novoNome == null || novoNome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome não pode ser vazio.");
        }

        if (novoEmail == null || !Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", novoEmail)) {
            throw new IllegalArgumentException("Formato de e-mail inválido.");
        }

        if (novaSenha == null || novaSenha.length() < 6) {
            throw new IllegalArgumentException("A senha deve ter no minimo 6 caracteres.");
        }

        funcionario.setNome(novoNome);
        funcionario.setEmail(novoEmail);
        funcionario.setSenha(novaSenha);

        repository.update(funcionario);
    }

    //Remove um funcionário do sistema, se não tiver serviços em andamento ou concluídos.
    public void removerFuncionario(String id) throws SQLException {
        Funcionario funcionario = buscarFuncionarioPorId(id);

        List<Servico> servicos = servicoRepository.findByFuncionario(funcionario.getId());

        boolean possuiServicosAtivos = servicos.stream()
                .anyMatch(s -> "EM_ANDAMENTO".equals(s.getStatus()) || "CONCLUIDO".equals(s.getStatus()));

        if (possuiServicosAtivos) {
            throw new IllegalStateException("Não é possível remover um funcionário com serviços em andamento ou concluídos.");
        }

        repository.delete(id);
    }
}
