package org.AgendaCheck;

import java.util.ArrayList;
import java.util.List;

public class PotentialHoursCalculator {

    private Double turnOverWhenStoreIsClosed(){
        return 0.00;
    }

    public List<Double> perfectHoursCalculation(List<Double> dayTurnOverShare, List<Double> hoursInMonth){
        List<Double> perfectHours = new ArrayList<>();
        double monthlyHours = hoursInMonth.get(hoursInMonth.size()-1);

        for (int i = 0; i < dayTurnOverShare.size(); i++) {
            double dailyPerfectHours = dayTurnOverShare.get(i) * monthlyHours;
            perfectHours.add(dailyPerfectHours);
        }
        return perfectHours;
    }


    public List<Double> differenceInHours(){
        List<Double> differenceHours = new ArrayList<>();

        return differenceHours;
    }



}
