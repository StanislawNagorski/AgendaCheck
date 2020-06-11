package org.AgendaCheck;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
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
        writeColumn("\"Idealne\" godziny", columnNrToWrite, perfectStoreHoursByDay, stylesForCell.get("defaultDoubleCellStyle"), reportSheet);
    }

    private void writeSeventhColumnDifferenceInHours(XSSFSheet reportSheet) {
        List<Double> dailyDifferenceInHoursToPerfectOnes = dataBank.getDifferenceBetweenPerfectAndActualHours();
        int columnNrToWrite = 6;
        writeColumn("Różnica godzin", columnNrToWrite, dailyDifferenceInHoursToPerfectOnes, stylesForCell.get("defaultDoubleCellStyle"), reportSheet);
    }

    public void writeStoreSheet() throws IOException {
        createReportSheet("Sklep");
        XSSFSheet reportSheet = report.getSheet("Sklep");

        writeFirstColumnDays(reportSheet);
        writeForthColumnHours(reportSheet);
        writeFifthColumnHoursShare(reportSheet);
        writeSecondColumnTurnOverForecast(reportSheet);
        writeThirdColumnShareOfTurnOver(reportSheet);
        writeSixthColumnPerfectHours(reportSheet);
        writeSeventhColumnDifferenceInHours(reportSheet);
        addChartToSheet(reportSheet);
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

    private void writeSingleDepartmentSheet(String departmentName, XSSFSheet reportSheet) throws IOException {

        writeFirstColumnDays( reportSheet);
        writeSecondDepartmentColumn(departmentName, reportSheet);
        writeThirdColumnShareOfTurnOver(reportSheet);
        writeForthDepartmentColumnHours(departmentName, reportSheet);
        writeFifthDepartmentColumnHoursShare(departmentName, reportSheet);
        writeSixthDepartmentColumnPerfectHours(departmentName, reportSheet);
        writeSeventhDepartmentColumnDifferenceInHours(departmentName, reportSheet);
        //add chart
    }

    public void writeAllDepartmentsSheets() throws IOException {
        Map<String, Double> turnover = dataBank.getMonthlyDepartmentTurnOver();
        Map<String, List<Double>> hours = dataBank.getDailyDepartmentHoursByName();

        DepartmentNameChecker.changeDepartmentNamesFromScheduleToThoseFromForecast(turnover, hours);

        Set<String> departmentNames = turnover.keySet();

        for (String departmentName : departmentNames) {

            if (!hours.containsKey(departmentName)){
                continue;
            }

            createReportSheet(departmentName);
            writeSingleDepartmentSheet(departmentName, report.getSheet(departmentName));
        }

    }
    private JFreeChart sheetChart() throws IOException {
        JFreeChart chart = ChartCreator.createChart(dataBank.getDailyStoreHoursShare(), dataBank.getDailyStoreTurnOverShare());

        return chart;
    }


    private void addChartToSheet(XSSFSheet reportSheet) throws IOException {
        JFreeChart chart = ChartCreator.createChart(dataBank.getDailyStoreHoursShare(), dataBank.getDailyStoreTurnOverShare());

        //FileInputStream obtains input bytes from the image file
        ByteArrayOutputStream chartOut = new ByteArrayOutputStream();
        BufferedImage chartImage = chart.createBufferedImage(800, 400);
        ImageIO.write(chartImage, "png", chartOut);
        chartOut.flush();
        //Get the contents of an InputStream as a byte[].
        byte[] bytes = chartOut.toByteArray();
        //Adds a picture to the workbook
        int pictureIdx = report.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
        //close the input stream
        chartOut.close();

        //Returns an object that handles instantiating concrete classes
        CreationHelper helper = report.getCreationHelper();

        //Creates the top-level drawing patriarch.
        Drawing drawing = reportSheet.createDrawingPatriarch();

        //Create an anchor that is attached to the worksheet
        ClientAnchor anchor = helper.createClientAnchor();
        //set top-left corner for the image
        anchor.setCol1(8);
        anchor.setRow1(1);

        //Creates a picture
        Picture pict = drawing.createPicture(anchor, pictureIdx);
        //Reset the image to the original size
        pict.resize();
    }



}
