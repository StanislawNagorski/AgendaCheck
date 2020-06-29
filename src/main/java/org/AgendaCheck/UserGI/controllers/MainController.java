package org.AgendaCheck.UserGI.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class MainController {

    @FXML
    private StackPane mainPane;

    @FXML
    public void initialize(){
        loadStartScreen();
    }

    public void loadStartScreen(){
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/FXMLs/StartView.fxml"));
        Pane pane = null;

        try {
            pane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        StartViewController startViewController = loader.getController();
        startViewController.setMainController(this);
        setScreen(pane);

    }

    public void setScreen(Pane pane){
        mainPane.getChildren().clear();
        mainPane.getChildren().add(pane);


    }

}
