package org.AgendaCheck;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReportWriter {
    private ScheduleReader scheduleReader;
    private XSSFSheet reportSheet;
    private CellStyle defaultFloatCellStyle;
    private CellStyle boldedFloatWithTopBorder;
    private CellStyle percentageStyle;

    public ReportWriter(XSSFWorkbook report, ScheduleReader scheduleReader) {
        this.scheduleReader = scheduleReader;
        this.reportSheet = report.createSheet("Raport");
        DataFormat dataFormat = report.createDataFormat();

        this.defaultFloatCellStyle = report.createCellStyle();
        defaultFloatCellStyle.setDataFormat(dataFormat.getFormat("#.###"));

        this.boldedFloatWithTopBorder = report.createCellStyle();
        boldedFloatWithTopBorder.setDataFormat(dataFormat.getFormat("#.##"));
        XSSFFont boldFont = report.createFont();
        boldFont.setBold(true);
        boldedFloatWithTopBorder.setFont(boldFont);
        boldedFloatWithTopBorder.setBorderTop(BorderStyle.MEDIUM);
        boldedFloatWithTopBorder.setAlignment(HorizontalAlignment.CENTER);

        this.percentageStyle = report.createCellStyle();
        percentageStyle.setDataFormat(dataFormat.getFormat("0.00%"));
    }

    public void writeFirstRowFromCopy() {
        scheduleReader.copyFirstRow();

        for (int i = 0; i < scheduleReader.getFirstColumn().size(); i++) {
            reportSheet.createRow(i).createCell(0).setCellValue(scheduleReader.getFirstColumn().get(i));
        }
        reportSheet.autoSizeColumn(0);
    }

    public void writeSumHours() {
        scheduleReader.sumDepartmentsHours();

        for (int i = 3; i < scheduleReader.getScheduleRowsNumber(); i++) {
            reportSheet.getRow(i).createCell(1).setCellValue(scheduleReader.getHoursSumByDay().get(i - 3));
            reportSheet.getRow(i).getCell(1).setCellStyle(defaultFloatCellStyle);

            reportSheet.getRow(i).createCell(2).setCellValue(scheduleReader.calculatePercentagesOfHoursByDay().get(i - 3));
            reportSheet.getRow(i).getCell(2).setCellStyle(percentageStyle);

        }

        reportSheet.getRow(1).createCell(1).setCellValue("Suma godzin");
        reportSheet.getRow(1).createCell(2).setCellValue("Udział dnia");

        reportSheet.createRow(scheduleReader.getScheduleRowsNumber()).createCell(1).setCellValue(scheduleReader.getMonthlyHoursSum());
        reportSheet.getRow(scheduleReader.getScheduleRowsNumber()).getCell(1).setCellStyle(boldedFloatWithTopBorder);
        reportSheet.autoSizeColumn(1);

        reportSheet.getRow(scheduleReader.getScheduleRowsNumber()).createCell(2).setCellValue("1");
        reportSheet.getRow(scheduleReader.getScheduleRowsNumber()).getCell(2).setCellStyle(boldedFloatWithTopBorder);
        reportSheet.autoSizeColumn(2);

    }


}
