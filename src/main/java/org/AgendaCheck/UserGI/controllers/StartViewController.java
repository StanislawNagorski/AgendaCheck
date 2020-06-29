package org.AgendaCheck.UserGI.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
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
import org.AgendaCheck.ReportGenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class StartViewController {

    private MainController mainController;
    ReportGenerator reportGenerator = new ReportGenerator();

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

    private double userInputSafeInCaseOfEmptyString() {
        String userTargetInput = userTarget.getText();
        if (userTargetInput.isBlank()) {
            return 1000;
        }
        return Double.parseDouble(userTargetInput);
    }


    private void sendDataToReportGenerator() {
        reportGenerator.setForecastFile(correctFiles[0]);
        reportGenerator.setScheduleFile(correctFiles[1]);
        reportGenerator.setProductivityTarget(userInputSafeInCaseOfEmptyString());
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    private void changeFrame() {
        fileDropPane.setStyle("-fx-border-color: rgba(3,158,211,1);" +
                " -fx-border-width: 2px; -fx-border-style: solid inside;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(3,158,211,1), 5, 0, 0, 0);");
    }

    @FXML
    void onDragExit() {
        fileDropPane.setStyle("-fx-border-color: black;");

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

    public void showAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Program nie wykrył plików");
        alert.setHeaderText("Sprawdź czy nazwa pliku spełnia poniższe wymagania: ");
        alert.setContentText("1. Plik Gessefa powinien w swojej nazwie mieć \"gessef\"\n" +
                "2. Raport WTMs może mieć niezmienioną nazwę lub zawierać np. \"godziny\" \n" +
                "3. Oba pliki powinny być w formacie .xlsx");
        alert.showAndWait();
    }

    private boolean areThereFiles(){
        return correctFiles[0] == null || correctFiles[1] == null;
    }

    @FXML
    void goToReport() {

        if (areThereFiles()) {
            showAlert();
        } else {
            sendDataToReportGenerator();
            loadChartScreen();
//            try {
//                reportGenerator.createFullReport();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (InvalidFormatException e) {
//                e.printStackTrace();
//            }

        }
    }


    @FXML
    void initialize() {

        userTarget.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,4}")) {
                    userTarget.setText(oldValue);
                }
            }
        });
    }


    private void loadChartScreen() {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/FXMLs/ReportView.fxml"));
        Pane pane = null;

        try {
            pane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ReportViewController reportViewController = loader.getController();
        reportViewController.setMainController(mainController);
        mainController.setScreen(pane);
    }
}