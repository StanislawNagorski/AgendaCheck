package org.AgendaCheck;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.*;

public class ForecastReaderTest {

    private ForecastReader forecastReader643;
    private ForecastReader forecastReader729;


    @Before
    public void setUp() throws IOException {
        XSSFWorkbook forecast643 = new XSSFWorkbook(new FileInputStream("643_Gessef 2020.xlsx"));
        XSSFWorkbook forecast729 = new XSSFWorkbook(new FileInputStream("729 Gessef 2020.xlsx"));
        forecastReader643 = new ForecastReader(forecast643);
        forecastReader729 = new ForecastReader(forecast729);
    }

    @Test
    public void shouldReturnTrueForMayFor643(){
        //Given
        String givenName = "BieganieOXELO";
        Double givenTurnOver = 900000.0;
        int testedMonth = 5;

        //When
        Map<String, Double> resultMap = forecastReader643.getDepartmentsMonthlyTurnOver(testedMonth);

        //Then

        Assert.assertTrue(resultMap.containsKey(givenName));
        Assert.assertTrue(resultMap.containsValue(givenTurnOver));
        Assert.assertEquals(givenTurnOver, resultMap.get(givenName));
    }

    @Test
    public void shouldReturnTrueForJuneFor643(){
        //Given
        String givenName = "BieganieOXELO";
        Double givenTurnOver = 895000.0;
        int testedMonth = 6;

        //When
        Map<String, Double> resultMap = forecastReader643.getDepartmentsMonthlyTurnOver(testedMonth);

        //Then

        Assert.assertTrue(resultMap.containsKey(givenName));
        Assert.assertTrue(resultMap.containsValue(givenTurnOver));
        Assert.assertEquals(givenTurnOver, resultMap.get(givenName));
    }

    @Test
    public void shouldReturnTrueForMayFor729(){
        //Given
        String givenName = "NATURAOXELO";
        Double givenTurnOver = 660000.0;
        int testedMonth = 5;

        //When
        Map<String, Double> resultMap = forecastReader729.getDepartmentsMonthlyTurnOver(testedMonth);

        //Then

        Assert.assertTrue(resultMap.containsKey(givenName));
        Assert.assertTrue(resultMap.containsValue(givenTurnOver));
        Assert.assertEquals(givenTurnOver, resultMap.get(givenName));
    }

    @Test
    public void shouldReturnTrueForJuneFor729(){
        //Given
        String givenName = "NATURAOXELO";
        Double givenTurnOver = 622000.0;
        int testedMonth = 6;

        //When
        Map<String, Double> resultMap = forecastReader729.getDepartmentsMonthlyTurnOver(testedMonth);

        //Then

        Assert.assertTrue(resultMap.containsKey(givenName));
        Assert.assertTrue(resultMap.containsValue(givenTurnOver));
        Assert.assertEquals(givenTurnOver, resultMap.get(givenName));
    }

    @Test
    public void shouldReturnTrueForJulyFor729(){
        //Given
        String givenName = "NATURAOXELO";
        Double givenTurnOver = 650000.0;
        int testedMonth = 7;

        //When
        Map<String, Double> resultMap = forecastReader729.getDepartmentsMonthlyTurnOver(testedMonth);

        //Then

        Assert.assertTrue(resultMap.containsKey(givenName));
        Assert.assertTrue(resultMap.containsValue(givenTurnOver));
        Assert.assertEquals(givenTurnOver, resultMap.get(givenName));
    }


}