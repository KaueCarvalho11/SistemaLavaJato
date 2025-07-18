package com.ufersa.sistemalavajato.ui;

import com.ufersa.sistemalavajato.model.Funcionario;
import com.ufersa.sistemalavajato.model.Servico;
import com.ufersa.sistemalavajato.service.ServicoService;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class FuncionarioUI {
    private final Funcionario funcionarioLogado;
    private final ServicoService servicoService;
    private final Scanner sc;

    public FuncionarioUI(Funcionario funcionarioLogado) {
        this.funcionarioLogado = funcionarioLogado;
        this.servicoService = new ServicoService();
        this.sc = new Scanner(System.in);
    }

    public void menu() {
        int op = -1;
        while(op != 0){
            System.out.println("\n--- MENU DO FUNCIONÁRIO: " + this.funcionarioLogado.getNome() + " ---");
            System.out.println("(1) Iniciar Serviço");
            System.out.println("(2) Atualizar Status de Serviço (Concluir/Cancelar)");
            System.out.println("(3) Definir Preço de um Serviço");
            System.out.println("(4) Visualizar Serviços em Andamento");
            System.out.println("(5) Visualizar Meus Serviços Concluídos");
            System.out.println("(0) Deslogar e Voltar ao Menu Principal");
            System.out.print("Escolha uma opção: ");

            try {
                op = sc.nextInt();
                sc.nextLine();

                switch (op) {
                    case 1:
                        iniciarServico();
                        break;
                    case 2:
                        atualizarStatusServico();
                        break;
                    case 3:
                        definirPreco();
                        break;
                    case 4:
                        visualizarServicosEmAndamento();
                        break;
                    case 5:
                        visualizarMeusServicosConcluidos();
                        break;
                    case 0:
                        System.out.println("Deslogando...");
                        ;
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.err.println("Entrada inválida. Por favor, digite um número.");
                sc.nextLine();
            }
            // permite capturar e tratar diferentes tipos de exceções
            catch (SQLException | IllegalArgumentException | IllegalStateException e) {
                System.err.println("ERRO: " + e.getMessage());
            }
        }
    }

    private void iniciarServico() throws SQLException {
        System.out.print("Digite o ID do serviço para iniciar: ");
        int servicoId = sc.nextInt();
        sc.nextLine();
        servicoService.iniciarServico(servicoId);
        System.out.println("Serviço ID " + servicoId + " iniciado com sucesso!");
    }

    private void atualizarStatusServico() throws SQLException {
        System.out.print("Digite o ID do serviço para atualizar: ");
        int servicoId = sc.nextInt();
        sc.nextLine();

        System.out.println("O que deseja fazer?");
        System.out.println("1. Concluir Serviço");
        System.out.println("2. Cancelar Serviço");
        int subOp = sc.nextInt();
        sc.nextLine();

        switch (subOp) {
            case 1:
                try {
                    servicoService.concluirServico(servicoId);
                    System.out.println("Serviço concluído!");
                } catch (SQLException e) {
                    System.err.println("Erro de banco de dados ao tentar concluir o serviço: " + e.getMessage());
                } catch (IllegalStateException e) {
                    System.err.println("Erro de regra de negócio: " + e.getMessage());
                }
                break;

            case 2:
                try {
                    servicoService.cancelarServico(servicoId);
                    System.out.println("Serviço cancelado!");
                } catch (SQLException e) {
                    System.err.println("Erro de banco de dados ao tentar cancelar o serviço: " + e.getMessage());
                } catch (IllegalStateException e) {
                    System.err.println("Erro de regra de negócio: " + e.getMessage());
                }
                break;

            default:
                System.out.println("Opção de atualização inválida.");
                break;
        }
    }

    private void definirPreco() throws SQLException {
        try {
            System.out.print("Digite o ID do serviço para DEFINIR O PREÇO: ");
            int servicoId = sc.nextInt();
            sc.nextLine();

            System.out.print("Digite o novo preço: R$ ");
            double novoPreco = sc.nextDouble();
            sc.nextLine();

            servicoService.atualizarServico(servicoId, null, null, novoPreco, null);
            System.out.println("Preço do serviço atualizado com sucesso!");
        } catch (InputMismatchException e) {
            System.err.println("ERRO DE ENTRADA: Por favor, digite um número válido para o ID ou preço.");
            sc.nextLine(); // Garante a limpeza do buffer para a próxima tentativa.
        } catch (SQLException e) {
            // CORREÇÃO: Mensagem de erro ajustada para a operação correta.
            System.err.println("ERRO DE BANCO DE DADOS: Não foi possível atualizar o preço. " + e.getMessage());
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.err.println("ERRO: " + e.getMessage());
        }
    }

    private void visualizarServicosEmAndamento() throws SQLException {
        System.out.println("\n--- SERVIÇOS EM ANDAMENTO ---");
        List<Servico> servicos = servicoService.listarServicosEmAndamento();
        if (servicos.isEmpty())
            System.out.println("Nenhum serviço em andamento no momento.");
        else {
            for (Servico s : servicos) {
                System.out.println(s);
            }
        }
    }

    private void visualizarMeusServicosConcluidos() throws SQLException {
        System.out.println("\n--- SERVIÇOS CONCLUÍDOS POR " + funcionarioLogado.getNome() + " ---");

        // NOTA: Conforme solicitado, a lógica de busca e filtro foi mantida, sem
        // alterar o Service.
        List<Servico> meusServicos = servicoService.buscarServicosPorFuncionario(funcionarioLogado.getId());
        List<Servico> concluidos = meusServicos.stream().filter(s -> "CONCLUIDO".equals(s.getStatus()))
                .collect(Collectors.toList());

        if (concluidos.isEmpty()) {
            System.out.println("Você ainda não concluiu nenhum serviço.");
        } else {
            concluidos.forEach(this::printServicoFormatado);
        }
    }

    private void printServicoFormatado(Servico s) {
        System.out.println("----------------------------------------");
        System.out.println(s);
        
        if (s.getVeiculo() != null && s.getVeiculo().getModelo() != null) {
            System.out.println(
                    "Veículo: " + s.getVeiculo().getModelo() + " (Chassi: " + s.getVeiculo().getNumChassi() + ")");
        } else {
            System.out.println("Veículo: (não especificado)");
        }
        System.out.println("----------------------------------------");
    }
}
