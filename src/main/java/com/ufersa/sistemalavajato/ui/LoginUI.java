package com.ufersa.sistemalavajato.ui;

import com.ufersa.sistemalavajato.model.Funcionario;
import com.ufersa.sistemalavajato.service.FuncionarioService;
import java.sql.SQLException;
import java.util.Scanner;

public class LoginUI {

    private final FuncionarioService funcionarioService;
    private final Scanner sc;

    public LoginUI() {
        this.funcionarioService = new FuncionarioService();
        this.sc = new Scanner(System.in);
    }

    public Funcionario exibirLoginDeFuncionario() {
        System.out.println("\n--- LOGIN DE FUNCIONÁRIO ---");
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Senha: ");
        String senha = sc.nextLine();

        try {
            Funcionario funcionario = funcionarioService.realizarLogin(email, senha);

            if (funcionario != null) {
                System.out.println("\nLogin realizado com sucesso! Bem-vindo(a), " + funcionario.getNome() + "!");
                return funcionario;
            } else {
                System.out.println("\nEmail ou senha incorretos. Tente novamente.");
                return null;
            }
        } catch (SQLException e) {
            System.err.println("Erro de banco de dados ao tentar fazer login: " + e.getMessage());
            return null;
        }
    }
}