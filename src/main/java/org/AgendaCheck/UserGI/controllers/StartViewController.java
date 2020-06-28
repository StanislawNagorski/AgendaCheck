package org.AgendaCheck.UserGI.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyEvent;
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

    private File[] correctFiles = new File[2];

    public File getGessefFile(){
        return correctFiles[0];
    }

    public File getPlanQFile(){
        return correctFiles[1];
    }

    public int getProductivityTarget(){
        return Integer.parseInt(userTarget.getText());
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    private void changeFrame() {
        fileDropPane.setStyle("-fx-border-color: rgba(3,158,211,1); -fx-border-radius: 25;" +
                " -fx-border-width: 2px; -fx-border-style: solid inside;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(3,158,211,1), 5, 0, 0, 0);");
    }

    @FXML
    void onDragExit() {
        fileDropPane.setStyle("-fx-border-color: gray; -fx-border-radius: 25");
    }

    @FXML
    void onDragOver(DragEvent event) {
        changeFrame();
        if (event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.ANY);
        }
        event.consume();
    }

    @FXML
    void onDragDropped(DragEvent event) {
        List<File> dropedFiles = event.getDragboard().getFiles();
        System.out.println("Got " + dropedFiles.size() + " files");

        String[] popularNamesGessef = {"gessef", "gesef"};
        String[] popularNamesPlanQ = {"division", "sum", "report", "godziny", "hours"};

        for (File file : dropedFiles) {
            String fileName = file.getName();
            if (fileNameCheck(fileName, popularNamesGessef)) {
                changeFontIconAndText(gessefLabel, fileName);
                correctFiles[0] = file;
            }
            if (fileNameCheck(fileName, popularNamesPlanQ)) {
                changeFontIconAndText(planQLabel, fileName);
               correctFiles[1] = file;
            }
        }
        event.consume();
    }

    private void changeFontIconAndText(Label label, String fileName) {
        label.setTextFill(Color.GREEN);
        label.setFont(Font.font(gessefLabel.getFont().getFamily(), FontWeight.BOLD, gessefLabel.getFont().getSize()));
        ImageView imageView = null;
        try {
            Image image = new Image(new FileInputStream("src/main/resources/Graphics/greenIcon.png"));
            imageView = new ImageView(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        label.setText(fileName);
        label.setGraphic(imageView);
    }

    private boolean fileNameCheck(String fileName, String[] popularNames) {

        for (String popularName : popularNames) {
            if (fileName.toLowerCase().contains(popularName) && fileName.toLowerCase().contains(".xlsx")) {
                return true;
            }
        }
        return false;
    }

    @FXML
    void goToReport(){
        for (File inputFile : correctFiles) {
            System.out.println(inputFile.getName());
        }
    }

    @FXML
    void keyTyped(KeyEvent e){
        String typedChar = e.getCharacter();

        if (!Character.isDigit(typedChar.charAt(0))){
            e.consume();
        }
    }
    @FXML
    void initialize(){
        userTarget.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,4}")) {
                    userTarget.setText(oldValue);
                }
            }
        });
    }
}
