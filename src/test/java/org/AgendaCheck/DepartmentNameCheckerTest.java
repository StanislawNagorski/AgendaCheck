package org.AgendaCheck;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.util.List;
import java.util.Map;

public class DepartmentNameCheckerTest {

    @Test
    public void hoursSetShouldContainAllSheetsFromForecastFor643() throws Exception {
        //Given
        OPCPackage scheduleInput = OPCPackage.open(new File("SampleInputFiles/godzinyCzerwiec.xlsx"));
        XSSFWorkbook schedule = new XSSFWorkbook(scheduleInput);
        scheduleInput.close();

        OPCPackage forecastInput = OPCPackage.open(new File("SampleInputFiles/643_Gessef 2020.xlsx"));
        XSSFWorkbook forecast = new XSSFWorkbook(forecastInput);
        forecastInput.close();
        ScheduleReader scheduleReader = new ScheduleReader(schedule);
        ForecastReader forecastReader = new ForecastReader(forecast);
        DataBank dataBank = new DataBank(scheduleReader, forecastReader);

        Map<String, Double> turnover = dataBank.getMonthlyDepartmentTurnOver();
        Map<String, List<Double>> hours = dataBank.getDailyDepartmentHoursByName();

        //When
        DepartmentNameChecker.changeDepartmentNamesFromScheduleToThoseFromForecast(turnover, hours);

        //Then
        Assertions.assertTrue(hours.keySet().containsAll(turnover.keySet()));
    }

    @Test
    public void hoursSetShouldContainAllSheetsFromForecastFor729() throws Exception {
        //Given
        OPCPackage scheduleInput = OPCPackage.open(new File("SampleInputFiles/godzinyCzerwiec729.xlsx"));
        XSSFWorkbook schedule = new XSSFWorkbook(scheduleInput);
        scheduleInput.close();

        OPCPackage forecastInput = OPCPackage.open(new File("SampleInputFiles/729 Gessef 2020.xlsx"));
        XSSFWorkbook forecast = new XSSFWorkbook(forecastInput);
        forecastInput.close();
        ScheduleReader scheduleReader = new ScheduleReader(schedule);
        ForecastReader forecastReader = new ForecastReader(forecast);
        DataBank dataBank = new DataBank(scheduleReader, forecastReader);

        Map<String, Double> turnover = dataBank.getMonthlyDepartmentTurnOver();
        Map<String, List<Double>> hours = dataBank.getDailyDepartmentHoursByName();

        //When
        System.out.println(hours.keySet());
        DepartmentNameChecker.changeDepartmentNamesFromScheduleToThoseFromForecast(turnover, hours);

        //Then
        System.out.println(turnover.keySet());
        System.out.println(hours.keySet());

        Assertions.assertTrue(hours.keySet().containsAll(turnover.keySet()));
    }


}