package org.AgendaCheck.UserGI.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;
import org.AgendaCheck.ReportGenerator;

import java.io.File;
import java.io.IOException;

public class ReportViewController {

    private MainController mainController;
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    private ReportGenerator reportGenerator;

    public void setReportGenerator(ReportGenerator reportGenerator) {
        this.reportGenerator = reportGenerator;
    }

    @FXML
    Pane reportViewMainPane;
    @FXML
    Button reportButton;
    @FXML
    BarChart barChart;
    @FXML
    CategoryAxis barCategoryX;
    @FXML
    NumberAxis barNumberY;


    @FXML
    void initialize(){
        createStoreChart();
    }

    private void createStoreChart(){

    }


    @FXML
    void actionOnReportButton(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        Window window = reportViewMainPane.getScene().getWindow();

        File file = directoryChooser.showDialog(window);
        String pathToWrite = file.getAbsolutePath();

        if (file != null){
            try {
                reportGenerator.writeFullReport(pathToWrite);
                succesWriteMessage(pathToWrite);
                goBackToStartView();
            } catch (IOException e) {
                showAlert();
                e.printStackTrace();
            }
        }
    }

    private void succesWriteMessage(String path) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Zrobione!");

        double programExecusioneTime = reportGenerator.getDurationInSec();
        String executionTimeString = String.format("%.2f", programExecusioneTime);

        alert.setHeaderText("Raport wygenerowany w: " + executionTimeString + " sekund");
        alert.setContentText("Zapisano w: " + path +
                "\nMiłego dnia :)");
        alert.showAndWait();
    }

    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Błąd zapisu");
        alert.setHeaderText("Program nie może zapisać pliku w tym miejscu ");
        alert.setContentText("Sprawdź czy masz uprawnienia do zapisu w tym katalogu");
        alert.showAndWait();
    }

    private void goBackToStartView(){
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/FXMLs/StartView.fxml"));
        Pane pane = null;

        try {
            pane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        StartViewController startViewController = loader.getController();
        startViewController.setMainController(mainController);
        mainController.setScreen(pane);
    }
}
