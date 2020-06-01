package org.AgendaCheck;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

public class ReportWriter {
    private final ScheduleReader scheduleReader;
    private final ForecastReader forecastReader;
    private final XSSFSheet reportSheet;

    private final CellStyle defaultFloatCellStyle;
    private final CellStyle boldedFloatWithTopBorder;
    private final CellStyle titleBoldedWithBotBorder;
    private final CellStyle percentageStyle;
    private final CellStyle polishZlotyStyle;
    private final CellStyle polishZlotyStyleBoldedWithBotBorder;




    public ReportWriter(XSSFWorkbook report, ScheduleReader scheduleReader, ForecastReader forecastReader) {
        this.scheduleReader = scheduleReader;
        this.forecastReader = forecastReader;

        this.reportSheet = report.createSheet("Raport");

        DataFormat dataFormat = report.createDataFormat();
        XSSFFont boldFont = report.createFont();

        this.defaultFloatCellStyle = report.createCellStyle();
        defaultFloatCellStyle.setDataFormat(dataFormat.getFormat("#.###"));

        this.boldedFloatWithTopBorder = report.createCellStyle();
        boldedFloatWithTopBorder.setDataFormat(dataFormat.getFormat("#.##"));
        boldFont.setBold(true);
        boldedFloatWithTopBorder.setFont(boldFont);
        boldedFloatWithTopBorder.setBorderTop(BorderStyle.MEDIUM);
        boldedFloatWithTopBorder.setAlignment(HorizontalAlignment.CENTER);

        this.titleBoldedWithBotBorder = report.createCellStyle();
        boldFont.setBold(true);
        titleBoldedWithBotBorder.setFont(boldFont);
        titleBoldedWithBotBorder.setBorderBottom(BorderStyle.MEDIUM);
        titleBoldedWithBotBorder.setAlignment(HorizontalAlignment.CENTER);

        this.percentageStyle = report.createCellStyle();
        percentageStyle.setDataFormat(dataFormat.getFormat("0.00%"));

        this.polishZlotyStyle = report.createCellStyle();
        polishZlotyStyle.setDataFormat(dataFormat.getFormat("###0,00\\ \"zł\";-###0,00\\ \"zł\""));

        this.polishZlotyStyleBoldedWithBotBorder = report.createCellStyle();
        polishZlotyStyleBoldedWithBotBorder.setDataFormat(dataFormat.getFormat("###0,00\\ \"zł\";-###0,00\\ \"zł\""));
        boldFont.setBold(true);
        polishZlotyStyleBoldedWithBotBorder.setFont(boldFont);
        polishZlotyStyleBoldedWithBotBorder.setBorderTop(BorderStyle.MEDIUM);
        polishZlotyStyleBoldedWithBotBorder.setAlignment(HorizontalAlignment.CENTER);
    }

    public void writeFirstColumnDays() {
        scheduleReader.copyFirstRow();

        for (int i = 0; i < scheduleReader.getFirstColumn().size(); i++) {
            reportSheet.createRow(i).createCell(0)
                    .setCellValue(scheduleReader.getFirstColumn().get(i));
        }

        XSSFCell titleOfDateColumn = reportSheet.getRow(1).getCell(0);
        titleOfDateColumn.setCellStyle(titleBoldedWithBotBorder);

        reportSheet.autoSizeColumn(0);
    }

    public void writeSecondAndThirdColumnHoursAndShare() {
        scheduleReader.sumDepartmentsHours();

        for (int i = 3; i < scheduleReader.getScheduleRowsNumber(); i++) {
            reportSheet.getRow(i).createCell(1)
                    .setCellValue(scheduleReader.getHoursSumByDay().get(i - 3));
            reportSheet.getRow(i).getCell(1).setCellStyle(defaultFloatCellStyle);

            reportSheet.getRow(i).createCell(2)
                    .setCellValue(scheduleReader.calculatePercentagesOfHoursByDay().get(i - 3));
            reportSheet.getRow(i).getCell(2).setCellStyle(percentageStyle);

        }

        XSSFCell titleOfHoursColumn = reportSheet.getRow(1).createCell(1);
        titleOfHoursColumn.setCellValue("Suma godzin");
        titleOfHoursColumn.setCellStyle(titleBoldedWithBotBorder);

        XSSFCell titleOfDayShareInHoursColumn = reportSheet.getRow(1).createCell(2);
        titleOfDayShareInHoursColumn.setCellValue("Udział dnia w godzinach");
        titleOfDayShareInHoursColumn.setCellStyle(titleBoldedWithBotBorder);

        reportSheet.createRow(scheduleReader.getScheduleRowsNumber()).createCell(1)
                .setCellValue(scheduleReader.getMonthlyHoursSum());
        reportSheet.getRow(scheduleReader.getScheduleRowsNumber()).getCell(1).setCellStyle(boldedFloatWithTopBorder);
        reportSheet.autoSizeColumn(1);

        reportSheet.getRow(scheduleReader.getScheduleRowsNumber()).createCell(2)
                .setCellValue("1");
        reportSheet.getRow(scheduleReader.getScheduleRowsNumber()).getCell(2).setCellStyle(boldedFloatWithTopBorder);
        reportSheet.autoSizeColumn(2);

    }


    public void writeFourthColumnTurnOverForecast() {
        int[] yearMonth = MonthChecker.checkMonthAndYear(scheduleReader.getFirstColumn());
        int[] range = MonthChecker.rangeOfDaysSince1900ForThisMonthAndMonthLength(yearMonth[0], yearMonth[1]);

        List<Double> forecast = forecastReader.forecastTOList(range);

        XSSFCell titleOfTurnOverColumn = reportSheet.getRow(1).createCell(3);
        titleOfTurnOverColumn.setCellValue("Pilotaż obrotu");
        titleOfTurnOverColumn.setCellStyle(titleBoldedWithBotBorder);

        for (int i = 0; i < forecast.size(); i++) {

            XSSFCell cell = reportSheet.getRow(i + 3).createCell(3);
            cell.setCellValue(forecast.get(i));
            cell.setCellStyle(polishZlotyStyle);
        }

        reportSheet.getRow(forecast.size()+2)
                .getCell(3).setCellStyle(polishZlotyStyleBoldedWithBotBorder);

        reportSheet.autoSizeColumn(3);
    }

    public void writeFifthColumnShareOfTurnOver() {
        int[] yearMonth = MonthChecker.checkMonthAndYear(scheduleReader.getFirstColumn());
        int[] range = MonthChecker.rangeOfDaysSince1900ForThisMonthAndMonthLength(yearMonth[0], yearMonth[1]);

        List<Double> forecast = forecastReader.forecastTOList(range);

        XSSFCell titleOfTurnOverColumn = reportSheet.getRow(1).createCell(4);
        titleOfTurnOverColumn.setCellValue("Udział dnia w TO");
        titleOfTurnOverColumn.setCellStyle(titleBoldedWithBotBorder);

        for (int i = 0; i < forecast.size(); i++) {

            XSSFCell cell = reportSheet.getRow(i + 3).createCell(4);
            double dailyShareOfMonthlyTurnOver = forecast.get(i) / forecast.get(forecast.size() - 1);
            cell.setCellValue(dailyShareOfMonthlyTurnOver);
            cell.setCellStyle(percentageStyle);
        }

        reportSheet.getRow(forecast.size()+2)
                .getCell(4).setCellStyle(boldedFloatWithTopBorder);

        reportSheet.autoSizeColumn(4);

    }


}
