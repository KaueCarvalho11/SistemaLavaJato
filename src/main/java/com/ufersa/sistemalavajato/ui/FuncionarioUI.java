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

    public FuncionarioUI(Funcionario funcionarioLogado){
        this.funcionarioLogado = funcionarioLogado;
        this.servicoService = new ServicoService();
        this.sc = new Scanner(System.in);
    }

    public void menu(Funcionario funcionarioLogado){
        int op = -1;
        while(op != 0){
            System.out.println("\n--- MENU DO FUNCIONÁRIO---");
            System.out.println("(1) Iniciar serviço");
            System.out.println("(2) Atualizar serviço (Concluir, Cancelar, Mudar Preço)");
            System.out.println("(3) Visualizar serviços em andamento");
            System.out.println("(4) Visualizar meus serviços concluídos");
            System.out.println("(0) Deslogar e voltar ao menu principal");
            System.out.print("Escolha uma opção: ");

            try{
                op = sc.nextInt();
                sc.nextLine();

                switch (op) {
                    case 1:
                        iniciarServico();
                        break;
                    case 2:
                        atualizarServico();
                        break;
                    case 3:
                        visualizarServicosEmAndamento();
                        break;
                    case 4:
                        visualizarMeusServicosConcluidos();
                        break;
                    case 0:
                        System.out.println("Deslogando...");;
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                        break;
                }
            }
            catch(InputMismatchException e){
                System.err.println("Entrada inválida. Por favor, digite um número.");
                sc.nextLine();
            }
            // permite capturar e tratar diferentes tipos de exceções
            catch(SQLException | IllegalArgumentException | IllegalStateException e){
                System.err.println("ERRO: " + e.getMessage());
            }
        }
    }

    private void iniciarServico() throws SQLException{
        System.out.print("Digite o ID do serviço para iniciar: ");
        int servicoId = sc.nextInt();
        sc.nextLine();
        servicoService.iniciarServico(servicoId);
        System.out.println("Serviço ID " + servicoId + " iniciado com sucesso!");
    }

    private void atualizarServico() throws SQLException{
        System.out.print("Digite o ID do serviço para atualizar: ");
        int servicoId = sc.nextInt();
        sc.nextLine();

        System.out.println("O que deseja fazer?");
        System.out.println("1. Concluir Serviço");
        System.out.println("2. Cancelar Serviço");
        System.out.println("3. Definir/Alterar Preço");
        int subOp = sc.nextInt();
        sc.nextLine();

        switch (subOp) {
            case 1:
                try{
                    servicoService.concluirServico(servicoId);
                    System.out.println("Serviço concluído!");
                }
                catch(SQLException e){
                    System.err.println("Erro de banco de dados ao tentar concluir o serviço: " + e.getMessage());
                }
                catch(IllegalStateException e){
                    System.err.println("Erro de regra de negócio: " + e.getMessage());
                }
                break;

            case 2:
                try{
                    System.out.println("Informe o motivo da cancelamento: ");
                    String motivo = sc.nextLine();
                    servicoService.cancelarServico(servicoId, motivo);
                    System.out.println("Serviço cancelado!");
                }
                catch(SQLException e){
                    System.err.println("Erro de banco de dados ao tentar cancelar o serviço: " + e.getMessage());
                }
                catch(IllegalStateException e){
                    System.err.println("Erro de regra de negócio: " + e.getMessage());
                }
                break;
            
            case 3:
                try{
                    System.out.print("Digite o novo preço: R$ ");
                    double novoPreco = sc.nextDouble();
                    sc.nextLine();
                    servicoService.atualizarServico(servicoId, null, null, novoPreco, null);
                    System.out.println("Preço do serviço atualizado!");
                }
                catch(SQLException e){
                    System.err.println("Erro de banco de dados ao tentar cancelar o serviço: " + e.getMessage());
                }
                catch(IllegalStateException e){
                    System.err.println("Erro de regra de negócio: " + e.getMessage());
                }
                break;
        
            default:
                System.out.println("Opção de atualização inválida.");
                break;
        }
    }

    private void visualizarServicosEmAndamento() throws SQLException{
        System.out.println("\n--- SERVIÇOS EM ANDAMENTO ---");
        List<Servico> servicos = servicoService.listarServicosEmAndamento();
        if(servicos.isEmpty()) System.out.println("Nenhum serviço em andamento no momento.");
        else{
            for(Servico s : servicos){
                System.out.println(s);
            }
        }
    }

    private void visualizarMeusServicosConcluidos() throws SQLException {
        System.out.println("\n--- SERVIÇOS CONCLUÍDOS POR " + funcionarioLogado.getNome() + " ---");

        List<Servico> meuServicos = servicoService.buscarServicosPorFuncionario(funcionarioLogado.getId());
        List<Servico> concluidos = meuServicos.stream().filter(x -> "CONCLUIDO".equals(x.getStatus())).collect(Collectors.toList());
        
        if(concluidos.isEmpty()) System.out.println("Nenhum serviço em andamento no momento.");
        else{
            for(Servico s : concluidos){
                System.out.println(s);
            }
        }
    }
}
