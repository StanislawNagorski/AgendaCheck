package org.AgendaCheck;

import org.AgendaCheck.Data.DataBank;
import org.AgendaCheck.Forecast.ForecastReader;
import org.AgendaCheck.ReportToXLSX.ReportWriter;
import org.AgendaCheck.Schedule.ScheduleReader;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReportGenerator {

    private File forecastFile;
    private File scheduleFile;
    private double productivityTarget;
    private XSSFWorkbook report;
    private double durationInSec;

    public double getDurationInSec() {
        return durationInSec;
    }


    public void setScheduleFile(File scheduleFile) {
        this.scheduleFile = scheduleFile;
    }

    public void setForecastFile(File forecastFile) {
        this.forecastFile = forecastFile;
    }

    public void setProductivityTarget(double productivityTarget) {
        this.productivityTarget = productivityTarget;
    }

    public void generateFullReport() throws IOException, InvalidFormatException {
        long start = System.nanoTime();
        report = new XSSFWorkbook();

        OPCPackage forecastInput = OPCPackage.open(forecastFile);
        XSSFWorkbook forecast = new XSSFWorkbook(forecastInput);
        forecastInput.close();

        OPCPackage scheduleInput = OPCPackage.open(scheduleFile);
        XSSFWorkbook schedule = new XSSFWorkbook(scheduleInput);
        scheduleInput.close();

        ScheduleReader scheduleReader = new ScheduleReader(schedule);
        ForecastReader forecastReader = new ForecastReader(forecast);
        DataBank dataBank = new DataBank(scheduleReader, forecastReader, productivityTarget);

        ReportWriter reportWriter = new ReportWriter(report, dataBank);

        reportWriter.writeStoreSheet();
        reportWriter.writeAllDepartmentsSheets();

        long end = System.nanoTime();
        long duration = (end - start);
        durationInSec = (double) duration / 1000000000;
        System.out.printf("Program ended in: %.4f seconds", durationInSec);
    }

    public void writeFullReport(String path) throws IOException {
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter dt = DateTimeFormatter.ofPattern("uuuu-MM-dd HH.mm.ss");

        String storeNumber = forecastFile.getName().substring(0,4);
        String fileName = "\\" + storeNumber + " Raport z " + date.format(dt) +".xlsx";

        report.write(new FileOutputStream(path+fileName));
        report.close();
    }

}



