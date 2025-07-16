package com.ufersa.sistemalavajato;

import com.ufersa.sistemalavajato.service.ClienteService;
import com.ufersa.sistemalavajato.service.FuncionarioService;
import com.ufersa.sistemalavajato.auth.AuthService;
import com.ufersa.sistemalavajato.auth.UserSession;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.InputMismatchException;

public class Program {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        ClienteService clienteService = new ClienteService();
        FuncionarioService funcionarioService = new FuncionarioService();
        AuthService authService = new AuthService();
        UserSession userSession = UserSession.getInstance();

        while (true) {
            System.out
                    .println("(1) - Cadastrar cliente\n(2) - Cadastrar Funcionario\n(3) - Realizar login\n(0) - Sair");
            int opcao;

            try {
                opcao = input.nextInt();
                input.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Digite um número válido");
                input.nextLine();
                continue;
            }

            try {
                switch (opcao) {
                    case 1:
                        System.out.print("ID: ");
                        String ClienteId = input.nextLine();

                        System.out.print("Nome: ");
                        String ClienteNome = input.nextLine();

                        System.out.print("Email: ");
                        String ClienteEmail = input.nextLine();

                        System.out.print("Senha: ");
                        String ClienteSenha = input.nextLine();

                        System.out.print("Endereço: ");
                        String endereco = input.nextLine();

                        System.out.print("Numero de telefone: ");
                        String numeroTelefone = input.nextLine();

                        clienteService.cadastrarCliente(ClienteId, ClienteNome, ClienteEmail, ClienteSenha, endereco,
                                numeroTelefone);
                        

                        System.out.print("Cliente cadastrado.");
                        break;

                    case 2:
                        System.out.print("ID: ");
                        String funcionarioId = input.nextLine();

                        System.out.println("Nome ");
                        String funcionarioNome = input.nextLine();

                        System.out.println("Email: ");
                        String funcionarioEmail = input.nextLine();

                        System.out.println("Senha: ");
                        String funcionarioSenha = input.nextLine();

                        funcionarioService.cadastrarFuncionario(funcionarioId, funcionarioNome, funcionarioEmail,
                                funcionarioSenha);

                        System.out.print("Funcionário cadastrado.");
                        break;

                    case 3:
                        System.out.print("Email: ");
                        String loginEmail = input.nextLine();

                        System.out.print("Senha: ");
                        String loginSenha = input.nextLine();

                        if (authService.login(loginEmail, loginSenha)) {
                            System.out.println("Login realizado com sucesso!");
                            System.out.println("Bem-vindo, " + userSession.getCurrentUser().getNome() + "!");
                            System.out.println("Tipo de usuário: " + userSession.getUserType());
                        } else {
                            System.out.println("Email ou senha incorretos. Tente novamente.");
                        }
                        break;

                    case 0:
                        System.out.print("Encerrado");
                        input.close();
                        return;

                    default:
                        System.out.println("Opção inválida. Tente novamente");
                }
            } catch (SQLException e) {
                System.out.println("Dados inválidos.");
            } catch (IllegalArgumentException e) {
                System.out.println("Não foi possível acessar o banco de dados");
            }
        }
    }
}
