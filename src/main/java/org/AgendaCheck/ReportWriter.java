package org.AgendaCheck;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReportWriter {
    private XSSFWorkbook report;
    private final ScheduleReader scheduleReader;
    private final ForecastReader forecastReader;
    private final XSSFSheet reportSheet;
    private Map<String, CellStyle> stylesForCell;
    private List<Double> dailyStoreTurnOverList;
    private List<Double> dailyStoreTurnOverShare;
    private List<Double> storeHoursByDay;
    private Map<String, Double> departmentsMonthlyTurnOverMap;


    public ReportWriter(XSSFWorkbook report, ScheduleReader scheduleReader, ForecastReader forecastReader) {
        this.report = report;
        this.scheduleReader = scheduleReader;
        this.forecastReader = forecastReader;
        this.reportSheet = report.createSheet();
        stylesForCell = StylesForCell.createCellStyles(report);
        dailyStoreTurnOverList = createDailyStoreTurnOverList();
        dailyStoreTurnOverShare = forecastReader.createDailyTurnOverShareList(dailyStoreTurnOverList);
        storeHoursByDay = scheduleReader.sumDepartmentsHours();
    }

    public void setSheetName(String sheetName) {
        report.setSheetName(report.getSheetIndex(reportSheet), sheetName);
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
        writeColumn("Dzień", columnNrToWrite, dates, stylesForCell.get("defaultCellStyle"));
    }

    private int[] createYearMonthToBeReported(){
        return  MonthChecker.checkMonthAndYear(scheduleReader.getFirstColumn());

    }

    private List<Double> createDailyStoreTurnOverList() {
        int[] yearMonthToBeReported = createYearMonthToBeReported();
        int[] range = MonthChecker.rangeOfDaysSince1900ForThisMonthAndMonthLength(yearMonthToBeReported[0], yearMonthToBeReported[1]);

        return forecastReader.forecastTOList(range);
    }

    public void writeSecondColumnTurnOverForecast() {
        int columnNrToWrite = 1;
        writeColumn("Pilotaż obrotu", columnNrToWrite, dailyStoreTurnOverList, stylesForCell.get("polishZlotyStyle"));
    }

    public void writeThirdColumnShareOfTurnOver() {
        int columnNrToWrite = 2;
        writeColumn("Udział dnia w TO", columnNrToWrite, dailyStoreTurnOverShare, stylesForCell.get("percentageStyle"));
    }

    public void writeForthColumnHours() {
        int columnNrToWrite = 3;
        writeColumn("Suma godzin", columnNrToWrite, storeHoursByDay, stylesForCell.get("defaultDoubleCellStyle"));
    }

    public void writeFifthColumnHoursShare() {
        List<Double> shareOfHours = scheduleReader.calculatePercentagesOfHoursByDay();
        int columnNrToWrite = 4;
        writeColumn("Udział w godzinach", columnNrToWrite, shareOfHours, stylesForCell.get("percentageStyle"));
    }

    public void writeSixthColumnPerfectHours() {

        PotentialHoursCalculator potentialHoursCalculator = new PotentialHoursCalculator();
        List<Double> perfectHours = potentialHoursCalculator.createPerfectHoursList(dailyStoreTurnOverShare, storeHoursByDay);

        int columnNrToWrite = 5;
        writeColumn("\"Idealne\" godziny", columnNrToWrite, perfectHours, stylesForCell.get("defaultDoubleCellStyle"));
    }

    public void writeSeventhColumnDifferenceInHours() {
        PotentialHoursCalculator potentialHoursCalculator = new PotentialHoursCalculator();

        List<Double> perfectHours = potentialHoursCalculator.createPerfectHoursList(dailyStoreTurnOverShare, storeHoursByDay);
        List<Double> dailyDifferenceInHours = potentialHoursCalculator.createDifferenceInHoursList(perfectHours, storeHoursByDay);

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

    public void writeDepartmentSheet(String sheetName, double monthlyTurnOver) {
        setSheetName(sheetName);
        writeFirstColumnDays();
        writeSecondDepartmentColumn(monthlyTurnOver);
        writeThirdColumnShareOfTurnOver();
        //czwarta to suma godzin działu
        //udział tych godzin
        //idealne godziny
        //różnica
    }

    private List<Double> createDailyDepartmentTurnOverList(double departmentMonthTurnOver){
        List<Double> dailyDepartmentTurnOver = new ArrayList<>();

        for (int i = 0; i < dailyStoreTurnOverShare.size(); i++) {
            double dayDepartmentTurnOver = dailyStoreTurnOverShare.get(i) * departmentMonthTurnOver;
            dailyDepartmentTurnOver.add(dayDepartmentTurnOver);
        }

        return dailyDepartmentTurnOver;
    }


    private void writeSecondDepartmentColumn(double departmentMonthTurnOver){

        List<Double> dailyDepartmentTurnOverList = createDailyDepartmentTurnOverList(departmentMonthTurnOver);
        int columnNrToWrite = 1;
        writeColumn("Pilotaż obrotu", columnNrToWrite, dailyDepartmentTurnOverList, stylesForCell.get("polishZlotyStyle"));
    }


}
