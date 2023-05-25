package mn.delivery.system.constants;

import java.time.format.DateTimeFormatter;

/**
 * @author digz6666
 */
public class GlobalDateFormat {

    public static DateTimeFormatter DATE_TIME_ISO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    public static DateTimeFormatter DATE_TIME_ONLY = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
    public static DateTimeFormatter DATE_TIME_FILE = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    public static DateTimeFormatter DATE = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    //    public static DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm:ss");
    public static DateTimeFormatter HOUR_MINUTE = DateTimeFormatter.ofPattern("HH:mm");

    //    public static DateTimeFormatter DATE_TIME_ISO_T_Z = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    public static DateTimeFormatter DATE_TIME_ISO_T = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
}
