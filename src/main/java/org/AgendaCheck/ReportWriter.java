package org.AgendaCheck;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;
import java.util.Map;

public class ReportWriter {
    private final XSSFWorkbook report;
    private final DataBank dataBank;
    private final Map<String, CellStyle> stylesForCell;


    public ReportWriter(XSSFWorkbook report, DataBank dataBank) {
        this.report = report;
        this.dataBank = dataBank;
        stylesForCell = StylesForCell.createCellStyles(report);
    }

    public XSSFSheet createReportSheet(String sheetName) {
        return report.createSheet(sheetName);
    }

    private void createRows(XSSFSheet reportSheet) {
        int reportLenght = 50;
        for (int i = 0; i < reportLenght; i++) {
            reportSheet.createRow(i);
        }
    }

    private void writeFirstColumnDays(XSSFSheet reportSheet) {
        createRows(reportSheet);
        List<String> dates = dataBank.getDatesColumn();
        int columnNrToWrite = 0;
        writeColumn("Dzień", columnNrToWrite, dates, stylesForCell.get("defaultCellStyle"), reportSheet);
    }


    private void writeSecondColumnTurnOverForecast(XSSFSheet reportSheet) {
        List<Double> dailyStoreTurnOverList = dataBank.getDailyStoreTurnOver();
        int columnNrToWrite = 1;
        writeColumn("Pilotaż obrotu", columnNrToWrite, dailyStoreTurnOverList, stylesForCell.get("polishZlotyStyle"), reportSheet);
    }

    private void writeThirdColumnShareOfTurnOver(XSSFSheet reportSheet) {
        List<Double> dailyStoreTurnOverShare = dataBank.getDailyStoreTurnOverShare();
        int columnNrToWrite = 2;
        writeColumn("Udział dnia w TO", columnNrToWrite, dailyStoreTurnOverShare, stylesForCell.get("percentageStyle"), reportSheet);
    }

    private void writeForthColumnHours(XSSFSheet reportSheet) {
        List<Double> storeHoursByDay = dataBank.getDailyStoreHours();
        int columnNrToWrite = 3;
        writeColumn("Suma godzin", columnNrToWrite, storeHoursByDay, stylesForCell.get("defaultDoubleCellStyle"), reportSheet);
    }

    private void writeFifthColumnHoursShare(XSSFSheet reportSheet) {
        List<Double> shareOfHours = dataBank.getDailyStoreHoursShare();
        int columnNrToWrite = 4;
        writeColumn("Udział w godzinach", columnNrToWrite, shareOfHours, stylesForCell.get("percentageStyle"), reportSheet);
    }

    private void writeSixthColumnPerfectHours(XSSFSheet reportSheet) {
        List<Double> perfectStoreHoursByDay = dataBank.getPerfectStoreHoursByDay();
        int columnNrToWrite = 5;
        writeColumn("\"Idealne\" godziny", columnNrToWrite, perfectStoreHoursByDay, stylesForCell.get("defaultDoubleCellStyle"), reportSheet);
    }

    private void writeSeventhColumnDifferenceInHours(XSSFSheet reportSheet) {
        List<Double> dailyDifferenceInHoursToPerfectOnes = dataBank.getDifferenceBetweenPerfectAndActualHours();
        int columnNrToWrite = 6;
        writeColumn("Różnica godzin", columnNrToWrite, dailyDifferenceInHoursToPerfectOnes, stylesForCell.get("defaultDoubleCellStyle"), reportSheet);
    }

    public void writeStoreSheet(XSSFSheet reportSheet) {
        writeFirstColumnDays(reportSheet);
        writeForthColumnHours(reportSheet);
        writeFifthColumnHoursShare(reportSheet);
        writeSecondColumnTurnOverForecast(reportSheet);
        writeThirdColumnShareOfTurnOver(reportSheet);
        writeSixthColumnPerfectHours(reportSheet);
        writeSeventhColumnDifferenceInHours(reportSheet);
    }

