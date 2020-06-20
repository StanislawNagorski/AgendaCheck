package org.AgendaCheck.Data;

import java.util.ArrayList;
import java.util.List;

public class PotentialHoursCalculator {

    private static List<Double> createShareConsideringCloseDaysList(List<Double> dayTurnOverShare, List<Double> hoursInMonth) {
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

        List<Double> shareWithCloseDays = createShareConsideringCloseDaysList(turnOverShareByDay, hoursByDay);


        for (int i = 0; i < shareWithCloseDays.size() -1; i++) {
            double dailyPerfectHours = shareWithCloseDays.get(i) * monthlyHours;
            perfectHours.add(dailyPerfectHours);
            monthlyHoursCheck += dailyPerfectHours;
        }
        perfectHours.add(monthlyHoursCheck);
        return perfectHours;
    }


    public static List<Double> createDifferenceInHoursList(List<Double> perfectHours, List<Double> hoursInMonth) {
        List<Double> differenceHours = new ArrayList<>();

        for (int i = 0; i < perfectHours.size(); i++) {
            double dailyDiff =hoursInMonth.get(i) - perfectHours.get(i);
            differenceHours.add(dailyDiff);
        }
        differenceHours.set(perfectHours.size()-1, 0.0);

        return differenceHours;
    }

}
