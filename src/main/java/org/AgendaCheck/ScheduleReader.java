package org.AgendaCheck;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;
import java.util.List;

public class ScheduleReader {
    private XSSFWorkbook schedule;
    private XSSFSheet scheduleSheet;
    private XSSFWorkbook report;
    private XSSFSheet reportSheet;
    private DataFormat dataFormat;
    private CellStyle defaultFloatCellStyle;
    private CellStyle boldedFloatWithTopBorder;
    private XSSFFont boldFont;
    private List<Float> hoursSumByDay;
    private int scheduleRowsNumber;
    private float monthlyHoursSum;

    public ScheduleReader(XSSFWorkbook schedule, XSSFWorkbook report) {
        this.schedule = schedule;
        this.scheduleSheet = schedule.getSheetAt(0);
        this.report = report;
        this.reportSheet = report.getSheetAt(0);
        this.dataFormat = report.createDataFormat();
        this.defaultFloatCellStyle = report.createCellStyle();
        defaultFloatCellStyle.setDataFormat(dataFormat.getFormat("#.##"));
        this.boldFont = report.createFont();
        boldFont.setBold(true);
        this.boldedFloatWithTopBorder = report.createCellStyle();
        boldedFloatWithTopBorder.setDataFormat(dataFormat.getFormat("#.##"));
        boldedFloatWithTopBorder.setFont(boldFont);
        boldedFloatWithTopBorder.setBorderTop(BorderStyle.MEDIUM);
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
            monthlyHoursSum += daySum;
        }
    }

    public void writeSumHours() {

        for (int i = 3; i < scheduleRowsNumber; i++) {
            reportSheet.getRow(i).createCell(1).setCellValue(hoursSumByDay.get(i - 3));
            reportSheet.getRow(i).getCell(1).setCellStyle(defaultFloatCellStyle);
        }

        reportSheet.getRow(1).createCell(1).setCellValue("Suma godzin");
        reportSheet.createRow(scheduleRowsNumber).createCell(1).setCellValue(monthlyHoursSum);
        reportSheet.getRow(scheduleRowsNumber).getCell(1).setCellStyle(boldedFloatWithTopBorder);

        reportSheet.autoSizeColumn(1);

    }


}