    private <T> void writeColumn(String columnName, int columnNr, List<T> dataList, CellStyle mainStyle, XSSFSheet reportSheet) {

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


    private void writeSecondDepartmentColumn(String departmentNameFromTurnOver, XSSFSheet reportSheet) {
        double departmentMonthTurnOver = dataBank.getMonthlyDepartmentTurnOver().get(departmentNameFromTurnOver);

        List<Double> dailyDepartmentTurnOverList =
                DepartmentCalculator.createDailyDepartmentTurnOverList(departmentMonthTurnOver, dataBank.getDailyStoreTurnOverShare());
        int columnNrToWrite = 1;
        writeColumn("Pilotaż obrotu", columnNrToWrite, dailyDepartmentTurnOverList, stylesForCell.get("polishZlotyStyle"), reportSheet);
    }

    private void writeForthDepartmentColumnHours(String departmentNameFromSchedule, XSSFSheet reportSheet) {
        List<Double> departmentHoursByDay = dataBank.getDailyDepartmentHoursByName().get(departmentNameFromSchedule);
        int columnNrToWrite = 3;
        writeColumn("Suma godzin", columnNrToWrite, departmentHoursByDay, stylesForCell.get("defaultDoubleCellStyle") , reportSheet);
    }

    private void writeFifthDepartmentColumnHoursShare(String departmentNameFromSchedule, XSSFSheet reportSheet) {
        List<Double> departmentHoursByDay = dataBank.getDailyDepartmentHoursByName().get(departmentNameFromSchedule);
        List<Double> shareOfHours = DepartmentCalculator.createDailyDepartmentHoursShareList(departmentHoursByDay);

        int columnNrToWrite = 4;
        writeColumn("Udział w godzinach", columnNrToWrite, shareOfHours, stylesForCell.get("percentageStyle") ,reportSheet);
    }

    private void writeSixthDepartmentColumnPerfectHours(String departmentNameFromSchedule, XSSFSheet reportSheet) {
        List<Double> departmentHoursByDay = dataBank.getDailyDepartmentHoursByName().get(departmentNameFromSchedule);

        List<Double> perfectStoreHoursByDay =
                PotentialHoursCalculator.createPerfectHoursList(dataBank.getDailyStoreTurnOverShare(), departmentHoursByDay);
        int columnNrToWrite = 5;
        writeColumn("\"Idealne\" godziny", columnNrToWrite, perfectStoreHoursByDay, stylesForCell.get("defaultDoubleCellStyle"), reportSheet);
    }

    private void writeSeventhDepartmentColumnDifferenceInHours(String departmentNameFromSchedule, XSSFSheet reportSheet) {
        List<Double> departmentHoursByDay = dataBank.getDailyDepartmentHoursByName().get(departmentNameFromSchedule);
        List<Double> perfectDepartmentHoursByDay =
                PotentialHoursCalculator.createPerfectHoursList(dataBank.getDailyStoreTurnOverShare(), departmentHoursByDay);


        List<Double> dailyDifferenceInHoursToPerfectOnes = PotentialHoursCalculator.createDifferenceInHoursList(perfectDepartmentHoursByDay, departmentHoursByDay);
        int columnNrToWrite = 6;
        writeColumn("Różnica godzin", columnNrToWrite, dailyDifferenceInHoursToPerfectOnes, stylesForCell.get("defaultDoubleCellStyle"), reportSheet);
    }

    public void writeDepartmentSheet(String departmentNameFromForeCast, String departmentNameFromSchedule, XSSFSheet reportSheet) {

        writeFirstColumnDays( reportSheet);
        writeSecondDepartmentColumn(departmentNameFromForeCast, reportSheet);
        writeThirdColumnShareOfTurnOver(reportSheet);
        writeForthDepartmentColumnHours(departmentNameFromSchedule, reportSheet);
        writeFifthDepartmentColumnHoursShare(departmentNameFromSchedule, reportSheet);
        writeSixthDepartmentColumnPerfectHours(departmentNameFromSchedule, reportSheet);
        writeSeventhDepartmentColumnDifferenceInHours(departmentNameFromSchedule, reportSheet);
    }



}
