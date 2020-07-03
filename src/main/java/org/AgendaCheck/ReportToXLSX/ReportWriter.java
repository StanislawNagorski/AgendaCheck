package org.AgendaCheck.ReportToXLSX;

import org.AgendaCheck.Data.DataBank;
import org.AgendaCheck.Data.PotentialHoursCalculator;
import org.AgendaCheck.Forecast.DepartmentCalculator;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
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
        writeColumn("Zaplanowane godziny", columnNrToWrite, storeHoursByDay, stylesForCell.get("defaultDoubleCellStyle"), reportSheet);
    }

    private void writeFifthColumnHoursShare(XSSFSheet reportSheet) {
        List<Double> shareOfHours = dataBank.getDailyStoreHoursShare();
        int columnNrToWrite = 4;
        writeColumn("Udział w godzinach", columnNrToWrite, shareOfHours, stylesForCell.get("percentageStyle"), reportSheet);
    }

    private void writeSixthColumnPerfectHours(XSSFSheet reportSheet) {
        List<Double> perfectStoreHoursByDay = dataBank.getPerfectStoreHoursByDay();
        int columnNrToWrite = 5;

        String targetInfo = "Godziny proporcjonalnie";
        addExtraTopCell(targetInfo,columnNrToWrite,reportSheet);
        writeColumn("wg dziennego obrotu", columnNrToWrite, perfectStoreHoursByDay, stylesForCell.get("defaultDoubleCellStyle"), reportSheet);
    }

    private void writeSeventhColumnHoursByProductivityTarget(XSSFSheet reportSheet) {
        List<Double> dailyHoursToMetProductivityTarget = dataBank.getDailyStoreHoursToMetProductivityTarget();
        int columnNrToWrite = 6;

        String extraInfo = "Cel produktywności: " + dataBank.getProductivityTarget();
        String columnName = "Godziny aby zrealizować";
        addExtraTopCell(columnName,columnNrToWrite,reportSheet);

        writeColumn(extraInfo, columnNrToWrite, dailyHoursToMetProductivityTarget, stylesForCell.get("defaultDoubleCellStyle"), reportSheet);
    }

    private void writeEightColumnDifferenceInHours(XSSFSheet reportSheet) {
        List<Double> dailyDifferenceInHoursToPerfectOnes = dataBank.getDifferenceBetweenPerfectAndActualHours();


        int columnNrToWrite = 7;
        addExtraTopCell("Różnica godzin do", columnNrToWrite, reportSheet);
        writeColumn("celu produktywności", columnNrToWrite, dailyDifferenceInHoursToPerfectOnes, stylesForCell.get("defaultDoubleCellStyle"), reportSheet);

    }

    private void  writeNinthColumnMissingTurnover(XSSFSheet reportSheet){
        List<Double> missingDailyTurnover = dataBank.getDailyMissingTurnoverToMetProductivityTarget();

        int columnNrToWrite = 8;
        addExtraTopCell("Różnica obrótu do celu", columnNrToWrite, reportSheet);
        writeColumn("przy zaplanowanych godzinach ", columnNrToWrite, missingDailyTurnover,
                stylesForCell.get("polishZlotyStyle"), reportSheet);

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
        writeSeventhColumnHoursByProductivityTarget(reportSheet);
        writeEightColumnDifferenceInHours(reportSheet);
        writeNinthColumnMissingTurnover(reportSheet);
        addStoreChart(reportSheet);
        addCellsWithStoreProductivity(reportSheet);
    }


    private void writeSecondDepartmentColumn(String departmentNameFromTurnOver, XSSFSheet reportSheet) {
        double departmentMonthTurnOver = dataBank.getMonthlyDepartmentTurnOver().get(departmentNameFromTurnOver);

        List<Double> dailyDepartmentTurnOverList =
                DepartmentCalculator.createDailyDepartmentTurnOverList(departmentMonthTurnOver, dataBank.getDailyStoreTurnOverShare());
        int columnNrToWrite = 1;
        writeColumn("Pilotaż obrotu", columnNrToWrite, dailyDepartmentTurnOverList,
                stylesForCell.get("polishZlotyStyle"), reportSheet);
    }

    private void writeForthDepartmentColumnHours(String departmentNameFromSchedule, XSSFSheet reportSheet) {
        List<Double> departmentHoursByDay = dataBank.getDailyDepartmentHoursByName().get(departmentNameFromSchedule);
        int columnNrToWrite = 3;
        writeColumn("Zaplanowane godziny", columnNrToWrite, departmentHoursByDay,
                stylesForCell.get("defaultDoubleCellStyle"), reportSheet);
    }

    private void writeFifthDepartmentColumnHoursShare(String departmentNameFromSchedule, XSSFSheet reportSheet) {
        List<Double> departmentHoursByDay = dataBank.getDailyDepartmentHoursByName().get(departmentNameFromSchedule);
        List<Double> shareOfHours = DepartmentCalculator.createDailyDepartmentHoursShareList(departmentHoursByDay);

        int columnNrToWrite = 4;
        writeColumn("Udział w godzinach", columnNrToWrite, shareOfHours, stylesForCell.get("percentageStyle"), reportSheet);
    }

    private void writeSixthDepartmentColumnPerfectHours(String departmentNameFromSchedule, XSSFSheet reportSheet) {
        List<Double> departmentHoursByDay = dataBank.getDailyDepartmentHoursByName().get(departmentNameFromSchedule);

        List<Double> perfectStoreHoursByDay =
                PotentialHoursCalculator.createPerfectHoursList(dataBank.getDailyStoreTurnOverShare(), departmentHoursByDay);
        int columnNrToWrite = 5;
        String targetInfo = "Godziny rozłożone";
        addExtraTopCell(targetInfo,columnNrToWrite,reportSheet);
        writeColumn("wg dziennego obrotu", columnNrToWrite, perfectStoreHoursByDay,
                stylesForCell.get("defaultDoubleCellStyle"), reportSheet);
    }

    private void writeSeventhDepartmentColumnHoursByProductivityTarget(String departmentNameFromSchedule, XSSFSheet reportSheet) {

        double storeTurnover = dataBank.getStoreForcastedByDepartmentsTurnOver();
        double departmentTurnover = dataBank.getMonthlyDepartmentTurnOver().get(departmentNameFromSchedule);
        List<Double> storeRetailDailyHours = dataBank.getDailyRetailHoursToMetProductivityTarget();

        List<Double> dailyDepartmentHoursToMetProductivityTarget =
                PotentialHoursCalculator.createDailyDepartmentHoursToMetProductivityTarget(storeTurnover,
                        departmentTurnover, storeRetailDailyHours);
        int columnNrToWrite = 6;

        String extraInfo = "Cel produktywności: " + dataBank.getProductivityTarget();

        addExtraTopCell("Godziny aby zrealizować SKLEPOWY",columnNrToWrite,reportSheet);
        writeColumn(extraInfo, columnNrToWrite, dailyDepartmentHoursToMetProductivityTarget,
                stylesForCell.get("defaultDoubleCellStyle"), reportSheet);
        addExtraBotCell("godziny wg udziału w TO sklepu", columnNrToWrite, reportSheet);
        columnGExplanation(reportSheet);
    }

    private void writeEightDepartmentColumnDifferenceInHours(String departmentNameFromSchedule, XSSFSheet reportSheet) {
        double storeTurnover = dataBank.getStoreForcastedByDepartmentsTurnOver();
        double departmentTurnover = dataBank.getMonthlyDepartmentTurnOver().get(departmentNameFromSchedule);
        List<Double> storeRetailDailyHours = dataBank.getDailyRetailHoursToMetProductivityTarget();

        List<Double> dailyDepartmentHoursToMetProductivityTarget =
                PotentialHoursCalculator.createDailyDepartmentHoursToMetProductivityTarget(storeTurnover,
                        departmentTurnover, storeRetailDailyHours);

        List<Double> departmentHoursByDay = dataBank.getDailyDepartmentHoursByName().get(departmentNameFromSchedule);

        List<Double> dailyDifferenceInHoursToPerfectOnes = PotentialHoursCalculator.substractColumnsValues
                (dailyDepartmentHoursToMetProductivityTarget, departmentHoursByDay);
        int columnNrToWrite = 7;

        addExtraTopCell("Różnica godzin do", columnNrToWrite, reportSheet);
        writeColumn("celu produktywności", columnNrToWrite, dailyDifferenceInHoursToPerfectOnes,
                stylesForCell.get("defaultDoubleCellStyle"), reportSheet);
    }

    private void writeNinthDepartmentColumnMissingTurnover(String departmentName, XSSFSheet reportSheet){
        double productivityTarget = dataBank.getProductivityTarget();
        double departmentMonthTurnOver = dataBank.getMonthlyDepartmentTurnOver().get(departmentName);
        List<Double> dailyDepartmentTurnOver =
                DepartmentCalculator.createDailyDepartmentTurnOverList(departmentMonthTurnOver, dataBank.getDailyStoreTurnOverShare());
        List<Double> dailyTurnoverShare = dataBank.getDailyStoreTurnOverShare();
        List<Double> dailyDepartmentHours = dataBank.getDailyDepartmentHoursByName().get(departmentName);


        List<Double> missingDailyTurnover = PotentialHoursCalculator.createDailyTurnoverMissingToMetProductivityTarget(productivityTarget,
                dailyDepartmentTurnOver, dailyTurnoverShare, dailyDepartmentHours);

        int columnNrToWrite = 8;
        addExtraTopCell("Różnica obrótu do celu", columnNrToWrite, reportSheet);
        writeColumn("przy zaplanowanych godzinach ", columnNrToWrite, missingDailyTurnover,
                stylesForCell.get("polishZlotyStyle"), reportSheet);

    }

    private void addDepartmentChart(XSSFSheet reportSheet, String departmentName) throws IOException {
        List<Double> departmentHoursByDay = dataBank.getDailyDepartmentHoursByName().get(departmentName);
        List<Double> shareOfHours = DepartmentCalculator.createDailyDepartmentHoursShareList(departmentHoursByDay);

        createChartToSheet(reportSheet, shareOfHours, dataBank.getDailyStoreTurnOverShare());
    }

    private void writeSingleDepartmentSheet(String departmentName, XSSFSheet reportSheet) throws IOException {

        writeFirstColumnDays(reportSheet);
        writeSecondDepartmentColumn(departmentName, reportSheet);
        writeThirdColumnShareOfTurnOver(reportSheet);
        writeForthDepartmentColumnHours(departmentName, reportSheet);
        writeFifthDepartmentColumnHoursShare(departmentName, reportSheet);
        writeSixthDepartmentColumnPerfectHours(departmentName, reportSheet);
        writeSeventhDepartmentColumnHoursByProductivityTarget(departmentName, reportSheet);
        writeEightDepartmentColumnDifferenceInHours(departmentName, reportSheet);
        writeNinthDepartmentColumnMissingTurnover(departmentName, reportSheet);
        addDepartmentChart(reportSheet, departmentName);
        addCellsWithDepartmentProductivity(reportSheet, departmentName);
    }

    public void writeAllDepartmentsSheets() throws IOException {
        Map<String, Double> turnover = dataBank.getMonthlyDepartmentTurnOver();
        Map<String, List<Double>> hours = dataBank.getDailyDepartmentHoursByName();

        Set<String> departmentNames = turnover.keySet();

        for (String departmentName : departmentNames) {

            if (!hours.containsKey(departmentName)) {
                continue;
            }

            createReportSheet(departmentName);
            writeSingleDepartmentSheet(departmentName, report.getSheet(departmentName));
        }
    }

    private <T> void writeColumn(String columnName, int columnNr, List<T> dataList, CellStyle mainStyle, XSSFSheet reportSheet) {
        if (dataList.isEmpty()) {
            return;
        }

        XSSFCell titleColumnCell = reportSheet.getRow(1).createCell(columnNr);
        titleColumnCell.setCellValue(columnName);
        titleColumnCell.setCellStyle(stylesForCell.get("titleBoldedWithBotBorder"));

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

    private void addExtraTopCell(String text, int columnNr, XSSFSheet reportSheet) {
        XSSFCell titleColumnCell = reportSheet.getRow(0).createCell(columnNr);
        titleColumnCell.setCellValue(text);
        titleColumnCell.setCellStyle(stylesForCell.get("boldedDoubleWithTopBorder"));
    }
    private void addExtraBotCell(String text, int columnNr, XSSFSheet reportSheet) {
        int spareRows = 13;
        int lastRowNum = reportSheet.getLastRowNum()-spareRows;
        XSSFCell titleColumnCell = reportSheet.getRow(lastRowNum).createCell(columnNr);
        titleColumnCell.setCellValue(text);
        titleColumnCell.setCellStyle(stylesForCell.get("titleBoldedWithBotBorder"));
    }

    private void columnGExplanation(XSSFSheet reportSheet) {

        int columnGNumber = 6;
        XSSFCell cellToComment  = reportSheet.getRow(1).getCell(columnGNumber);
        XSSFDrawing hpt = reportSheet.createDrawingPatriarch();
        XSSFComment comment = hpt.createCellComment(new XSSFClientAnchor(0, 0, 0, 0, (short) 4, 1, (short) 6, 5));

        String commentString = "Sposób liczenia: \n " +
                "Dzienne godziny sklepu potrzebne do osiągnięcia celu produktywności" +
                "\n minus  godziny działów niehandlowych z tego dnia. " +
                "\n To podzielone przez " +
                "udział w obrocie danego sektora. " +
                "\nKolumna pokazuje ile dany sektor powinien miec godzin wg jego obrotu w sklepie. Gdzie przeinwestowaliśmy a gdzie niedoinwestowaliśmy.";

        comment.setString(commentString);
        cellToComment.setCellComment(comment);
    }



    private void addCellsWithStoreProductivity(XSSFSheet reportSheet){

        int columnNumber = 2;
        int spareRows = 13;
        int lastRowNum = reportSheet.getLastRowNum()-spareRows;

        XSSFCell storeProductivityCellTitle= reportSheet.getRow(lastRowNum).createCell(columnNumber);
        storeProductivityCellTitle.setCellValue("Pilotowana produktywność");
        storeProductivityCellTitle.setCellStyle(stylesForCell.get("titleBoldedWithBotBorder"));

        List<Double> dailyStoreTurnOver = dataBank.getDailyStoreTurnOver();
        double forcastedStoreTurnOver = dailyStoreTurnOver.get(dailyStoreTurnOver.size() - 1);

        List<Double> dailyStoreHours = dataBank.getDailyStoreHours();
        double monthlyHours = dailyStoreHours.get(dailyStoreHours.size() - 1);

        double currentProductivity = forcastedStoreTurnOver /  monthlyHours;

        XSSFCell storeProductivityCellValue= reportSheet.getRow(lastRowNum+1).createCell(columnNumber);
        storeProductivityCellValue.setCellValue(currentProductivity);
        storeProductivityCellValue.setCellStyle(stylesForCell.get("defaultDoubleCellStyle"));
    }

    private void addCellsWithDepartmentProductivity(XSSFSheet reportSheet, String departmentName){
        int columnNumber = 2;
        int spareRows = 13;
        int lastRowNum = reportSheet.getLastRowNum()-spareRows;

        XSSFCell storeProductivityCellTitle= reportSheet.getRow(lastRowNum).createCell(columnNumber);
        storeProductivityCellTitle.setCellValue("Pilotowana produktywność");
        storeProductivityCellTitle.setCellStyle(stylesForCell.get("titleBoldedWithBotBorder"));

        Double forcastedDepartmentTurnOver = dataBank.getMonthlyDepartmentTurnOver().get(departmentName);

        List<Double> hoursByDay = dataBank.getDailyDepartmentHoursByName().get(departmentName);
        double monthlyHours = hoursByDay.get(hoursByDay.size() - 1);

        double currentProductivity = forcastedDepartmentTurnOver /  monthlyHours;

        XSSFCell storeProductivityCellValue= reportSheet.getRow(lastRowNum+1).createCell(columnNumber);
        storeProductivityCellValue.setCellValue(currentProductivity);
        storeProductivityCellValue.setCellStyle(stylesForCell.get("defaultDoubleCellStyle"));
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
        anchor.setCol1(10);
        anchor.setRow1(1);

        Picture pict = drawing.createPicture(anchor, pictureIdx);
        pict.resize();
    }


}
