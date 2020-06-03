package org.AgendaCheck;

public class DepartmentNameChecker {


    public static boolean namesCheck(String forecastName, String scheduleName) {
        if (forecastName.equalsIgnoreCase(scheduleName)) {
            return true;
        }

        String[] splitedScheduleName = scheduleName.split("(\\s|\\/)");
        for (String s : splitedScheduleName) {
            if (forecastName.contains(s)) {
                return true;
            }
        }

        return false;
    }

}
