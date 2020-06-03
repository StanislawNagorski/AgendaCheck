package org.AgendaCheck;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;
import java.util.Map;

public class ReportWriter {
    private final ScheduleReader scheduleReader;
    private final ForecastReader forecastReader;
    private final XSSFSheet reportSheet;
    private Map<String, CellStyle> stylesForCell;

    public ReportWriter(XSSFWorkbook report, ScheduleReader scheduleReader, ForecastReader forecastReader, String sheetName) {
        this.scheduleReader = scheduleReader;
        this.forecastReader = forecastReader;
        this.reportSheet = report.createSheet(sheetName);
        stylesForCell = StylesForCell.createCellStyles(report);
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
        writeColumn("Dzień",
                columnNrToWrite,
                dates,
                stylesForCell.get("defaultCellStyle"));
    }

    public void writeSecondColumnTurnOverForecast() {
        List<Double> forecast = foreCastListCreation();
        int columnNrToWrite = 1;

        writeColumn("Pilotaż obrotu", columnNrToWrite, forecast, stylesForCell.get("polishZlotyStyle"));
    }

    public int[] getYearMonth(){
        return MonthChecker.checkMonthAndYear(scheduleReader.getFirstColumn());
    }

    private List<Double> foreCastListCreation() {
        int[] yearMonth = getYearMonth();
        int[] range = MonthChecker.rangeOfDaysSince1900ForThisMonthAndMonthLength(yearMonth[0], yearMonth[1]);

        return forecastReader.forecastTOList(range);
    }

    public void writeThirdColumnShareOfTurnOver() {

        List<Double> forecast = foreCastListCreation();
        List<Double> dailyShare = forecastReader.dailyTurnOverShare(forecast);

        int columnNrToWrite = 2;

        writeColumn("Udział dnia w TO", columnNrToWrite, dailyShare, stylesForCell.get("percentageStyle"));

    }

    public void writeForthColumnHours() {
        List<Double> hoursByDay = scheduleReader.sumDepartmentsHours();
        int columnNrToWrite = 3;
        writeColumn("Suma godzin", columnNrToWrite, hoursByDay, stylesForCell.get("defaultDoubleCellStyle"));
    }

    public void writeFifthColumnHoursShare() {
        List<Double> shareOfHours = scheduleReader.calculatePercentagesOfHoursByDay();
        int columnNrToWrite = 4;
        writeColumn("Udział w godzinach", columnNrToWrite, shareOfHours, stylesForCell.get("percentageStyle"));
    }

    public void writeSixthColumnPerfectHours(){
        List<Double> forecast = foreCastListCreation();
        List<Double> dailyShare = forecastReader.dailyTurnOverShare(forecast);
        List<Double> hoursByDay = scheduleReader.sumDepartmentsHours();

        PotentialHoursCalculator potentialHoursCalculator = new PotentialHoursCalculator();
        List<Double> perfectHours = potentialHoursCalculator.perfectHoursCalculation(dailyShare,hoursByDay);

        int columnNrToWrite = 5;
        writeColumn("\"Idealne\" godziny",columnNrToWrite, perfectHours, stylesForCell.get("defaultDoubleCellStyle"));
    }

    public void writeSeventhColumnDifferenceInHours(){
        PotentialHoursCalculator potentialHoursCalculator = new PotentialHoursCalculator();
        List<Double> forecast = foreCastListCreation();
        List<Double> dailyShare = forecastReader.dailyTurnOverShare(forecast);
        List<Double> hoursByDay = scheduleReader.sumDepartmentsHours();
        List<Double> perfectHours = potentialHoursCalculator.perfectHoursCalculation(dailyShare,hoursByDay);


        List<Double> dailyDifferenceInHours = potentialHoursCalculator.differenceInHours(perfectHours, hoursByDay);
        int columnNrToWrite = 6;
        writeColumn("Różnica godzin", columnNrToWrite, dailyDifferenceInHours, stylesForCell.get("defaultDoubleCellStyle"));
    }


    private <T> void writeColumn(String columnName, int columnNr, List<T> dataList, CellStyle mainStyle) {

        XSSFCell titleOfTurnOverColumn = reportSheet.getRow(1).createCell(columnNr);
        titleOfTurnOverColumn.setCellValue(columnName);
        titleOfTurnOverColumn.setCellStyle(stylesForCell.get("titleBoldedWithBotBorder"));

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
                .getCell(columnNr).setCellStyle(stylesForCell.get("boldedDoubleWithTopBorder"));

        reportSheet.autoSizeColumn(columnNr);
    }
}
