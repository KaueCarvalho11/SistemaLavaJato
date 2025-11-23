package com.paintspray;

import atlantafx.base.theme.PrimerLight;
import com.paintspray.config.DatabaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Aplicação principal do Paint-Spray
 * Sistema de gestão para oficina de pintura de motos
 */
public class MainApplication extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;

        // Inicializa banco de dados
        DatabaseConnection.getInstance().getConnection();

        // Aplica tema AtlantaFX
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());

        // Carrega tela de login
        loadLoginScene();

        primaryStage.setTitle("Paint-Spray - Sistema de Gestão");
        primaryStage.setMinWidth(1280);
        primaryStage.setMinHeight(720);
        primaryStage.show();
    }

    /**
     * Carrega a tela de login
     */
    public static void loadLoginScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                MainApplication.class.getResource("/com/paintspray/fxml/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        // Adiciona CSS customizado
        scene.getStylesheets().add(
                MainApplication.class.getResource("/com/paintspray/css/styles.css").toExternalForm());

        primaryStage.setScene(scene);
    }

    /**
     * Carrega a tela principal (Dashboard)
     */
    public static void loadMainScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                MainApplication.class.getResource("/com/paintspray/fxml/main.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        // Adiciona CSS customizado
        scene.getStylesheets().add(
                MainApplication.class.getResource("/com/paintspray/css/styles.css").toExternalForm());

        primaryStage.setScene(scene);
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch();
    }
}
