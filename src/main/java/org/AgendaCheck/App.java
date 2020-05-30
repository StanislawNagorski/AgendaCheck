package org.AgendaCheck;

import org.apache.poi.xssf.usermodel.XSSFSheet;
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
        XSSFSheet reportSheet = report.createSheet("Raport");

        ScheduleReader scheduleReader = new ScheduleReader(schedule, report);
        scheduleReader.copyFirstRow();
        scheduleReader.sumDepartmentsHours();
        scheduleReader.writeSumHours();


        report.write(new FileOutputStream("RaportGrafików.xlsx"));
        report.close();


    }
}
