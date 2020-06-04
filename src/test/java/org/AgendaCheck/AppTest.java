package org.AgendaCheck;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class AppTest {

    @Before
    public void setUp() {
        String path = "TestResults";
        new File(path).mkdirs();
    }

    @Test
    public void shouldCreateReportForJune() throws IOException {

        XSSFWorkbook schedule = new XSSFWorkbook(new FileInputStream("godzinyCzerwiec.xlsx"));
        XSSFWorkbook forecast = new XSSFWorkbook(new FileInputStream("643_Gessef 2020.xlsx"));

        XSSFWorkbook report = new XSSFWorkbook();

        ScheduleReader scheduleReader = new ScheduleReader(schedule);
        ForecastReader forecastReader = new ForecastReader(forecast);
        DataBank dataBank = new DataBank(scheduleReader, forecastReader);
        ReportWriter reportWriter = new ReportWriter(report, dataBank);

        reportWriter.writeFirstColumnDays();
        reportWriter.writeForthColumnHours();
        reportWriter.writeFifthColumnHoursShare();
        reportWriter.writeSecondColumnTurnOverForecast();
        reportWriter.writeThirdColumnShareOfTurnOver();
        reportWriter.writeSixthColumnPerfectHours();
        reportWriter.writeSeventhColumnDifferenceInHours();


        report.write(new FileOutputStream("TestResults/Czerwiec643.xlsx"));
        report.close();
    }

    @Test
    public void shouldCreateReportForMay() throws IOException {

        XSSFWorkbook schedule = new XSSFWorkbook(new FileInputStream("godzinyMaj.xlsx"));
        XSSFWorkbook forecast = new XSSFWorkbook(new FileInputStream("643_Gessef 2020.xlsx"));

        XSSFWorkbook report = new XSSFWorkbook();

        ScheduleReader scheduleReader = new ScheduleReader(schedule);
        ForecastReader forecastReader = new ForecastReader(forecast);
        DataBank dataBank = new DataBank(scheduleReader, forecastReader);
        ReportWriter reportWriter = new ReportWriter(report, dataBank);

        reportWriter.writeFirstColumnDays();
        reportWriter.writeForthColumnHours();
        reportWriter.writeFifthColumnHoursShare();
        reportWriter.writeSecondColumnTurnOverForecast();
        reportWriter.writeThirdColumnShareOfTurnOver();
        reportWriter.writeSixthColumnPerfectHours();
        reportWriter.writeSeventhColumnDifferenceInHours();


        report.write(new FileOutputStream("TestResults/Maj643.xlsx"));
        report.close();
    }

    @Test
    public void shouldCreateReportForMayForStore729() throws IOException {

        XSSFWorkbook schedule = new XSSFWorkbook(new FileInputStream("godzinyMaj.xlsx"));
        XSSFWorkbook forecast = new XSSFWorkbook(new FileInputStream("729 Gessef 2020.xlsx"));

        XSSFWorkbook report = new XSSFWorkbook();

        ScheduleReader scheduleReader = new ScheduleReader(schedule);
        ForecastReader forecastReader = new ForecastReader(forecast);
        DataBank dataBank = new DataBank(scheduleReader, forecastReader);
        ReportWriter reportWriter = new ReportWriter(report, dataBank);

        reportWriter.writeFirstColumnDays();
        reportWriter.writeForthColumnHours();
        reportWriter.writeFifthColumnHoursShare();
        reportWriter.writeSecondColumnTurnOverForecast();
        reportWriter.writeThirdColumnShareOfTurnOver();
        reportWriter.writeSixthColumnPerfectHours();
        reportWriter.writeSeventhColumnDifferenceInHours();


        report.write(new FileOutputStream("TestResults/Maj729.xlsx"));
        report.close();
    }

    @Test
    public void shouldCreateReportForJuneForStore729() throws IOException {

        XSSFWorkbook schedule = new XSSFWorkbook(new FileInputStream("godzinyCzerwiec.xlsx"));
        XSSFWorkbook forecast = new XSSFWorkbook(new FileInputStream("729 Gessef 2020.xlsx"));

        XSSFWorkbook report = new XSSFWorkbook();

        ScheduleReader scheduleReader = new ScheduleReader(schedule);
        ForecastReader forecastReader = new ForecastReader(forecast);
        DataBank dataBank = new DataBank(scheduleReader, forecastReader);
        ReportWriter reportWriter = new ReportWriter(report, dataBank);

        reportWriter.writeFirstColumnDays();
        reportWriter.writeForthColumnHours();
        reportWriter.writeFifthColumnHoursShare();
        reportWriter.writeSecondColumnTurnOverForecast();
        reportWriter.writeThirdColumnShareOfTurnOver();
        reportWriter.writeSixthColumnPerfectHours();
        reportWriter.writeSeventhColumnDifferenceInHours();


        report.write(new FileOutputStream("TestResults/Czerwiec729.xlsx"));
        report.close();
    }



}