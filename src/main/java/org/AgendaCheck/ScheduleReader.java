package org.AgendaCheck;


import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;
import java.util.List;

public class ScheduleReader {
    private XSSFSheet scheduleSheet;
    private List<Float> hoursSumByDay;
    private int scheduleRowsNumber;
    private float monthlyHoursSum;
    private List<String> firstColumn;

    public ScheduleReader(XSSFWorkbook schedule) {
        this.scheduleSheet = schedule.getSheetAt(0);
        this.hoursSumByDay = new ArrayList<>();
        this.scheduleRowsNumber = scheduleSheet.getLastRowNum();
        firstColumn = new ArrayList<>();
    }

    public List<String> getFirstColumn(){
        return firstColumn;
    }

    public List<Float> getHoursSumByDay(){
        return hoursSumByDay;
    }

    public int getScheduleRowsNumber(){
        return scheduleRowsNumber;
    }

    public float getMonthlyHoursSum(){
        return monthlyHoursSum;
    }

    public void copyFirstRow() {
        for (int i = 0; i < scheduleRowsNumber; i++) {
            firstColumn.add(String.valueOf(scheduleSheet.getRow(i).getCell(0)));
        }
    }

    public void sumDepartmentsHours() {

        for (int i = 3; i < scheduleRowsNumber; i++) {
            float daySum = 0;
            for (int j = 1; j < scheduleSheet.getRow(i).getLastCellNum(); j++) {
                daySum += scheduleSheet.getRow(i).getCell(j).getNumericCellValue();
            }
            hoursSumByDay.add(daySum);
            monthlyHoursSum += daySum;
        }
    }




}
