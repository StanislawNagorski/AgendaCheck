package org.AgendaCheck;

import java.util.ArrayList;
import java.util.List;

public class PotentialHoursCalculator {

    private List<Double> shareConsideringCloseDays(List<Double> dayTurnOverShare, List<Double> hoursInMonth) {
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

    public List<Double> perfectHoursCalculation(List<Double> dayTurnOverShare, List<Double> hoursInMonth) {
        List<Double> perfectHours = new ArrayList<>();
        double monthlyHours = hoursInMonth.get(hoursInMonth.size() - 1);
        double monthlyHoursCheck = 0;

        List<Double> shareWithCloseDays = shareConsideringCloseDays(dayTurnOverShare, hoursInMonth);


        for (int i = 0; i < shareWithCloseDays.size()-1; i++) {
            double dailyPerfectHours = shareWithCloseDays.get(i) * monthlyHours;
            perfectHours.add(dailyPerfectHours);
            monthlyHoursCheck += dailyPerfectHours;
        }
        perfectHours.add(monthlyHoursCheck);
        return perfectHours;
    }


    public List<Double> differenceInHours() {
        List<Double> differenceHours = new ArrayList<>();

        return differenceHours;
    }


}
