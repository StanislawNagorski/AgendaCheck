package org.AgendaCheck.Data;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class DepartmentNameCheckerTest {

    @Test
    public void shouldReturnTrueForSameNames(){
        //Given
        String givenNameForecast = "X";
        String givenNameSchedule = "X";
        //When
        boolean result = DepartmentNameChecker.namesCheck(givenNameForecast,givenNameSchedule);

        //Then
        Assertions.assertTrue(result);
    }

    @Test
    public void shouldReturnFalseForDiffrentNames(){
        //Given
        String givenNameForecast = "OdjechanaNazwa";
        String givenNameSchedule = "X";
        //When
        boolean result = DepartmentNameChecker.namesCheck(givenNameForecast,givenNameSchedule);

        //Then
        Assertions.assertFalse(result);
    }

    @Test
    public void shouldReturnTrueIfNamesAreSimilar(){
        //Given
        String givenNameForecast = "Rowery";
        String givenNameSchedule = "(643.1) Rowery";
        //When
        boolean result = DepartmentNameChecker.namesCheck(givenNameForecast,givenNameSchedule);

        //Then
        Assertions.assertTrue(result);
    }

    @Test
    public void shouldReturnFalseIfNamesAreDiffrent(){
        //Given
        String givenNameForecast = "BieganieOXELO";
        String givenNameSchedule = "(643.1) Rowery";
        //When
        boolean result = DepartmentNameChecker.namesCheck(givenNameForecast,givenNameSchedule);

        //Then
        Assertions.assertFalse(result);
    }

    @Test
    public void shouldReturnTrueIfNamesHaveSameElement(){
        //Given
        String givenNameForecast = "BieganieOXELO";
        String givenNameSchedule = "(643.8) Bieganie";
        //When
        boolean result = DepartmentNameChecker.namesCheck(givenNameForecast,givenNameSchedule);

        //Then
        Assertions.assertTrue(result);
    }

    @Test
    public void shouldReturnTrueIfNamesHaveSameElementCompicated(){
        //Given
        String givenNameForecast = "Tetris - Gry/Rakiety";
        String givenNameSchedule = "(643.20) Gry/Tenis";
        //When
        boolean result = DepartmentNameChecker.namesCheck(givenNameForecast,givenNameSchedule);

        //Then
        Assertions.assertTrue(result);
    }

//
//    @Test
//    public void hoursSetShouldContainAllSheetsFromForecastFor729() throws Exception {
//        //Given
//        OPCPackage scheduleInput = OPCPackage.open(new File("SampleInput/godzinyCzerwiec729.xlsx"));
//        XSSFWorkbook schedule = new XSSFWorkbook(scheduleInput);
//        scheduleInput.close();
//
//        OPCPackage forecastInput = OPCPackage.open(new File("SampleInput/729 Gessef 2020.xlsx"));
//        XSSFWorkbook forecast = new XSSFWorkbook(forecastInput);
//        forecastInput.close();
//        ScheduleReader scheduleReader = new ScheduleReader(schedule);
//        ForecastReader forecastReader = new ForecastReader(forecast);
//        double productivityTargetUserInput = 800;
//        DataBank dataBank = new DataBank(scheduleReader, forecastReader, productivityTargetUserInput);
//
//        Map<String, Double> turnover = dataBank.getMonthlyDepartmentTurnOver();
//        Map<String, List<Double>> hours = dataBank.getDailyDepartmentHoursByName();
//
//        //When
//        System.out.println(hours.keySet());
//        DepartmentNameChecker.changeDepartmentNamesInScheduleToThoseFromForecast(turnover, hours);
//
//        //Then
//        System.out.println(turnover.keySet());
//        System.out.println(hours.keySet());
//
//        Assertions.assertTrue(hours.keySet().containsAll(turnover.keySet()));
//    }


}