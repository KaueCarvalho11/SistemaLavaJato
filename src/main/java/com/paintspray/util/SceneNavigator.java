package com.paintspray.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

/**
 * Utilitário para navegação entre telas
 */
public class SceneNavigator {

    /**
     * Carrega um arquivo FXML e retorna o Parent
     */
    public static Parent loadFXML(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                SceneNavigator.class.getResource("/com/paintspray/fxml/" + fxmlPath));
        return loader.load();
    }

    /**
     * Carrega um arquivo FXML e retorna o loader (para acessar o controller)
     */
    public static FXMLLoader getFXMLLoader(String fxmlPath) {
        return new FXMLLoader(
                SceneNavigator.class.getResource("/com/paintspray/fxml/" + fxmlPath));
    }
}
