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

public class ReportGenerator {

    private File forecastFile;
    private File scheduleFile;
    private double productivityTarget;

    public void setScheduleFile(File scheduleFile) {
        this.scheduleFile = scheduleFile;
    }

    public void setForecastFile(File forecastFile) {
        this.forecastFile = forecastFile;
    }

    public void setProductivityTarget(double productivityTarget) {
        this.productivityTarget = productivityTarget;
    }

    public void createFullReport() throws IOException, InvalidFormatException {
        long start = System.nanoTime();

        OPCPackage forecastInput = OPCPackage.open(forecastFile);
        XSSFWorkbook forecast = new XSSFWorkbook(forecastInput);
        forecastInput.close();

        OPCPackage scheduleInput = OPCPackage.open(scheduleFile);
        XSSFWorkbook schedule = new XSSFWorkbook(scheduleInput);
        scheduleInput.close();



        XSSFWorkbook report = new XSSFWorkbook();
        ScheduleReader scheduleReader = new ScheduleReader(schedule);
        ForecastReader forecastReader = new ForecastReader(forecast);
        DataBank dataBank = new DataBank(scheduleReader, forecastReader, productivityTarget);
        ReportWriter reportWriter = new ReportWriter(report, dataBank);

        reportWriter.writeStoreSheet();
        reportWriter.writeAllDepartmentsSheets();

        report.write(new FileOutputStream("SampleOutputReport.xlsx"));
        report.close();

        long end = System.nanoTime();
        long duration = (end - start);
        double durationInSec = (double) duration / 1000000000;
        System.out.printf("Program ended in: %.4f seconds", durationInSec);
    }

}



