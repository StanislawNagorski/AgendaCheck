package org.AgendaCheck.Data;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DepartmentNameChecker {

    private static boolean commonPairCheck(String[] namePair, String name1, String name2) {
        boolean pair1A = name1.toLowerCase().contains(namePair[0]) || name1.toLowerCase().contains(namePair[1]);
        boolean pair1B = name2.toLowerCase().contains(namePair[0]) || name2.toLowerCase().contains(namePair[1]);
        return pair1A && pair1B;
    }

    private static boolean commonNamesReplacements(String name1, String name2) {
        String[] pair1 = {"fitnes", "forma"};
        if (commonPairCheck(pair1, name1, name2)) {
            return true;
        }
        String[] pair2 = {"turystyka", "góry"};
        if (commonPairCheck(pair2, name1, name2)) {
            return true;
        }
        String[] pair3 = {"oxelo", "rolki"};
        if (commonPairCheck(pair3, name1, name2)) {
            return true;
        }
        String[] pair4 = {"wedze", "zima"};
        if (commonPairCheck(pair4, name1, name2)) {
            return true;
        }
        String[] pair5 = {"turystyka", "quechua"};
        if (commonPairCheck(pair5, name1, name2)) {
            return true;
        }
        String[] pair6 = {"góry", "quechua"};
        if (commonPairCheck(pair6, name1, name2)) {
            return true;
        }

        return false;
    }


    protected static boolean namesCheck(String forecastName, String scheduleName) {
        if (forecastName.equalsIgnoreCase(scheduleName)) {
            return true;
        }

        if (commonNamesReplacements(forecastName, scheduleName)) {
            return true;
        }

        String[] splitedScheduleName = scheduleName.split("(\\s|\\/)");
        for (String s : splitedScheduleName) {
            if (s.equals("")){
                continue;
            }

            if (forecastName.toLowerCase().contains(s.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public static void changeDepartmentNamesFromScheduleToThoseFromForecast
            (Map<String, Double> monthTurnoverByDepartment, Map<String, List<Double>> dailyHoursByDepartment) {

        Set<Map.Entry<String, Double>> turnoverSet = monthTurnoverByDepartment.entrySet();
        Set<Map.Entry<String, List<Double>>> hoursSet = dailyHoursByDepartment.entrySet();

        Map<String, List<Double>> tempMap = new LinkedHashMap<>();

        for (Map.Entry<String, Double> turnoverEntry : turnoverSet) {
            for (Map.Entry<String, List<Double>> hoursEntry : hoursSet) {

                boolean isNameMatching = namesCheck(turnoverEntry.getKey(), hoursEntry.getKey());
                if (isNameMatching) {
                    String keyToChange = hoursEntry.getKey();
                    String keyNew = turnoverEntry.getKey();
                    List<Double> listToKeep = dailyHoursByDepartment.remove(keyToChange);
                    tempMap.put(keyNew, listToKeep);
                    break;
                }
            }
        }

        dailyHoursByDepartment.putAll(tempMap);


    }


}
