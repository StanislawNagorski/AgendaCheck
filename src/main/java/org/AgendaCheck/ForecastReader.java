package org.AgendaCheck;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.*;

public class ForecastReader {

    private XSSFWorkbook forecast;
    private final int FORECAST_SHEET_SIZE = 450;
    private int numberOFDepartmentSheets = 0;

    public ForecastReader(XSSFWorkbook forecast) {
        this.forecast = forecast;
    }

    public int getNumberOFDepartmentSheets() {
        return numberOFDepartmentSheets;
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

                if (numericValueOfDate > monthEndsAt) {
                    break;
                }
            }
        }
        foreList.add(monthlyTurnOver);
        return foreList;
    }

    public List<Double> dailyTurnOverShare(List<Double> forecastTO) {
        List<Double> dailyShareToList = new ArrayList<>();

        for (int i = 0; i < forecastTO.size(); i++) {
            double shareValue = forecastTO.get(i) / forecastTO.get(forecastTO.size() - 1);
            dailyShareToList.add(shareValue);
        }

        return dailyShareToList;
    }

    private int calculateMonthColumnNr(int monthNumber) {
        return monthNumber + (monthNumber - 1);
    }

    private boolean isItRetailDepartmentSheetCheck(XSSFSheet sheet) {
        String desiredString = "Obrót 2020 PILOTAŻ";

        boolean condition1;
        XSSFCell cellShouldBeString = sheet.getRow(2).getCell(0);
        if (cellShouldBeString.getCellType().equals(CellType.STRING)) {
            condition1 = cellShouldBeString.getStringCellValue().equalsIgnoreCase(desiredString);
        } else {
            condition1 = false;
        }

        boolean condition2;
        XSSFCell cellNumericValueShouldBeGreaterThen0 = sheet.getRow(2).getCell(1);
        if (cellNumericValueShouldBeGreaterThen0.getCellType().equals(CellType.NUMERIC)) {
            condition2 = cellNumericValueShouldBeGreaterThen0.getNumericCellValue() > 0;
        } else if (cellNumericValueShouldBeGreaterThen0.getCellType().equals(CellType.FORMULA)) {
            double rawValue = Double.parseDouble(cellNumericValueShouldBeGreaterThen0.getRawValue());
            condition2 = rawValue > 0;
        }
        else {
            condition2 = false;
        }

        return condition1 && condition2;
    }

    public Map<String, Double> getDepartmentsMonthlyTurnOver(int monthNumber) {
        Map<String, Double> monthlyTurnOverByDepartment = new LinkedHashMap<>();

        int numberOfSheets = forecast.getNumberOfSheets();
        int monthColumnNr = calculateMonthColumnNr(monthNumber);
        int rowNr = 2;

        for (int i = 0; i < numberOfSheets; i++) {
            XSSFSheet forecastSheet = forecast.getSheetAt(i);

            if (isItRetailDepartmentSheetCheck(forecastSheet)) {
                String departmentName = forecastSheet.getSheetName();
                XSSFCell cell = forecastSheet.getRow(rowNr).getCell(monthColumnNr);
                double departmentForecastedTurnOver;

                if (cell.getCellType().equals(CellType.FORMULA)) {
                departmentForecastedTurnOver = Double.parseDouble(cell.getRawValue());
                } else {
                    departmentForecastedTurnOver = cell.getNumericCellValue();
                }

                monthlyTurnOverByDepartment.put(departmentName, departmentForecastedTurnOver);
                numberOFDepartmentSheets++;
            }

        }
        return monthlyTurnOverByDepartment;
    }

}
