package com.paintspray.ui;

import com.paintspray.model.Cliente;
import com.paintspray.model.Funcionario;
import com.paintspray.model.Servico;
import com.paintspray.model.Veiculo;
import com.paintspray.service.ClienteService;
import com.paintspray.service.FuncionarioService;
import com.paintspray.service.ServicoService;
import com.paintspray.service.VeiculoService;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Classe de Interface de Usuário para o Cliente.
 * Gerencia todas as interações do cliente logado com o sistema.
 */
public class ClienteUI {

    private final Cliente clienteLogado;
    private final VeiculoService veiculoService;
    private final ServicoService servicoService;
    private final ClienteService clienteService;
    private final FuncionarioService funcionarioService; 
    private final Scanner sc;

    public ClienteUI(Cliente clienteLogado) {
        this.clienteLogado = clienteLogado;
        this.veiculoService = new VeiculoService();
        this.servicoService = new ServicoService();
        this.clienteService = new ClienteService();
        this.funcionarioService = new FuncionarioService();
        this.sc = new Scanner(System.in);
    }

    /**
     * Exibe o menu principal do cliente e gerencia a navegação.
     */
    public void menu() {
        int op = -1;
        while (op != 0) {
            System.out.println("\n--- MENU DO CLIENTE: " + clienteLogado.getNome() + " ---");
            System.out.println("(1) Gerenciar meus veículos");
            System.out.println("(2) Solicitar um novo serviço");
            System.out.println("(3) Visualizar meus serviços solicitados");
            System.out.println("(0) Deslogar e voltar ao menu principal");
            System.out.print("Escolha uma opção: ");

            try {
                op = sc.nextInt();
                sc.nextLine(); 

                switch (op) {
                    case 1:
                        gerenciarVeiculos();
                        break;
                    case 2:
                        solicitarServico();
                        break;
                    case 3:
                        visualizarMeusServicos();
                        break;
                    case 0:
                        System.out.println("Deslogando...");
                        break;
                    default:
                        System.err.println("Opção inválida. Tente novamente.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.err.println("Entrada inválida. Por favor, digite um número.");
                sc.nextLine(); 
            } catch (SQLException | IllegalArgumentException | IllegalStateException e) {
                System.err.println("ERRO: " + e.getMessage());
            }
        }
    }

    /**
     * Submenu para gerenciamento de veículos.
     */
    private void gerenciarVeiculos() throws SQLException {
        int op = -1;
        while (op != 0) {
            System.out.println("\n--- GERENCIAR VEÍCULOS ---");
            System.out.println("(1) Adicionar novo veículo");
            System.out.println("(2) Remover um veículo");
            System.out.println("(3) Listar meus veículos");
            System.out.println("(0) Voltar ao menu anterior");
            System.out.print("Escolha uma opção: ");

            op = sc.nextInt();
            sc.nextLine();

            switch (op) {
                case 1:
                    adicionarVeiculo();
                    break;
                case 2:
                    removerVeiculo();
                    break;
                case 3:
                    listarMeusVeiculos();
                    break;
                case 0:
                    break;
                default:
                    System.err.println("Opção inválida.");
                    break;
            }
        }
    }

    /**
     * Método para cadastrar um novo veículo associado ao cliente logado.
     **/
    private void adicionarVeiculo() throws SQLException {
    System.out.println("\n--- CADASTRAR NOVO VEÍCULO ---");
    System.out.print("Número do Chassi: ");
    int chassi = sc.nextInt();
    sc.nextLine(); // Limpa o buffer

    System.out.print("Quilometragem: ");
    double quilometragem = sc.nextDouble();
    sc.nextLine(); // Limpa o buffer

    System.out.print("Preço: ");
    double preco = sc.nextDouble();
    sc.nextLine(); // Limpa o buffer

    System.out.print("Modelo (ex: Fiat Uno): ");
    String modelo = sc.nextLine();

    System.out.print("Cor: ");
    String cor = sc.nextLine();

    System.out.print("Ano de Fabricação: ");
    int ano = sc.nextInt();
    sc.nextLine(); // Limpa o buffer

    veiculoService.cadastrarVeiculo(clienteLogado.getId(), modelo, chassi, quilometragem, preco, cor, ano, "disponivel");
    System.out.println("Veículo cadastrado com sucesso!");
}

    /**
     * Método para remover um veículo do cliente logado.
     **/
    private void removerVeiculo() throws SQLException {
        System.out.println("\n--- REMOVER VEÍCULO ---");
        listarMeusVeiculos();
        System.out.print("Digite o número do chassi do veículo para remover: ");
        int chassi = sc.nextInt();
        sc.nextLine();

       
        List<Servico> servicosDoVeiculo = servicoService.buscarServicosPorVeiculo(chassi);
        boolean temServicoAtivo = servicosDoVeiculo.stream()
                .anyMatch(s -> "EM_ANDAMENTO".equals(s.getStatus()) || "PENDENTE".equals(s.getStatus()));

        if (temServicoAtivo) {
            throw new IllegalStateException(
                    "ERRO: Não é possível remover um veículo com serviços pendentes ou em andamento.");
        }

        veiculoService.removerVeiculo(chassi);
        System.out.println("Veículo removido com sucesso!");
    }

    /**
     * Lista todos os veículos associados ao cliente logado.
     **/
    private void listarMeusVeiculos() throws SQLException {
        System.out.println("\n--- MEUS VEÍCULOS ---");
        List<Veiculo> veiculos = veiculoService.listarVeiculosPorCliente(clienteLogado.getId());

        if (veiculos.isEmpty()) {
            System.out.println("Você não possui veículos cadastrados.");
        } else {
            veiculos.forEach(v -> System.out.printf("Modelo: %s, Chassi: %d, Cor: %s, Ano: %d, Quilometragem: %.2f, Preco: R$%.2f\n", 
                    v.getModelo(), v.getNumChassi(), v.getCor(), v.getAnoFabricacao(), v.getQuilometragem(), v.getPreco()));
        }
    }

    /**
     * Permite ao cliente solicitar um novo serviço para um de seus veículos.
     **/
    private void solicitarServico() throws SQLException {
        System.out.println("\n--- SOLICITAR SERVIÇO ---");
        listarMeusVeiculos();
        System.out.print("Digite o número do chassi do veículo: ");
        int chassi = sc.nextInt();
        sc.nextLine();
        Veiculo veiculoEscolhido = veiculoService.buscarVeiculoPorChassi(chassi);

        if (!veiculoEscolhido.getIdCliente().equals(clienteLogado.getId())) {
            throw new IllegalArgumentException("ERRO: Este veículo não pertence a você.");
        }

        System.out.println(
                "Escolha o tipo de serviço: (1) Limpeza Completa [R$100] (2) Limpeza Externa [R$50] (3) Polimento [R$250]");
        int tipoOp = sc.nextInt();
        sc.nextLine();

        String tipoServico;
        double precoBase;
        switch (tipoOp) {
            case 1:
                tipoServico = "Limpeza Completa";
                precoBase = 100.0;
                break;
            case 2:
                tipoServico = "Limpeza Externa";
                precoBase = 50.0;
                break;
            case 3:
                tipoServico = "Polimento";
                precoBase = 250.0;
                break;
            default:
                throw new IllegalArgumentException("Opção de serviço inválida.");
        }

        List<Funcionario> funcionarios = funcionarioService.listarTodosFuncionarios();
        if (funcionarios.isEmpty()) {
            throw new IllegalStateException("Não há funcionários disponíveis no momento.");
        }

        System.out.println("\n--- Funcionários Disponíveis ---");
        for (int i = 0; i < funcionarios.size(); i++) {
            System.out.printf("(%d) - %s\n", i + 1, funcionarios.get(i).getNome());
        }

        System.out.print("Escolha um funcionário (digite o número): ");
        int indiceEscolhido = sc.nextInt() - 1;
        sc.nextLine();

        if (indiceEscolhido < 0 || indiceEscolhido >= funcionarios.size()) {
            throw new IllegalArgumentException("Opção de funcionário inválida.");
        }

        Funcionario funcionarioAtribuido = funcionarios.get(indiceEscolhido);

        System.out.println(
                "Escolha o tipo de pagamento: (1) Cartão de Crédito (2) Cartão de Débito (3) Pix (4) Cédulas e moedas");

        int pagamentoOp = sc.nextInt();
        sc.nextLine();

        if (pagamentoOp < 1 || pagamentoOp > 4) {
            throw new IllegalArgumentException("Opção de pagamento inválida.");
        }
        String metodoPagamento;
        switch (pagamentoOp) {
            case 1:
                metodoPagamento = "Cartão de Crédito";
                break;
            case 2:
                metodoPagamento = "Cartão de Débito";
                break;
            case 3:
                metodoPagamento = "Pix";
                break;
            case 4:
                metodoPagamento = "Cédulas e moedas";
                break;
            default:
                throw new IllegalArgumentException("Opção de pagamento inválida.");
        }

        servicoService.cadastrarServico(tipoServico, "Solicitado pelo cliente", precoBase, veiculoEscolhido,
                funcionarioAtribuido, metodoPagamento);
        System.out.println(
                "Serviço solicitado com sucesso e atribuído ao funcionário: " + funcionarioAtribuido.getNome() + "!");
    }

    /**
     * Exibe todos os serviços solicitados pelo cliente logado.
     **/
    private void visualizarMeusServicos() throws SQLException {
        System.out.println("\n--- MEUS SERVIÇOS SOLICITADOS ---");
        List<Servico> servicos = clienteService.verServicosSolicitados(clienteLogado.getId());

        if (servicos.isEmpty()) {
            System.out.println("Nenhum serviço encontrado.");
        } else {
            servicos.forEach(s -> System.out.printf("ID: %d, Tipo: %s, Status: %s, Preço: R$%.2f, Veículo Chassi: %d\n",
                    s.getIdServico(), s.getTipo(), s.getStatus(), s.getPreco(), s.getVeiculo().getNumChassi()));
        }
    }
}