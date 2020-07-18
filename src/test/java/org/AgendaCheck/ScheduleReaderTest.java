package org.AgendaCheck;

import org.AgendaCheck.Schedule.ScheduleReader;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ScheduleReaderTest {
    private ScheduleReader scheduleReaderJune;
    private ScheduleReader scheduleReaderMay;

    @Before
    public void setUp() throws IOException {
        XSSFWorkbook scheduleJune = new XSSFWorkbook(new FileInputStream("SampleInput/godzinyCzerwiec.xlsx"));
        scheduleReaderJune = new ScheduleReader(scheduleJune);
        XSSFWorkbook scheduleMay = new XSSFWorkbook(new FileInputStream("SampleInput/godzinyMaj.xlsx"));
        scheduleReaderMay = new ScheduleReader(scheduleMay);
    }

    @Test
    public void shouldReturnTrueForGivenKeyForJune() {
        //Given
        String givenString = "(643.1) Rowery";

        //When
        Map<String, List<Double>> testedMap = scheduleReaderJune.createMapOfScheduleDailyHoursByDepartment();

        //Then
        Assert.assertTrue(testedMap.containsKey(givenString));
    }

    @Test
    public void shouldReturnTrueForGivenKeyForMay() {
        //Given
        String givenString = "(643.1) Rowery";

        //When
        Map<String, List<Double>> testedMap = scheduleReaderMay.createMapOfScheduleDailyHoursByDepartment();

        //Then
        Assert.assertTrue(testedMap.containsKey(givenString));
    }

    @Test
    public void shouldReturnTrueForGivenValueForJune() {
        //Given
        String givenString = "(643.1) Rowery";
        List<Double> givenList = Arrays.asList(50.6, 32.0, 31.0, 43.0, 62.0, 67.0,
                0.0, 41.0, 32.0, 42.0,
                0.0, 67.2, 62.0,
                0.0, 48.0, 33.0, 29.0, 44.0, 75.9, 52.0,
                0.0, 43.0, 40.0, 29.0, 35.8, 60.5, 66.0, 52.0, 25.0, 48.9, 1211.9);

        //When
        Map<String, List<Double>> testedMap = scheduleReaderJune.createMapOfScheduleDailyHoursByDepartment();

        //Then
        Assert.assertEquals(givenList, testedMap.get(givenString));
    }

    @Test
    public void shouldReturnTrueForGivenValueForMay() {
        //Given
        String givenString = "(643.1) Rowery";
        List<Double> givenList = Arrays.asList(0.0, 32.0, 0.0,
                29.0, 13.0, 31.0, 31.0, 27.0, 23.0,
                0.0, 25.0, 26.0, 30.0, 40.0, 47.0, 47.0,
                0.0, 57.0, 34.0, 35.0, 31.5, 56.0, 49.0,
                0.0, 39.0, 42.0, 38.0, 54.5, 65.0, 67.0, 0.0, 969.0);

        //When
        Map<String, List<Double>> testedMap = scheduleReaderMay.createMapOfScheduleDailyHoursByDepartment();

        //Then
        Assert.assertEquals(givenList, testedMap.get(givenString));
    }
}