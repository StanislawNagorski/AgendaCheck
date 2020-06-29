package org.AgendaCheck.UserGI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class LunchApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/FXMLs/MainView.fxml"));
        StackPane stackPane = loader.load();
        Scene scene = new Scene(stackPane,600,350);
        stage.setTitle("eStah ;)");
        stage.setScene(scene);
        stage.show();
    }
}
