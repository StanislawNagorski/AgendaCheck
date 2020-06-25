package org.AgendaCheck.ReportToXLSX;

import org.AgendaCheck.Data.DataBank;
import org.AgendaCheck.Data.PotentialHoursCalculator;
import org.AgendaCheck.Forecast.DepartmentCalculator;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFShape;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.chart.JFreeChart;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ReportWriter {
    private final XSSFWorkbook report;
    private final DataBank dataBank;
    private final Map<String, CellStyle> stylesForCell;
    private double noneRetailDepartmentsHoursSum;


    public ReportWriter(XSSFWorkbook report, DataBank dataBank) {
        this.report = report;
        this.dataBank = dataBank;
        stylesForCell = StylesForCell.createCellStyles(report);
    }

    private void createReportSheet(String sheetName) {
        report.createSheet(sheetName);
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
        writeColumn("Godziny wg obrotu", columnNrToWrite, perfectStoreHoursByDay, stylesForCell.get("defaultDoubleCellStyle"), reportSheet);
    }

    private void writeSeventhColumnDifferenceInHours(XSSFSheet reportSheet) {
        List<Double> dailyDifferenceInHoursToPerfectOnes = dataBank.getDifferenceBetweenPerfectAndActualHours();
        int columnNrToWrite = 6;
        writeColumn("Różnica godzin", columnNrToWrite, dailyDifferenceInHoursToPerfectOnes, stylesForCell.get("defaultDoubleCellStyle"), reportSheet);
    }

    private void writeEightColumnHoursByProductivityTarget(XSSFSheet reportSheet){
        List<Double> dailyHoursToMetProductivityTarget = dataBank.getDailyHoursToMetProductivityTarget();
        int columnNrToWrite = 7;
        String columnName = "Godziny wg celu produktywności: " + dataBank.getProductivityTarget();
        writeColumn(columnName, columnNrToWrite, dailyHoursToMetProductivityTarget, stylesForCell.get("defaultDoubleCellStyle"), reportSheet);
    }

    private void addStoreChart(XSSFSheet reportSheet) throws IOException {
        createChartToSheet(reportSheet, dataBank.getDailyStoreHoursShare(), dataBank.getDailyStoreTurnOverShare());
    }

    public void writeStoreSheet() throws IOException {
        createReportSheet("Total");
        XSSFSheet reportSheet = report.getSheet("Total");

        writeFirstColumnDays(reportSheet);
        writeSecondColumnTurnOverForecast(reportSheet);
        writeThirdColumnShareOfTurnOver(reportSheet);
        writeForthColumnHours(reportSheet);
        writeFifthColumnHoursShare(reportSheet);
        writeSixthColumnPerfectHours(reportSheet);
        writeSeventhColumnDifferenceInHours(reportSheet);
        writeEightColumnHoursByProductivityTarget(reportSheet);
        addStoreChart(reportSheet);
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
        writeColumn("Godziny wg obrotu", columnNrToWrite, perfectStoreHoursByDay, stylesForCell.get("defaultDoubleCellStyle"), reportSheet);
    }

    private void writeSeventhDepartmentColumnDifferenceInHours(String departmentNameFromSchedule, XSSFSheet reportSheet) {
        List<Double> departmentHoursByDay = dataBank.getDailyDepartmentHoursByName().get(departmentNameFromSchedule);
        List<Double> perfectDepartmentHoursByDay =
                PotentialHoursCalculator.createPerfectHoursList(dataBank.getDailyStoreTurnOverShare(), departmentHoursByDay);


        List<Double> dailyDifferenceInHoursToPerfectOnes = PotentialHoursCalculator.createDifferenceInHoursList(perfectDepartmentHoursByDay, departmentHoursByDay);
        int columnNrToWrite = 6;
        writeColumn("Różnica godzin", columnNrToWrite, dailyDifferenceInHoursToPerfectOnes, stylesForCell.get("defaultDoubleCellStyle"), reportSheet);
    }

    private void writeEightDepartmentColumnHoursByProductivityTarger(){
        //TODO dzienne godziny sklepu do celu produktywnosci
        //TODO minus administracja i pok
        //TODO przez udział w obrocie sklepu
    }

    private void addDepartmentChart(XSSFSheet reportSheet, String departmentName) throws IOException {
        List<Double> departmentHoursByDay = dataBank.getDailyDepartmentHoursByName().get(departmentName);
        List<Double> shareOfHours = DepartmentCalculator.createDailyDepartmentHoursShareList(departmentHoursByDay);

        createChartToSheet(reportSheet, shareOfHours, dataBank.getDailyStoreTurnOverShare());
    }

    private void writeSingleDepartmentSheet(String departmentName, XSSFSheet reportSheet) throws IOException {

        writeFirstColumnDays( reportSheet);
        writeSecondDepartmentColumn(departmentName, reportSheet);
        writeThirdColumnShareOfTurnOver(reportSheet);
        writeForthDepartmentColumnHours(departmentName, reportSheet);
        writeFifthDepartmentColumnHoursShare(departmentName, reportSheet);
        writeSixthDepartmentColumnPerfectHours(departmentName, reportSheet);
        writeSeventhDepartmentColumnDifferenceInHours(departmentName, reportSheet);
        addDepartmentChart(reportSheet, departmentName);
    }
    public void writeAllDepartmentsSheets() throws IOException {
        Map<String, Double> turnover = dataBank.getMonthlyDepartmentTurnOver();
        Map<String, List<Double>> hours = dataBank.getDailyDepartmentHoursByName();

        Set<String> departmentNames = turnover.keySet();

        //TODO when moving to different output format you can create Map<Sting,<Map<String, List<Double>>>>
        for (String departmentName : departmentNames) {

            if (!hours.containsKey(departmentName)){
                continue;
            }

            createReportSheet(departmentName);
            writeSingleDepartmentSheet(departmentName, report.getSheet(departmentName));
        }
    }

    private <T> void writeColumn(String columnName, int columnNr, List<T> dataList, CellStyle mainStyle, XSSFSheet reportSheet) {
        if (dataList.isEmpty()){
            return;
        }

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

    private void createChartToSheet(XSSFSheet reportSheet, List<Double> hoursShare, List<Double> turnoverShare) throws IOException {
        JFreeChart chart = ChartCreator.createChart(hoursShare, turnoverShare);

        ByteArrayOutputStream chartOutputSteam = new ByteArrayOutputStream();
        BufferedImage chartImage = chart.createBufferedImage(800, 400);
        ImageIO.write(chartImage, "png", chartOutputSteam);
        chartOutputSteam.flush();

        byte[] chartToBytes = chartOutputSteam.toByteArray();
        int pictureIdx = report.addPicture(chartToBytes, Workbook.PICTURE_TYPE_PNG);
        chartOutputSteam.close();

        //Returns an object that handles instantiating concrete classes
        CreationHelper helper = report.getCreationHelper();

        Drawing<XSSFShape> drawing = reportSheet.createDrawingPatriarch();

        ClientAnchor anchor = helper.createClientAnchor();
        anchor.setCol1(9);
        anchor.setRow1(1);

        Picture pict = drawing.createPicture(anchor, pictureIdx);
        pict.resize();
    }



}
