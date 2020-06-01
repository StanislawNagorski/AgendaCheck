package org.AgendaCheck;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;
import java.util.List;

public class ForecastReader {

    private XSSFWorkbook forecast;
    private int FORECAST_SHEET_SIZE = 450;

    public ForecastReader(XSSFWorkbook forecast) {
        this.forecast = forecast;
    }

    public List<String> forecastTOList() {

        List<String> foreList = new ArrayList<>();
        List<Double> dateTestList = new ArrayList<>();
        XSSFSheet forecastSheet = forecast.getSheet("DZIEN DZIEN 2020");

        System.out.println(forecastSheet.getLastRowNum());

        for (int i = 0; i < FORECAST_SHEET_SIZE-5; i++) {
            System.out.println(i);


            if (forecastSheet.getRow(i+4).getCell(3).getCellType()== CellType.NUMERIC ||
                    forecastSheet.getRow(i+4).getCell(3).getCellType()== CellType.FORMULA){
                foreList.add(forecastSheet.getRow(i+4).getCell(5).getRawValue());
            }


        }
        System.out.println(foreList);
             return foreList;
    }



}
