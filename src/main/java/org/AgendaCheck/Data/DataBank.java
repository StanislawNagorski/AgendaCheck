package org.AgendaCheck.Data;

import org.AgendaCheck.Forecast.ForecastReader;
import org.AgendaCheck.Schedule.MonthChecker;
import org.AgendaCheck.Schedule.ScheduleReader;

import java.util.*;

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
    private final double storeForcastedByDepartmentsTurnOver;
    private final List<Double> dailyStoreHours;
    private final List<Double> dailyStoreHoursShare;
    private final Map<String, List<Double>> dailyDepartmentHoursByName;
    private final Map<String, List<Double>> dailyNoneRetailDepartmentHours;
    private final List<Double> dailySumOfNonRetailHours;
    private final List<Double> perfectStoreHoursByDay;
    private final List<Double> differenceBetweenPerfectAndActualHours;
    private final double productivityTarget;
    private final List<Double> dailyStoreHoursToMetProductivityTarget;
    private final List<Double> dailyRetailHoursToMetProductivityTarget;


    public DataBank(ScheduleReader scheduleReader, ForecastReader forecastReader, double productivityTarget) {
        this.productivityTarget = productivityTarget;
        this.scheduleReader = scheduleReader;
        this.forecastReader = forecastReader;
        numberOFDepartmentSheets = forecastReader.getNumberOFDepartmentSheets();
        datesColumn = scheduleReader.copyDatesToList();
        yearMonthOfReport = MonthChecker.checkMonthAndYear(datesColumn);
        rangeOfDays = MonthChecker.rangeOfDaysSince1900ForThisMonthAndMonthLength(yearMonthOfReport[0], yearMonthOfReport[1]);
        dailyStoreTurnOver = forecastReader.createStoreForecastedTurnOverList(rangeOfDays);
        dailyStoreTurnOverShare = forecastReader.createDailyTurnOverShareList(dailyStoreTurnOver);
        monthlyDepartmentTurnOver = forecastReader.createDepartmentsMonthlyTurnOverMap(yearMonthOfReport[1]);
        storeForcastedByDepartmentsTurnOver = sumOfForecastedTuroverByDepartmets();
        dailyStoreHours = scheduleReader.createStoreDailyHoursList();
        dailyStoreHoursShare = scheduleReader.createStoreShareOfHoursByDayList();
        dailyDepartmentHoursByName = scheduleReader.createMapOfScheduleDailyHoursByDepartment();
        matchNamesInScheduleToThoseFromForecast();
        dailyNoneRetailDepartmentHours = crateNonRetailDepartmentsHoursMap();
        dailySumOfNonRetailHours = calcDailySumOfNonRetailDepartment();
        perfectStoreHoursByDay = PotentialHoursCalculator.createPerfectHoursList(dailyStoreTurnOverShare, dailyStoreHours);
        dailyStoreHoursToMetProductivityTarget = PotentialHoursCalculator.createStoreHoursDeterminedByProductivityTargetList
                (productivityTarget, dailyStoreTurnOver, dailyStoreTurnOverShare, dailyStoreHours);
        dailyRetailHoursToMetProductivityTarget = createDailyRetailHoursToMetProductivityTarget();
        differenceBetweenPerfectAndActualHours = PotentialHoursCalculator.substractColumnsValues(dailyStoreHoursToMetProductivityTarget, dailyStoreHours);
    }

    public double getProductivityTarget() {
        return productivityTarget;
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

    public Map<String, List<Double>> getDailyDepartmentHoursByName() {
        return dailyDepartmentHoursByName;
    }

    public List<Double> getPerfectStoreHoursByDay() {
        return perfectStoreHoursByDay;
    }

    public List<Double> getDifferenceBetweenPerfectAndActualHours() {
        return differenceBetweenPerfectAndActualHours;
    }

    public List<Double> getDailyStoreHoursToMetProductivityTarget() {
        return dailyStoreHoursToMetProductivityTarget;
    }

    private void matchNamesInScheduleToThoseFromForecast() {
        DepartmentNameChecker
                .changeDepartmentNamesInScheduleToThoseFromForecast(monthlyDepartmentTurnOver, dailyDepartmentHoursByName);
    }

    private Map<String, List<Double>> crateNonRetailDepartmentsHoursMap() {
        Map<String, List<Double>> noneRetailDepartmentHoursByDay = new LinkedHashMap<>();

        Set<String> departmentNamesFromSchedule = dailyDepartmentHoursByName.keySet();

        for (String departmentName : departmentNamesFromSchedule) {

            if (!monthlyDepartmentTurnOver.containsKey(departmentName)) {
                noneRetailDepartmentHoursByDay.put(departmentName, dailyDepartmentHoursByName.get(departmentName));
            }
        }
        return noneRetailDepartmentHoursByDay;
    }

    private List<Double> calcDailySumOfNonRetailDepartment() {
        List<Double> sumOfDailyNonRetailHours = new ArrayList<>();
        for (int i = 0; i < dailyStoreHours.size(); i++) {
            sumOfDailyNonRetailHours.add(0.0);
        }

        Set<String> noneRetailDepartmentNames = dailyNoneRetailDepartmentHours.keySet();

        for (String noneRetailDepartmentName : noneRetailDepartmentNames) {

            List<Double> singleDepartmentList = dailyDepartmentHoursByName.get(noneRetailDepartmentName);
            int daysNumber = sumOfDailyNonRetailHours.size();

            for (int i = 0; i < daysNumber; i++) {

                double dailyHours = singleDepartmentList.get(i);
                double presentDailyHours = sumOfDailyNonRetailHours.get(i);
                double newDailyHours = presentDailyHours + dailyHours;
                sumOfDailyNonRetailHours.set(i, newDailyHours);
            }
        }
        return sumOfDailyNonRetailHours;
    }

    public Map<String, List<Double>> getDailyNoneRetailDepartmentHours() {
        return dailyNoneRetailDepartmentHours;
    }

    public List<Double> getDailySumOfNonRetailHours() {
        return dailySumOfNonRetailHours;
    }

    private List<Double> createDailyRetailHoursToMetProductivityTarget() {
        List<Double> dailyRetailHoursToMetProductivityTarget = new ArrayList<>();

        for (int i = 0; i < dailySumOfNonRetailHours.size(); i++) {
            double dailySumStoreHours = dailyStoreHours.get(i);
            double dailySumNoneRetailHours = dailySumOfNonRetailHours.get(i);
            dailyRetailHoursToMetProductivityTarget.add(dailySumStoreHours-dailySumNoneRetailHours);
        }
        return dailyRetailHoursToMetProductivityTarget;
    }

    public List<Double> getDailyRetailHoursToMetProductivityTarget() {
        return dailyRetailHoursToMetProductivityTarget;
    }

    private double sumOfForecastedTuroverByDepartmets(){
        double sum = 0;

        Set<String> deparmentsNames = monthlyDepartmentTurnOver.keySet();
        for (String deparmentsName : deparmentsNames) {
            Double departmentTurnover = monthlyDepartmentTurnOver.get(deparmentsName);
            sum += departmentTurnover;
        }
        return sum;
    }

    public double getStoreForcastedByDepartmentsTurnOver() {
        return storeForcastedByDepartmentsTurnOver;
    }
}
