package org.AgendaCheck.UserGI.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class StartViewController {

    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private Pane fileDropPane;
    @FXML
    private TextField userTarget;
    @FXML
    private Button goToChart;

    private void changeFrame(){
        fileDropPane.setStyle("-fx-border-color: rgba(3,158,211,1); -fx-border-radius: 25;" +
                " -fx-border-width: 2px; -fx-border-style: solid inside;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(3,158,211,1), 5, 0, 0, 0);");
    }

    @FXML
    void resetFrame(){
        fileDropPane.setStyle("-fx-border-color: gray; -fx-border-radius: 25");
    }

    @FXML
    void receiveFiles(){
      changeFrame();
    }

}
