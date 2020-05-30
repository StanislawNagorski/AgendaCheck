package org.AgendaCheck;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;
import java.util.List;

public class ScheduleReader {
    private XSSFWorkbook schedule;
    private XSSFSheet scheduleSheet;
    private XSSFWorkbook report;
    private XSSFSheet reportSheet;
    private List<Float> hoursSumByDay;
    private int scheduleRowsNumber;

    public ScheduleReader(XSSFWorkbook schedule, XSSFWorkbook report) {
        this.schedule = schedule;
        this.scheduleSheet = schedule.getSheetAt(0);
        this.report = report;
        this.reportSheet = report.getSheetAt(0);
        this.hoursSumByDay = new ArrayList<>();
        this.scheduleRowsNumber = scheduleSheet.getLastRowNum();
    }

    public void copyFirstRow() {

        for (int i = 0; i < scheduleRowsNumber; i++) {
            reportSheet.createRow(i).createCell(0).setCellValue(String.valueOf(scheduleSheet.getRow(i).getCell(0)));
        }

        reportSheet.autoSizeColumn(0);
    }

    public void sumDepartmentsHours() {

        for (int i = 3; i < scheduleRowsNumber; i++) {
            float daySum = 0;
            for (int j = 1; j < scheduleSheet.getRow(i).getLastCellNum(); j++) {
                daySum += scheduleSheet.getRow(i).getCell(j).getNumericCellValue();
            }
            hoursSumByDay.add(daySum);
        }
    }

    public void writeSumHours() {

        for (int i = 3; i < scheduleRowsNumber; i++) {
            reportSheet.getRow(i).createCell(1).setCellValue(hoursSumByDay.get(i - 3));
        }

        reportSheet.getRow(1).createCell(1).setCellValue("Suma godzin");
        reportSheet.autoSizeColumn(1);

    }


}
