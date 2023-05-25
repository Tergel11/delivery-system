package mn.delivery.system.constants;

import java.text.DecimalFormat;
import java.util.regex.Pattern;

/**
 * @author digz6666
 */
public class GlobalCoordinateFormat {

    public static final Pattern REGEX_DOUBLE_NUMBER = Pattern.compile("(\\d+\\.\\d+)");

    public static final DecimalFormat LAT_LON = new DecimalFormat("#.######");
}
