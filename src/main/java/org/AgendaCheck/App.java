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
        ReportWriter reportWriter = new ReportWriter(report, scheduleReader, forecastReader, "Sklep");
        sheetCreation(reportWriter);

        int[] yearMonth = reportWriter.getYearMonth();
        System.out.println(forecastReader.getDepartmentsMonthlyTurnOver(yearMonth[1]));


        report.write(new FileOutputStream("RaportGrafików.xlsx"));
        report.close();

        long end = System.nanoTime();
        long duration = (end-start);
        double durationInSec = (double) duration/1000000000;
        System.out.printf("Program ended in: %.4f seconds", durationInSec);
    }

    public static void sheetCreation(ReportWriter reportWriter){

        reportWriter.writeFirstColumnDays();
        reportWriter.writeForthColumnHours();
        reportWriter.writeFifthColumnHoursShare();
        reportWriter.writeSecondColumnTurnOverForecast();
        reportWriter.writeThirdColumnShareOfTurnOver();
        reportWriter.writeSixthColumnPerfectHours();
        reportWriter.writeSeventhColumnDifferenceInHours();

    }


}
