package org.AgendaCheck;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class App 
{
    public static void main( String[] args ) throws IOException {

        long start = System.nanoTime();

        XSSFWorkbook schedule = new XSSFWorkbook(new FileInputStream("godzinyCzerwiec.xlsx"));
        XSSFWorkbook forecast = new XSSFWorkbook(new FileInputStream("643_Gessef 2020.xlsx"));

        XSSFWorkbook report = new XSSFWorkbook();

        ScheduleReader scheduleReader = new ScheduleReader(schedule);
        ForecastReader forecastReader = new ForecastReader(forecast);
        DataBank dataBank = new DataBank(scheduleReader, forecastReader);

        ReportWriter reportWriter = new ReportWriter(report, dataBank);
        sheetStoreCreation(reportWriter, "Sklep");

        //to do pętli z mapą
        sheetDepartmentCreation(new ReportWriter(report, dataBank), "BieganieOXELO", "(643.8) Bieganie");


        report.write(new FileOutputStream("SampleOutputReport.xlsx"));
        report.close();

        long end = System.nanoTime();
        long duration = (end-start);
        double durationInSec = (double) duration/1000000000;
        System.out.printf("Program ended in: %.4f seconds", durationInSec);
    }

    public static void sheetStoreCreation(ReportWriter reportWriter, String sheetName){

        reportWriter.setSheetName(sheetName);
        reportWriter.writeFirstColumnDays();
        reportWriter.writeForthColumnHours();
        reportWriter.writeFifthColumnHoursShare();
        reportWriter.writeSecondColumnTurnOverForecast();
        reportWriter.writeThirdColumnShareOfTurnOver();
        reportWriter.writeSixthColumnPerfectHours();
        reportWriter.writeSeventhColumnDifferenceInHours();
    }

    public static void sheetDepartmentCreation(ReportWriter reportWriter, String sheetName, String nameFromSchedule){
        reportWriter.writeDepartmentSheet(sheetName, nameFromSchedule);
    }


}
