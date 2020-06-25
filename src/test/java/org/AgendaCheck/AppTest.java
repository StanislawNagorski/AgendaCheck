package org.AgendaCheck;

import org.AgendaCheck.Data.DataBank;
import org.AgendaCheck.Forecast.ForecastReader;
import org.AgendaCheck.ReportToXLSX.ReportWriter;
import org.AgendaCheck.Schedule.ScheduleReader;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AppTest {

    @Before
    public void setUp() {
        String path = "TestResults";
        new File(path).mkdirs();
    }

    @Test
    public void shouldCreateReportForJune() throws IOException, InvalidFormatException {

        OPCPackage scheduleInput = OPCPackage.open(new File("src/main/SampleInputFiles/godzinyCzerwiec.xlsx"));
        XSSFWorkbook schedule = new XSSFWorkbook(scheduleInput);
        scheduleInput.close();

        OPCPackage forecastInput = OPCPackage.open(new File("src/main/SampleInputFiles/643_Gessef 2020.xlsx"));
        XSSFWorkbook forecast = new XSSFWorkbook(forecastInput);
        forecastInput.close();

        XSSFWorkbook report = new XSSFWorkbook();

        ScheduleReader scheduleReader = new ScheduleReader(schedule);
        ForecastReader forecastReader = new ForecastReader(forecast);
        double productivityTargetUserInput = 800.0;
        DataBank dataBank = new DataBank(scheduleReader, forecastReader, productivityTargetUserInput);
        ReportWriter reportWriter = new ReportWriter(report, dataBank);

        reportWriter.writeStoreSheet();
        reportWriter.writeAllDepartmentsSheets();

        report.write(new FileOutputStream("TestResults/Czerwiec643.xlsx"));
        report.close();
    }

    @Test
    public void shouldCreateReportForMay() throws IOException, InvalidFormatException {

        OPCPackage scheduleInput = OPCPackage.open(new File("src/main/SampleInputFiles/godzinyMaj.xlsx"));
        XSSFWorkbook schedule = new XSSFWorkbook(scheduleInput);
        scheduleInput.close();

        OPCPackage forecastInput = OPCPackage.open(new File("src/main/SampleInputFiles/643_Gessef 2020.xlsx"));
        XSSFWorkbook forecast = new XSSFWorkbook(forecastInput);
        forecastInput.close();

        XSSFWorkbook report = new XSSFWorkbook();

        ScheduleReader scheduleReader = new ScheduleReader(schedule);
        ForecastReader forecastReader = new ForecastReader(forecast);
        double productivityTargetUserInput = 800.0;
        DataBank dataBank = new DataBank(scheduleReader, forecastReader, productivityTargetUserInput);
        ReportWriter reportWriter = new ReportWriter(report, dataBank);

        reportWriter.writeStoreSheet();
        reportWriter.writeAllDepartmentsSheets();

        report.write(new FileOutputStream("TestResults/Maj643.xlsx"));
        report.close();
    }

    @Test
    public void shouldCreateReportForMayForStore729() throws IOException, InvalidFormatException {

        OPCPackage scheduleInput = OPCPackage.open(new File("src/main/SampleInputFiles/godzinyMaj729.xlsx"));
        XSSFWorkbook schedule = new XSSFWorkbook(scheduleInput);
        scheduleInput.close();

        OPCPackage forecastInput = OPCPackage.open(new File("src/main/SampleInputFiles/729 Gessef 2020.xlsx"));
        XSSFWorkbook forecast = new XSSFWorkbook(forecastInput);
        forecastInput.close();

        XSSFWorkbook report = new XSSFWorkbook();

        ScheduleReader scheduleReader = new ScheduleReader(schedule);
        ForecastReader forecastReader = new ForecastReader(forecast);
        double productivityTargetUserInput = 800.0;
        DataBank dataBank = new DataBank(scheduleReader, forecastReader, productivityTargetUserInput);
        ReportWriter reportWriter = new ReportWriter(report, dataBank);

        reportWriter.writeStoreSheet();
        reportWriter.writeAllDepartmentsSheets();

        report.write(new FileOutputStream("TestResults/Maj729.xlsx"));
        report.close();
    }

    @Test
    public void shouldCreateReportForJuneForStore729() throws IOException, InvalidFormatException {

        OPCPackage scheduleInput = OPCPackage.open(new File("src/main/SampleInputFiles/godzinyCzerwiec729.xlsx"));
        XSSFWorkbook schedule = new XSSFWorkbook(scheduleInput);
        scheduleInput.close();

        OPCPackage forecastInput = OPCPackage.open(new File("src/main/SampleInputFiles/729 Gessef 2020.xlsx"));
        XSSFWorkbook forecast = new XSSFWorkbook(forecastInput);
        forecastInput.close();

        XSSFWorkbook report = new XSSFWorkbook();

        ScheduleReader scheduleReader = new ScheduleReader(schedule);
        ForecastReader forecastReader = new ForecastReader(forecast);
        double productivityTargetUserInput = 800.0;
        DataBank dataBank = new DataBank(scheduleReader, forecastReader, productivityTargetUserInput);
        ReportWriter reportWriter = new ReportWriter(report, dataBank);

        reportWriter.writeStoreSheet();
        reportWriter.writeAllDepartmentsSheets();

        report.write(new FileOutputStream("TestResults/Czerwiec729.xlsx"));
        report.close();
    }

    @Test
    public void shouldCreateReportForJuneForStore1064() throws IOException, InvalidFormatException {

        OPCPackage scheduleInput = OPCPackage.open(new File("src/main/SampleInputFiles/godzinyCzerwiec1064.xlsx"));
        XSSFWorkbook schedule = new XSSFWorkbook(scheduleInput);
        scheduleInput.close();

        OPCPackage forecastInput = OPCPackage.open(new File("src/main/SampleInputFiles/1064 Gessef 2020.xlsx"));
        XSSFWorkbook forecast = new XSSFWorkbook(forecastInput);
        forecastInput.close();

        XSSFWorkbook report = new XSSFWorkbook();

        ScheduleReader scheduleReader = new ScheduleReader(schedule);
        ForecastReader forecastReader = new ForecastReader(forecast);
        double productivityTargetUserInput = 800.0;
        DataBank dataBank = new DataBank(scheduleReader, forecastReader, productivityTargetUserInput);
        ReportWriter reportWriter = new ReportWriter(report, dataBank);

        reportWriter.writeStoreSheet();
        reportWriter.writeAllDepartmentsSheets();

        report.write(new FileOutputStream("TestResults/Czerwiec1064.xlsx"));
        report.close();
    }

}