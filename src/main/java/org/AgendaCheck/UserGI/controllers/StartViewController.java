package org.AgendaCheck.UserGI.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

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
    @FXML
    private Label gessefLabel;
    @FXML
    private Label planQLabel;

    private void changeFrame(){
        fileDropPane.setStyle("-fx-border-color: rgba(3,158,211,1); -fx-border-radius: 25;" +
                " -fx-border-width: 2px; -fx-border-style: solid inside;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(3,158,211,1), 5, 0, 0, 0);");
    }

    @FXML
    void onDragExit(){
        fileDropPane.setStyle("-fx-border-color: gray; -fx-border-radius: 25");
    }

    @FXML
    void onDragOver(DragEvent event){
      changeFrame();
        if (event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.ANY);
        }
        event.consume();
    }

    @FXML
    void onDragDropped(DragEvent event){
        List<File> files = event.getDragboard().getFiles();
        System.out.println("Got " + files.size() + " files");
        String[] popularNamesGessef = {"gessef", "gesef"};
        String[] popularNamesPlanQ = {"division", "sum", "report", "godziny", "hours"};
        for (File file : files) {
            String fileName = file.getName();
            if (fileNameCheck(fileName, popularNamesGessef)){
                setFontBoldGreenAndChangeIcon(gessefLabel);
            }
            if (fileNameCheck(fileName, popularNamesPlanQ)){
                setFontBoldGreenAndChangeIcon(planQLabel);
            }

        }

        event.consume();
    }

    private void setFontBoldGreenAndChangeIcon(Label label) {
        label.setTextFill(Color.GREEN);
        label.setFont(Font.font(gessefLabel.getFont().getFamily(), FontWeight.BOLD, gessefLabel.getFont().getSize()));
        ImageView imageView = null;
        try {
            Image image = new Image(new FileInputStream("src/main/resources/Graphics/greenIcon.png"));
             imageView = new ImageView(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        label.setGraphic(imageView);
    }

    private boolean fileNameCheck(String fileName, String[] popularNames ){

        for (String popularName : popularNames) {
            if (fileName.toLowerCase().contains(popularName) && fileName.toLowerCase().contains(".xlsx")){
                return true;
            }
        }
        return false;
    }



}
