package org.AgendaCheck;


import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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

}
