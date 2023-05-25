package mn.delivery.system.util;

import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * second - 0-59 ,-*
 * minute - 0-59 ,-*
 * hour - 0-23 ,-*
 * day of month - 1-31 ,-*LW
 * month - 1-12 JAN-DEC ,-*
 * day of week - 1-7 SUN-SAT ,-*L#
 * year - 1970-2099 ,-*
 *
 * , - specific values
 * - - range of values
 * * - all values
 * L - last
 * W - weekday
 * LW - last weekday of month
 * # - Nth day of month
 * 
 * @author MethoD
 */

@Slf4j
public class CronExpressionUtil {
    public static String getOnetime(LocalDateTime dateTime) {
        StringBuilder sb = new StringBuilder();
        sb.append(dateTime.getSecond()).append(" ");
        sb.append(dateTime.getMinute()).append(" ");
        sb.append(dateTime.getHour()).append(" ");
        sb.append(dateTime.getDayOfMonth()).append(" "); // day of month
        sb.append(dateTime.getMonthValue()).append(" "); // month
        sb.append("* "); // day of week
        // sb.append(dateTime.getYear()); // year

        return sb.toString();
    }

    public static String getDaily(LocalTime time) {
        StringBuilder sb = new StringBuilder();
        sb.append(time.getSecond()).append(" ");
        sb.append(time.getMinute()).append(" ");
        sb.append(time.getHour()).append(" ");
        sb.append("* "); // day of month
        sb.append("* "); // month
        sb.append("*"); // day of week

        return sb.toString();
    }

    public static String getMonthlyDays(int[] months,
            int[] days, // ON_DAYS
            LocalTime time) {

        StringBuilder sb = new StringBuilder();
        sb.append(time.getSecond()).append(" ");
        sb.append(time.getMinute()).append(" ");
        sb.append(time.getHour()).append(" ");

        StringBuilder sb_day = new StringBuilder();
        for (int day : days) {
            if (day > 0) {
                if (sb_day.length() != 0) {
                    sb_day.append(",");
                }

                if (day == -1) { // LAST
                    // sb_day.append("L"); // dont support L
                } else {
                    sb_day.append(day);
                }
            }
        }
        sb.append(sb_day);
        sb.append(" ");

        // months
        StringBuilder sb_month = new StringBuilder();
        for (int month : months) {
            if (sb_month.length() != 0) {
                sb_month.append(",");
            }
            sb_month.append(month);
        }
        sb.append(sb_month).append(" ");
        sb.append("?");

        return sb.toString();
    }
}
