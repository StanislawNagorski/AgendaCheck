package org.AgendaCheck;


import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.*;

public class ScheduleReader {
    private final XSSFSheet scheduleSheet;
    private double monthlyHoursSum;

    public ScheduleReader(XSSFWorkbook schedule) {
        this.scheduleSheet = schedule.getSheetAt(0);
    }

    public List<String> getFirstColumn() {

        List<String> firstColumn = new ArrayList<>();

        for (int i = 3; i < scheduleSheet.getLastRowNum(); i++) {
            firstColumn.add(String.valueOf(scheduleSheet.getRow(i).getCell(0)));
        }

        firstColumn.add("Sumy:");
        return firstColumn;
    }

    public List<Double> sumDepartmentsHours() {
        List<Double> hoursSumByDay = new ArrayList<>();
        monthlyHoursSum = 0;

        for (int i = 3; i < scheduleSheet.getLastRowNum(); i++) {
            double daySum = 0;
            for (int j = 1; j < scheduleSheet.getRow(i).getLastCellNum(); j++) {
                daySum += scheduleSheet.getRow(i).getCell(j).getNumericCellValue();
            }
            hoursSumByDay.add(daySum);
            monthlyHoursSum += daySum;
        }
        hoursSumByDay.add(monthlyHoursSum);

        return hoursSumByDay;
    }

    public List<Double> calculatePercentagesOfHoursByDay() {
        List<Double> hoursSumByDay = sumDepartmentsHours();
        List<Double> percentagesOfHoursByDay = new LinkedList<>();

        for (Double hoursInDay : hoursSumByDay) {
            double dailyShare = hoursInDay / monthlyHoursSum;
            percentagesOfHoursByDay.add(dailyShare);
        }
        return percentagesOfHoursByDay;
    }

    private List<String> getListOfDepartmentNames() {
        List<String> listOfDepartmentNames = new ArrayList<>();
        int rowOnWhichDataStarts = 1;
        XSSFRow startRow = scheduleSheet.getRow(rowOnWhichDataStarts);

        int columnOnWhichDataStarts = 1;

        for (int i = columnOnWhichDataStarts; i < startRow.getLastCellNum(); i++) {
            if (!startRow.getCell(i).getStringCellValue().isBlank()) {
                listOfDepartmentNames.add(startRow.getCell(i).getStringCellValue());
            }
        }
        return listOfDepartmentNames;
    }

    private List<List<Double>> getListOfDailyHoursByDepartment() {
        List<List<Double>> dailyHoursByDepartment = new ArrayList<>();

        int rowOnWhichDataStarts = 3;
        int columnOnWhichDataStarts = 1;
        int rowLenght = scheduleSheet.getRow(1).getLastCellNum()-1;

        for (int i = columnOnWhichDataStarts; i < rowLenght; i++) {

            List<Double> departmentHoursByDay = new ArrayList<>();
            for (int j = rowOnWhichDataStarts; j < scheduleSheet.getLastRowNum(); j++) {
                departmentHoursByDay.add(scheduleSheet.getRow(j).getCell(i).getNumericCellValue());
            }
            dailyHoursByDepartment.add(departmentHoursByDay);
        }

        return dailyHoursByDepartment;
    }

    public Map<String, List<Double>> getMapOfScheduleDailyHoursByDepartment(){
        Map<String, List<Double>> dailyHoursMap = new HashMap<>();

        List<String> departmentNames = getListOfDepartmentNames();
        List<List<Double>> dailyScheduledHours = getListOfDailyHoursByDepartment();

        int departmentsCount = departmentNames.size();

        for (int i = 0; i < departmentsCount; i++) {
            dailyHoursMap.put(departmentNames.get(i), dailyScheduledHours.get(i));
        }

        return dailyHoursMap;
    }


}
