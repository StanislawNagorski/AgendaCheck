package org.AgendaCheck;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

public class MonthChecker {

    public static int[] checkMonthAndYear(List<String> firstColumn){
        String firstDay = firstColumn.get(3);
        String[] spited = firstDay.split("-");
        int year = Integer.parseInt(spited[0]);
        int month = Integer.parseInt(spited[1]);

        return new int[]{year, month};
    }

    public static int[] rangeOfDaysSince1900ForThisMonthAndMonthLength(int year, int month){

        YearMonth reportedMonth = YearMonth.of(year,month);
        int lengthOfReportedMonth = reportedMonth.lengthOfMonth() -1;
        LocalDate date = reportedMonth.atDay(1);
        LocalDate start = LocalDate.of(1900,1, 1);

        long startMonthInDaysSince1900 = DAYS.between(start, date) +2;
        long endMonthInDaysSince1900 = startMonthInDaysSince1900 + lengthOfReportedMonth;

        return new int[]{(int) startMonthInDaysSince1900, (int) endMonthInDaysSince1900, lengthOfReportedMonth};
    }


}
