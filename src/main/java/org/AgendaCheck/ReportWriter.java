package org.AgendaCheck;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;
import java.util.Map;

public class ReportWriter {
    private XSSFWorkbook report;
    private final DataBank dataBank;
    private final XSSFSheet reportSheet;
    private final Map<String, CellStyle> stylesForCell;


    public ReportWriter(XSSFWorkbook report, DataBank dataBank) {
        this.report = report;
        this.dataBank = dataBank;
        reportSheet = report.createSheet();
        stylesForCell = StylesForCell.createCellStyles(report);
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
        List<String> dates = dataBank.getDatesColumn();
        int columnNrToWrite = 0;
        writeColumn("Dzień", columnNrToWrite, dates, stylesForCell.get("defaultCellStyle"));
    }


    public void writeSecondColumnTurnOverForecast() {
        List<Double> dailyStoreTurnOverList = dataBank.getDailyStoreTurnOver();
        int columnNrToWrite = 1;
        writeColumn("Pilotaż obrotu", columnNrToWrite, dailyStoreTurnOverList, stylesForCell.get("polishZlotyStyle"));
    }

    public void writeThirdColumnShareOfTurnOver() {
        List<Double> dailyStoreTurnOverShare = dataBank.getDailyStoreTurnOverShare();
        int columnNrToWrite = 2;
        writeColumn("Udział dnia w TO", columnNrToWrite, dailyStoreTurnOverShare, stylesForCell.get("percentageStyle"));
    }

    public void writeForthColumnHours() {
        List<Double> storeHoursByDay = dataBank.getDailyStoreHours();
        int columnNrToWrite = 3;
        writeColumn("Suma godzin", columnNrToWrite, storeHoursByDay, stylesForCell.get("defaultDoubleCellStyle"));
    }

    public void writeFifthColumnHoursShare() {
        List<Double> shareOfHours = dataBank.getDailyStoreHoursShare();
        int columnNrToWrite = 4;
        writeColumn("Udział w godzinach", columnNrToWrite, shareOfHours, stylesForCell.get("percentageStyle"));
    }

    public void writeSixthColumnPerfectHours() {
        List<Double> perfectStoreHoursByDay = dataBank.getPerfectStoreHoursByDay();
        int columnNrToWrite = 5;
        writeColumn("\"Idealne\" godziny", columnNrToWrite, perfectStoreHoursByDay, stylesForCell.get("defaultDoubleCellStyle"));
    }

    public void writeSeventhColumnDifferenceInHours() {
        List<Double> dailyDifferenceInHoursToPerfectOnes = dataBank.getDifferenceBetweenPerfectAndActualHours();
        int columnNrToWrite = 6;
        writeColumn("Różnica godzin", columnNrToWrite, dailyDifferenceInHoursToPerfectOnes, stylesForCell.get("defaultDoubleCellStyle"));
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

    public void writeDepartmentSheet(String departmentNameFromForeCast, String departmentNameFromSchedule) {

        //tutaj musi być już pętla - zmienić nazwy w listach na wspólną?
        setSheetName(departmentNameFromForeCast);
        writeFirstColumnDays();
        writeSecondDepartmentColumn(departmentNameFromForeCast);
        writeThirdColumnShareOfTurnOver();
        writeForthDepartmentColumnHours(departmentNameFromSchedule);
        //writeFifthDepartmentColumnHoursShare(departmentNameFromSchedule);
        //idealne godziny
        //różnica
        //ilość godzin wg udziału w obrocie sklepu
    }


    private void writeSecondDepartmentColumn(String departmentNameFromTurnOver) {
        double departmentMonthTurnOver = dataBank.getMonthlyDepartmentTurnOver().get(departmentNameFromTurnOver);

        List<Double> dailyDepartmentTurnOverList =
                DepartmentCalculator.createDailyDepartmentTurnOverList(departmentMonthTurnOver, dataBank.getDailyStoreTurnOverShare());
        int columnNrToWrite = 1;
        writeColumn("Pilotaż obrotu", columnNrToWrite, dailyDepartmentTurnOverList, stylesForCell.get("polishZlotyStyle"));
    }

    private void writeForthDepartmentColumnHours(String departmentNameFromSchedule) {
        List<Double> departmentHoursByDay = dataBank.getDailyDepartmentHoursByName().get(departmentNameFromSchedule);
        int columnNrToWrite = 3;
        writeColumn("Suma godzin", columnNrToWrite, departmentHoursByDay, stylesForCell.get("defaultDoubleCellStyle"));
    }

    private void writeFifthDepartmentColumnHoursShare(String departmentNameFromSchedule) {
        List<Double> departmentHoursByDay = dataBank.getDailyDepartmentHoursByName().get(departmentNameFromSchedule);
        List<Double> shareOfHours = DepartmentCalculator.createDailyDepartmentHoursShareList(departmentHoursByDay);
        int columnNrToWrite = 4;
        writeColumn("Udział w godzinach", columnNrToWrite, shareOfHours, stylesForCell.get("percentageStyle"));
    }


}
