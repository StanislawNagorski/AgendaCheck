package org.AgendaCheck;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException {

        XSSFWorkbook schedule = new XSSFWorkbook(new FileInputStream("godzinyMaj.xlsx"));
        XSSFWorkbook report = new XSSFWorkbook();

        ScheduleReader scheduleReader = new ScheduleReader(schedule);
        ReportWriter reportWriter = new ReportWriter(report, scheduleReader);
        reportWriter.writeFirstRowFromCopy();
        reportWriter.writeSumHours();


        report.write(new FileOutputStream("RaportGrafików.xlsx"));
        report.close();


    }
}
