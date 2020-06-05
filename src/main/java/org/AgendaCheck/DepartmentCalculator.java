package org.AgendaCheck;

import java.util.ArrayList;
import java.util.List;

public class DepartmentCalculator {


    public static List<Double> createDailyDepartmentTurnOverList(double departmentMonthTurnOver, List<Double> dailyTurnOverShare) {
        List<Double> dailyDepartmentTurnOver = new ArrayList<>();

        for (int i = 0; i < dailyTurnOverShare.size(); i++) {
            double dayDepartmentTurnOver = dailyTurnOverShare.get(i) * departmentMonthTurnOver;
            dailyDepartmentTurnOver.add(dayDepartmentTurnOver);
        }
        return dailyDepartmentTurnOver;
    }

    public static List<Double> createDailyDepartmentHoursShareList(List<Double> dailyDepartmentHours){
        List<Double> dailyDepartmentHoursShare = new ArrayList<>();

        double monthlyDepartmentHoursSum = dailyDepartmentHours.get(dailyDepartmentHours.size()-1);

        for (int i = 0; i < dailyDepartmentHours.size(); i++) {
            double shareOfDay = dailyDepartmentHours.get(i)/ monthlyDepartmentHoursSum;
            dailyDepartmentHours.add(shareOfDay);
        }

        return dailyDepartmentHoursShare;
    }

}
