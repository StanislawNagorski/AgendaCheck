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

    private final CellStyle defaultCellStyle;
    private final CellStyle defaultDoubleCellStyle;
    private final CellStyle boldedDoubleWithTopBorder;
    private final CellStyle titleBoldedWithBotBorder;
    private final CellStyle percentageStyle;
    private final CellStyle polishZlotyStyle;
    private final CellStyle polishZlotyStyleBoldedWithBotBorder;


    public ReportWriter(XSSFWorkbook report, ScheduleReader scheduleReader, ForecastReader forecastReader, String sheetName) {
        this.scheduleReader = scheduleReader;
        this.forecastReader = forecastReader;
        this.reportSheet = report.createSheet(sheetName);

        DataFormat dataFormat = report.createDataFormat();
        XSSFFont boldFont = report.createFont();
        this.defaultCellStyle = report.createCellStyle();

        this.defaultDoubleCellStyle = report.createCellStyle();
        defaultDoubleCellStyle.setDataFormat(dataFormat.getFormat("#.#"));

        this.boldedDoubleWithTopBorder = report.createCellStyle();
        boldedDoubleWithTopBorder.setDataFormat(dataFormat.getFormat("#"));
        boldFont.setBold(true);
        boldedDoubleWithTopBorder.setFont(boldFont);
        boldedDoubleWithTopBorder.setBorderTop(BorderStyle.MEDIUM);
        boldedDoubleWithTopBorder.setAlignment(HorizontalAlignment.CENTER);

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

    private void createRows() {
        int reportLenght = 50;

        for (int i = 0; i < reportLenght; i++) {
            reportSheet.createRow(i);
        }
    }

    public void writeFirstColumnDays() {
        createRows();

        List<String> dates = scheduleReader.getFirstColumn();
        int columnNrToWrite = 0;
        writeColumn("Dzień", columnNrToWrite, dates, defaultCellStyle);
    }

    public void writeSecondColumnTurnOverForecast() {
        List<Double> forecast = foreCastListCreation();
        int columnNrToWrite = 1;

        writeColumn("Pilotaż obrotu", columnNrToWrite, forecast, polishZlotyStyle);
    }

    private List<Double> foreCastListCreation() {

        int[] yearMonth = MonthChecker.checkMonthAndYear(scheduleReader.getFirstColumn());
        int[] range = MonthChecker.rangeOfDaysSince1900ForThisMonthAndMonthLength(yearMonth[0], yearMonth[1]);

        return forecastReader.forecastTOList(range);
    }

    public void writeThirdColumnShareOfTurnOver() {

        List<Double> forecast = foreCastListCreation();
        List<Double> dailyShare = forecastReader.dailyTurnOverShare(forecast);

        int columnNrToWrite = 2;

        writeColumn("Udział dnia w TO", columnNrToWrite, dailyShare, percentageStyle);

    }

    public void writeForthColumnHours() {
        List<Double> hoursByDay = scheduleReader.sumDepartmentsHours();
        int columnNrToWrite = 3;
        writeColumn("Suma godzin", columnNrToWrite, hoursByDay, defaultDoubleCellStyle);
    }

    public void writeFifthColumnHoursShare() {
        List<Double> shareOfHours = scheduleReader.calculatePercentagesOfHoursByDay();
        int columnNrToWrite = 4;
        writeColumn("Udział w godzinach", columnNrToWrite, shareOfHours, percentageStyle);
    }

    public void writeSixthColumnPerfectHours(){
        List<Double> forecast = foreCastListCreation();
        List<Double> dailyShare = forecastReader.dailyTurnOverShare(forecast);
        List<Double> hoursByDay = scheduleReader.sumDepartmentsHours();

        PotentialHoursCalculator potentialHoursCalculator = new PotentialHoursCalculator();
        List<Double> perfectHours = potentialHoursCalculator.perfectHoursCalculation(dailyShare,hoursByDay);

        int columnNrToWrite = 5;
        writeColumn("\"Idealne\" godziny",columnNrToWrite, perfectHours, defaultDoubleCellStyle);
    }

    public void writeSeventhColumnDifferenceInHours(){
        PotentialHoursCalculator potentialHoursCalculator = new PotentialHoursCalculator();
        List<Double> forecast = foreCastListCreation();
        List<Double> dailyShare = forecastReader.dailyTurnOverShare(forecast);
        List<Double> hoursByDay = scheduleReader.sumDepartmentsHours();
        List<Double> perfectHours = potentialHoursCalculator.perfectHoursCalculation(dailyShare,hoursByDay);


        List<Double> dailyDifferenceInHours = potentialHoursCalculator.differenceInHours(perfectHours, hoursByDay);
        int columnNrToWrite = 6;
        writeColumn("Różnica godzin", columnNrToWrite, dailyDifferenceInHours, defaultDoubleCellStyle);
    }


    private <T> void writeColumn(String columnName, int columnNr, List<T> dataList, CellStyle mainStyle) {

        XSSFCell titleOfTurnOverColumn = reportSheet.getRow(1).createCell(columnNr);
        titleOfTurnOverColumn.setCellValue(columnName);
        titleOfTurnOverColumn.setCellStyle(titleBoldedWithBotBorder);

        int rowToStartData = 3;
        for (int i = 0; i < dataList.size(); i++) {

            XSSFCell cell = reportSheet.getRow(i + rowToStartData).createCell(columnNr);

            Object o = dataList.get(0);
            if (o instanceof String) {
                cell.setCellValue((String) dataList.get(i));
            } else {
                cell.setCellValue((Double) dataList.get(i));
            }

            cell.setCellStyle(mainStyle);
        }

        reportSheet.getRow(dataList.size() + 2)
                .getCell(columnNr).setCellStyle(boldedDoubleWithTopBorder);

        reportSheet.autoSizeColumn(columnNr);
    }
}
