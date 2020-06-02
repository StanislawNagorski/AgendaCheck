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


    public ReportWriter(XSSFWorkbook report, ScheduleReader scheduleReader, ForecastReader forecastReader) {
        this.scheduleReader = scheduleReader;
        this.forecastReader = forecastReader;

        this.reportSheet = report.createSheet("Raport");

        DataFormat dataFormat = report.createDataFormat();
        XSSFFont boldFont = report.createFont();
        this.defaultCellStyle = report.createCellStyle();

        this.defaultDoubleCellStyle = report.createCellStyle();
        defaultDoubleCellStyle.setDataFormat(dataFormat.getFormat("#.###"));

        this.boldedDoubleWithTopBorder = report.createCellStyle();
        boldedDoubleWithTopBorder.setDataFormat(dataFormat.getFormat("#.##"));
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

    public void writeFirstColumnDays() {
        List<String> dates = scheduleReader.getFirstColumn();
        int writeColumnNr = 0;

        for (int i = 0; i < 100; i++) {
            reportSheet.createRow(i);
        }

        for (int i = 0; i < dates.size(); i++) {
            reportSheet.getRow(i).createCell(writeColumnNr)
                    .setCellValue(dates.get(i));
        }

        XSSFCell titleOfDateColumn = reportSheet.getRow(1).getCell(writeColumnNr);
        titleOfDateColumn.setCellStyle(titleBoldedWithBotBorder);

        reportSheet.autoSizeColumn(writeColumnNr);

    }

    public void writeSecondColumnHours() {
        List<Double> hoursByDay = scheduleReader.sumDepartmentsHours();
        int writeColumnNr = 1;
        writeColumn("Suma godzin",writeColumnNr,hoursByDay,defaultDoubleCellStyle);
    }

    public void writeThirdColumnHoursShare(){
        List<Double> shareOfHours = scheduleReader.calculatePercentagesOfHoursByDay();
        int writeColumnNr = 2;
        writeColumn("Udział w godzinach", writeColumnNr, shareOfHours, percentageStyle);

    }

    private List<Double> foreCastListCreation() {

        int[] yearMonth = MonthChecker.checkMonthAndYear(scheduleReader.getFirstColumn());
        int[] range = MonthChecker.rangeOfDaysSince1900ForThisMonthAndMonthLength(yearMonth[0], yearMonth[1]);

       return forecastReader.forecastTOList(range);
    }


    public void writeFourthColumnTurnOverForecast() {
        List<Double> forecast = foreCastListCreation();
        int writeColumnNr = 3;

        writeColumn("Pilotaż obrotu", writeColumnNr, forecast, polishZlotyStyle);
    }

    public void writeFifthColumnShareOfTurnOver() {

        List<Double> forecast = foreCastListCreation();
        List<Double> dailyShare = forecastReader.dailyTurnOverShare(forecast);

        int writeToColumnNr = 4;

        writeColumn("Udział dnia w TO", writeToColumnNr, dailyShare, percentageStyle);

    }

    private <T> void writeColumn(String columnName,int columnNr, List<T> dataList, CellStyle mainStyle){

        XSSFCell titleOfTurnOverColumn = reportSheet.getRow(1).createCell(columnNr);
        titleOfTurnOverColumn.setCellValue(columnName);
        titleOfTurnOverColumn.setCellStyle(titleBoldedWithBotBorder);


        for (int i = 0; i < dataList.size(); i++) {

            XSSFCell cell = reportSheet.getRow(i + 3).createCell(columnNr);

           Object o = dataList.get(0);
           if (o instanceof String){
               cell.setCellValue((String) dataList.get(i));
           } else {
               cell.setCellValue((Double) dataList.get(i));
           }

            cell.setCellStyle(mainStyle);
        }

        reportSheet.getRow(dataList.size()+2)
                .getCell(columnNr).setCellStyle(boldedDoubleWithTopBorder);

        reportSheet.autoSizeColumn(columnNr);

    }


}
