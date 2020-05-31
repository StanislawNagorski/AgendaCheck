package org.AgendaCheck;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;
import java.util.List;

public class ForecastReader {

    private XSSFWorkbook forecast;

    public ForecastReader(XSSFWorkbook forecast) {
        this.forecast = forecast;
    }

    public List<String> forecastTOList() {

        List<String> foreList = new ArrayList<>();
        XSSFSheet forecastSheet = forecast.getSheet("DZIEN DZIEN 2020");

        for (int i = 4; i < forecastSheet.getLastRowNum(); i++) {
            foreList.add(forecastSheet.getRow(i).getCell(5).getRawValue());
        }

        System.out.println(foreList);
        return foreList;
    }

}
