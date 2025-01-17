package com.wolfco.velocity;

public class utils {
    public static long[] quotientRemainder(long dividend, long divisor) {
        long quotient = dividend / divisor;
        long remainder = dividend % divisor;
        return new long[] { quotient, remainder };
    }

    public static String time(long start) {
        return time(start, System.currentTimeMillis());
    }

    public static String time(long start, long end) {
        long time = Math.round(end - start);
        long[] years = quotientRemainder(time, 31556952000L);
        long[] months = quotientRemainder(years[1], 2629743L);
        long[] days = quotientRemainder(months[1], 86400L);
        long[] hours = quotientRemainder(days[1], 3600L);
        long[] minutes = quotientRemainder(hours[1], 60L);
        long seconds = minutes[1];
        String returnString = "";
        if (years[0] > 0) {
            returnString += years[0] + "Years ";
        } else if (months[0] > 0) {
            returnString += months[0] + "Months ";
        } else if (days[0] > 0) {
            returnString += days[0] + "Days ";
        } else if (hours[0] > 0) {
            returnString += hours[0] + "Hours ";
        } else if (minutes[0] > 0) {
            returnString += minutes[0] + "Minutes ";
        } else {
            returnString += seconds + "Seconds ";
        }
        return returnString;
    }

    public static String nullCheck(String text) {
        if (text == null) {
            return "";
        } else {
            return text;
        }
    }

    public static String filterMessage(String message) {
        return message.replaceAll("@", "`@`").replaceAll("#", "`#`");
    }
}
