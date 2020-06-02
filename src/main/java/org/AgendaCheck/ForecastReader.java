package org.AgendaCheck;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;
import java.util.List;

public class ForecastReader {

    private XSSFWorkbook forecast;
    private final int FORECAST_SHEET_SIZE = 450;

    public ForecastReader(XSSFWorkbook forecast) {
        this.forecast = forecast;
    }

    public List<Double> forecastTOList(int[] range) {

        List<Double> foreList = new ArrayList<>();
        double monthlyTurnOver = 0;
        XSSFSheet forecastSheet = forecast.getSheet("DZIEN DZIEN 2020");

        int dateColumnNr = 3;
        int readiedColumnNr = 5;
        int dataStartRow = 4;

        int monthStartsAt = range[0];
        int monthEndsAt = range[1];

        for (int i = 0; i < FORECAST_SHEET_SIZE - 5; i++) {

            if ((forecastSheet.getRow(i + dataStartRow).getCell(dateColumnNr).getCellType() == CellType.NUMERIC) ||
                    (forecastSheet.getRow(i + dataStartRow).getCell(dateColumnNr).getCellType() == CellType.FORMULA)) {

                int numericValueOfDate = (int) forecastSheet.getRow(i + dataStartRow)
                        .getCell(dateColumnNr).getNumericCellValue();

                if (numericValueOfDate >= monthStartsAt && numericValueOfDate <= monthEndsAt) {
                    double dayTO = forecastSheet.getRow(i + dataStartRow)
                            .getCell(readiedColumnNr).getNumericCellValue();

                    monthlyTurnOver += dayTO;
                    foreList.add(dayTO);
                }

                if (numericValueOfDate > monthEndsAt){
                    break;
                }
            }
        }
        foreList.add(monthlyTurnOver);
        return foreList;
    }

    public List<Double> dailyTurnOverShare (List<Double> forecastTO) {
        List<Double> dailyShareToList = new ArrayList<>();

        for (int i = 0; i < forecastTO.size(); i++) {
            double shareValue = forecastTO.get(i)/forecastTO.get(forecastTO.size()-1);
            dailyShareToList.add(shareValue);
        }

        return dailyShareToList;
    }

}
