package org.AgendaCheck;

import java.util.List;
import java.util.Map;

public class DataBank {

   ScheduleReader scheduleReader;
   ForecastReader forecastReader;
   private final int numberOFDepartmentSheets;
   private final List<String> datesColumn;
   private final int[] yearMonthOfReport;
   private final int[] rangeOfDays;
   private final List<Double> dailyStoreTurnOver;
   private final List<Double> dailyStoreTurnOverShare;
   private final Map<String, Double> monthlyDepartmentTurnOver;
   private final List<Double> dailyStoreHours;
   private final List<Double> dailyStoreHoursShare;
   private final Map<String, List<Double>> departmentNameAndHoursByDay;
   private final List<Double> perfectStoreHoursByDay;
   private final List<Double> differenceBetweenPerfectAndActualHours;


    public DataBank(ScheduleReader scheduleReader, ForecastReader forecastReader) {
        this.scheduleReader = scheduleReader;
        this.forecastReader = forecastReader;
        numberOFDepartmentSheets = forecastReader.getNumberOFDepartmentSheets();
        datesColumn = scheduleReader.copyDatesToList();
        yearMonthOfReport = MonthChecker.checkMonthAndYear(datesColumn);
        rangeOfDays = MonthChecker.rangeOfDaysSince1900ForThisMonthAndMonthLength(yearMonthOfReport[0], yearMonthOfReport[1]);
        dailyStoreTurnOver = forecastReader.createStoreForecastedTurnOverList(rangeOfDays);
        dailyStoreTurnOverShare = forecastReader.createDailyTurnOverShareList(dailyStoreTurnOver);
        monthlyDepartmentTurnOver = forecastReader.createDepartmentsMonthlyTurnOverMap(yearMonthOfReport[1]);
        dailyStoreHours = scheduleReader.createStoreDailyHoursList();
        dailyStoreHoursShare = scheduleReader.createStoreShareOfHoursByDayList();
        departmentNameAndHoursByDay = scheduleReader.createMapOfScheduleDailyHoursByDepartment();
        perfectStoreHoursByDay = PotentialHoursCalculator.createPerfectHoursList(dailyStoreTurnOverShare, dailyStoreHours);
        differenceBetweenPerfectAndActualHours = PotentialHoursCalculator.createDifferenceInHoursList(perfectStoreHoursByDay, dailyStoreHours);
    }

    public ScheduleReader getScheduleReader() {
        return scheduleReader;
    }

    public ForecastReader getForecastReader() {
        return forecastReader;
    }

    public int getNumberOFDepartmentSheets() {
        return numberOFDepartmentSheets;
    }

    public List<String> getDatesColumn() {
        return datesColumn;
    }

    public int[] getYearMonthOfReport() {
        return yearMonthOfReport;
    }

    public int[] getRangeOfDays() {
        return rangeOfDays;
    }

    public List<Double> getDailyStoreTurnOver() {
        return dailyStoreTurnOver;
    }

    public List<Double> getDailyStoreTurnOverShare() {
        return dailyStoreTurnOverShare;
    }

    public Map<String, Double> getMonthlyDepartmentTurnOver() {
        return monthlyDepartmentTurnOver;
    }

    public List<Double> getDailyStoreHours() {
        return dailyStoreHours;
    }

    public List<Double> getDailyStoreHoursShare() {
        return dailyStoreHoursShare;
    }

    public Map<String, List<Double>> getDepartmentNameAndHoursByDay() {
        return departmentNameAndHoursByDay;
    }

    public List<Double> getPerfectStoreHoursByDay() {
        return perfectStoreHoursByDay;
    }

    public List<Double> getDifferenceBetweenPerfectAndActualHours() {
        return differenceBetweenPerfectAndActualHours;
    }
}
