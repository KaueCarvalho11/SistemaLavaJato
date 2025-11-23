package com.paintspray.controller;

import com.paintspray.MainApplication;
import com.paintspray.model.Usuario;
import com.paintspray.service.UsuarioService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Controller da tela de Login
 */
public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField senhaField;

    @FXML
    private Button loginButton;

    private final UsuarioService usuarioService = new UsuarioService();

    @FXML
    private void initialize() {
        // Configura ação do Enter nos campos
        emailField.setOnAction(event -> handleLogin());
        senhaField.setOnAction(event -> handleLogin());
    }

    @FXML
    private void handleLogin() {
        String email = emailField.getText().trim();
        String senha = senhaField.getText();

        // Validação básica
        if (email.isEmpty() || senha.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Campos obrigatórios",
                    "Por favor, preencha email e senha.");
            return;
        }

        try {
            Usuario usuario = usuarioService.realizarLogin(email, senha);

            // Armazena usuário logado na sessão
            SessionManager.setUsuarioLogado(usuario);

            // Navega para tela principal
            MainApplication.loadMainScene();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erro de Login",
                    "Email ou senha incorretos.");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erro",
                    "Erro ao carregar tela principal: " + e.getMessage());
        }
    }

    @FXML
    private void handleCadastro() {
        try {
            // Cria usuário admin automaticamente
            usuarioService.cadastrarUsuario("1", "Administrador", "admin", "admin");

            showAlert(Alert.AlertType.INFORMATION, "Sucesso",
                    "Usuário admin criado com sucesso!\n\n" +
                            "Email: admin\n" +
                            "Senha: admin\n\n" +
                            "Use essas credenciais para fazer login.");

            // Preenche os campos automaticamente
            emailField.setText("admin");
            senhaField.setText("admin");
            emailField.requestFocus();

        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed") ||
                    e.getMessage().contains("Já existe")) {
                showAlert(Alert.AlertType.WARNING, "Usuário já existe",
                        "O usuário admin já foi criado anteriormente.\n\n" +
                                "Use as credenciais:\n" +
                                "Email: admin\n" +
                                "Senha: admin");

                // Preenche os campos automaticamente
                emailField.setText("admin");
                senhaField.setText("admin");
                emailField.requestFocus();
            } else {
                showAlert(Alert.AlertType.ERROR, "Erro",
                        "Erro ao criar usuário: " + e.getMessage());
            }
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
