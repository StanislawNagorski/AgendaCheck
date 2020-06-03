package org.AgendaCheck;

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

}