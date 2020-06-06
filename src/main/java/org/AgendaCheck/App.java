package org.AgendaCheck;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException, InvalidFormatException {

        long start = System.nanoTime();

        OPCPackage scheduleInput = OPCPackage.open(new File("godzinyCzerwiec.xlsx"));
        XSSFWorkbook schedule = new XSSFWorkbook(scheduleInput);
        scheduleInput.close();

        OPCPackage forecastInput = OPCPackage.open(new File("643_Gessef 2020.xlsx"));
        XSSFWorkbook forecast = new XSSFWorkbook(forecastInput);
        forecastInput.close();


        XSSFWorkbook report = new XSSFWorkbook();
        ScheduleReader scheduleReader = new ScheduleReader(schedule);
        ForecastReader forecastReader = new ForecastReader(forecast);
        DataBank dataBank = new DataBank(scheduleReader, forecastReader);


        ReportWriter reportWriter = new ReportWriter(report, dataBank);

        XSSFSheet store = reportWriter.createReportSheet("Sklep");
        reportWriter.writeStoreSheet(store);

        XSSFSheet department = reportWriter.createReportSheet("BieganieOXELO");
        reportWriter.writeDepartmentSheet("BieganieOXELO", "(643.8) Bieganie", department);


        report.write(new FileOutputStream("SampleOutputReport.xlsx"));
        report.close();

        long end = System.nanoTime();
        long duration = (end - start);
        double durationInSec = (double) duration / 1000000000;
        System.out.printf("Program ended in: %.4f seconds", durationInSec);
    }

}



