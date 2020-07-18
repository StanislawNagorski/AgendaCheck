package org.AgendaCheck;

import org.AgendaCheck.Data.DataBank;
import org.AgendaCheck.Forecast.ForecastReader;
import org.AgendaCheck.ReportToXLSX.ReportWriter;
import org.AgendaCheck.Schedule.ScheduleReader;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
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
        //Given
        OPCPackage scheduleInput = OPCPackage.open(new File("SampleInput/godzinyCzerwiec.xlsx"));
        XSSFWorkbook schedule = new XSSFWorkbook(scheduleInput);
        scheduleInput.close();

        OPCPackage forecastInput = OPCPackage.open(new File("SampleInput/643_Gessef 2020.xlsx"));
        XSSFWorkbook forecast = new XSSFWorkbook(forecastInput);
        forecastInput.close();

        XSSFWorkbook report = new XSSFWorkbook();

        ScheduleReader scheduleReader = new ScheduleReader(schedule);
        ForecastReader forecastReader = new ForecastReader(forecast);
        double productivityTargetUserInput = 800.0;
        DataBank dataBank = new DataBank(scheduleReader, forecastReader, productivityTargetUserInput);
        ReportWriter reportWriter = new ReportWriter(report, dataBank);

        //When
        reportWriter.writeStoreSheet();
        reportWriter.writeAllDepartmentsSheets();

        FileOutputStream fileOutputStream = new FileOutputStream("TestResults/Czerwiec643.xlsx");
        report.write(fileOutputStream);
        report.close();
        File checkFile = new File("TestResults/Czerwiec643.xlsx");

        //Then
        Assert.assertTrue(checkFile.exists());
    }

    @Test
    public void shouldCreateReportForMay() throws IOException, InvalidFormatException {

        //Given
        OPCPackage scheduleInput = OPCPackage.open(new File("SampleInput/godzinyMaj.xlsx"));
        XSSFWorkbook schedule = new XSSFWorkbook(scheduleInput);
        scheduleInput.close();

        OPCPackage forecastInput = OPCPackage.open(new File("SampleInput/643_Gessef 2020.xlsx"));
        XSSFWorkbook forecast = new XSSFWorkbook(forecastInput);
        forecastInput.close();

        XSSFWorkbook report = new XSSFWorkbook();

        ScheduleReader scheduleReader = new ScheduleReader(schedule);
        ForecastReader forecastReader = new ForecastReader(forecast);
        double productivityTargetUserInput = 800.0;
        DataBank dataBank = new DataBank(scheduleReader, forecastReader, productivityTargetUserInput);
        ReportWriter reportWriter = new ReportWriter(report, dataBank);
        //When
        reportWriter.writeStoreSheet();
        reportWriter.writeAllDepartmentsSheets();

        report.write(new FileOutputStream("TestResults/Maj643.xlsx"));
        report.close();

        File checkFile = new File("TestResults/Maj643.xlsx");

        //Then
        Assert.assertTrue(checkFile.exists());


    }

    @Test
    public void shouldCreateReportForMayForStore729() throws IOException, InvalidFormatException {

        //Given
        OPCPackage scheduleInput = OPCPackage.open(new File("SampleInput/godzinyMaj729.xlsx"));
        XSSFWorkbook schedule = new XSSFWorkbook(scheduleInput);
        scheduleInput.close();

        OPCPackage forecastInput = OPCPackage.open(new File("SampleInput/729 Gessef 2020.xlsx"));
        XSSFWorkbook forecast = new XSSFWorkbook(forecastInput);
        forecastInput.close();

        XSSFWorkbook report = new XSSFWorkbook();

        ScheduleReader scheduleReader = new ScheduleReader(schedule);
        ForecastReader forecastReader = new ForecastReader(forecast);
        double productivityTargetUserInput = 800.0;
        DataBank dataBank = new DataBank(scheduleReader, forecastReader, productivityTargetUserInput);
        ReportWriter reportWriter = new ReportWriter(report, dataBank);
        //When
        reportWriter.writeStoreSheet();
        reportWriter.writeAllDepartmentsSheets();

        report.write(new FileOutputStream("TestResults/Maj729.xlsx"));
        report.close();
        File checkFile = new File("TestResults/Maj729.xlsx");

        //Then
        Assert.assertTrue(checkFile.exists());

    }

    @Test
    public void shouldCreateReportForJuneForStore729() throws IOException, InvalidFormatException {
        //Given
        OPCPackage scheduleInput = OPCPackage.open(new File("SampleInput/godzinyCzerwiec729.xlsx"));
        XSSFWorkbook schedule = new XSSFWorkbook(scheduleInput);
        scheduleInput.close();

        OPCPackage forecastInput = OPCPackage.open(new File("SampleInput/729 Gessef 2020.xlsx"));
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
        //When
        report.write(new FileOutputStream("TestResults/Czerwiec729.xlsx"));
        report.close();

        File checkFile = new File("TestResults/Czerwiec729.xlsx");

        //Then
        Assert.assertTrue(checkFile.exists());
    }

    @Test
    public void shouldCreateReportForJuneForStore1064() throws IOException, InvalidFormatException {
        //Given
        OPCPackage scheduleInput = OPCPackage.open(new File("SampleInput/godzinyCzerwiec1064.xlsx"));
        XSSFWorkbook schedule = new XSSFWorkbook(scheduleInput);
        scheduleInput.close();

        OPCPackage forecastInput = OPCPackage.open(new File("SampleInput/1064 Gessef 2020.xlsx"));
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

        //When
        report.write(new FileOutputStream("TestResults/Czerwiec1064.xlsx"));
        report.close();

        File checkFile = new File("TestResults/Czerwiec1064.xlsx");

        //Then
        Assert.assertTrue(checkFile.exists());

    }

}