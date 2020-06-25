package org.AgendaCheck.Data;

import java.util.ArrayList;
import java.util.List;

public class PotentialHoursCalculator {

    private static List<Double> createTurnoverShareConsideringCloseDaysList(List<Double> dayTurnOverShare, List<Double> hoursInMonth) {
        List<Double> shareWithCloseDays = new ArrayList<>();

        double closeDaysTotalShare = 0;
        for (int i = 0; i < dayTurnOverShare.size(); i++) {
            if (hoursInMonth.get(i) == 0) {
                closeDaysTotalShare += dayTurnOverShare.get(i);
            }
        }

        for (int i = 0; i < dayTurnOverShare.size(); i++) {
            if (i == dayTurnOverShare.size() - 1) {
                shareWithCloseDays.add(dayTurnOverShare.get(i));
            } else if (hoursInMonth.get(i) == 0) {
                shareWithCloseDays.add(0.0);
            } else {
                shareWithCloseDays.add(dayTurnOverShare.get(i) +
                        (dayTurnOverShare.get(i) * closeDaysTotalShare));
            }
        }
        return shareWithCloseDays;
    }

    public static List<Double> createPerfectHoursList(List<Double> turnOverShareByDay, List<Double> hoursByDay) {
        List<Double> perfectHours = new ArrayList<>();
        double monthlyHours = hoursByDay.get(hoursByDay.size() - 1);
        double monthlyHoursCheck = 0;

        List<Double> shareWithCloseDays = createTurnoverShareConsideringCloseDaysList(turnOverShareByDay, hoursByDay);


        for (int i = 0; i < shareWithCloseDays.size() - 1; i++) {
            double dailyPerfectHours = shareWithCloseDays.get(i) * monthlyHours;
            perfectHours.add(dailyPerfectHours);
            monthlyHoursCheck += dailyPerfectHours;
        }
        perfectHours.add(monthlyHoursCheck);
        return perfectHours;
    }


    public static List<Double> substractColumnsValues(List<Double> hours, List<Double> hoursToSubtract) {
        List<Double> differenceHours = new ArrayList<>();

        for (int i = 0; i < hoursToSubtract.size(); i++) {
            double dailyDiff = hours.get(i) - hoursToSubtract.get(i);
            differenceHours.add(dailyDiff);
        }

        return differenceHours;
    }

    public static List<Double> createStoreHoursDeterminedByProductivityTargetList(double productivityTarget,
                                                                                  List<Double> turnOverByDay,
                                                                                  List<Double> turnOverShareByDay,
                                                                                  List<Double> hoursInMonth) {

        double totalMonthTurnover = turnOverByDay.get(turnOverByDay.size() - 1);
        double amountOfHoursToMetProductivityTarget = totalMonthTurnover / productivityTarget;
        List<Double> brickTurnOverShareByDay = createTurnoverShareConsideringCloseDaysList(turnOverShareByDay, hoursInMonth);

        List<Double> dailyHoursToMetProductivityTarget = new ArrayList<>();

        for (Double dailyTurnoverShare : brickTurnOverShareByDay) {
            double amountHoursForThisDay = dailyTurnoverShare * amountOfHoursToMetProductivityTarget;
            dailyHoursToMetProductivityTarget.add(amountHoursForThisDay);
        }

        return dailyHoursToMetProductivityTarget;
    }

    public static List<Double> createDailyDepartmentHoursToMetProductivityTarget(double storeTotalTurnOverAsSumOfDepartments,
                                                                                 double departmentTurnover,
                                                                                 List<Double> dailyRetailStoreHoursToMetProductivityTarget) {

        double departmentTurnoverShare = departmentTurnover / storeTotalTurnOverAsSumOfDepartments;

        List<Double> dailyDepartmentHoursToMetProductivityTarget = new ArrayList<>();
        for (int i = 0; i < dailyRetailStoreHoursToMetProductivityTarget.size(); i++) {
            double hoursForThisDay = dailyRetailStoreHoursToMetProductivityTarget.get(i) * departmentTurnoverShare;
            dailyDepartmentHoursToMetProductivityTarget.add(hoursForThisDay);
        }
        return dailyDepartmentHoursToMetProductivityTarget;
    }

}
