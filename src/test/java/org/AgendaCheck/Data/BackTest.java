package org.AgendaCheck.Data;

import org.AgendaCheck.Forecast.ForecastReader;
import org.AgendaCheck.ReportToXLSX.ReportWriter;
import org.AgendaCheck.Schedule.ScheduleReader;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BackTest {
    @Test
    public void shouldCreateReportForMayForJune() throws IOException, InvalidFormatException {

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

        report.write(new FileOutputStream("TestResults/Czerwiec643.xlsx"));
        report.close();
        File checkFile = new File("TestResults/Czerwiec643.xlsx");

        //Then
        Assert.assertTrue(checkFile.exists());

    }

    @Test
    public void shouldCreateReportForAugust() throws IOException, InvalidFormatException {

        //Given
        OPCPackage scheduleInput = OPCPackage.open(new File("SampleInput/godzinySierpień.xlsx"));
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

        report.write(new FileOutputStream("TestResults/Sierpień643.xlsx"));
        report.close();

        File checkFile = new File("TestResults/Sierpień643.xlsx");

        //Then
        Assert.assertTrue(checkFile.exists());


    }

    @Test
    public void shouldCreateReportForMayForStore729() throws IOException, InvalidFormatException {

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
        //When
        reportWriter.writeStoreSheet();
        reportWriter.writeAllDepartmentsSheets();

        report.write(new FileOutputStream("TestResults/Maj729.xlsx"));
        report.close();
        File checkFile = new File("TestResults/Maj729.xlsx");

        //Then
        Assert.assertTrue(checkFile.exists());

    }
}
