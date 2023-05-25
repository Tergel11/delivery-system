package mn.delivery.system.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.extern.slf4j.Slf4j;

/**
 * @author digz6666
 */
@Slf4j
public class StringUtils {

    public static final Pattern PASSWORD_REGEX = Pattern.compile("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}",
            Pattern.UNICODE_CHARACTER_CLASS);

    /*
     * Convert String to UTF-16 hex string
     */
    public static String toHexString(String s) {
        return IntStream.range(0, s.length())
                .mapToObj(i -> "\\u" + Integer.toHexString(s.charAt(i) | 0x10000).substring(1))
                .collect(Collectors.joining());
    }

    public static boolean isValidPassword(String password) {
        if (!org.springframework.util.StringUtils.hasText(password)) {
            return false;
        }
        Matcher matcher = PASSWORD_REGEX.matcher(password);
        return matcher.find();
    }

    public static String toStarString(String s, String type) {
        switch (type) {
            case "mobile":
                s = s.substring(0, 2) + "****" + s.substring(s.length() - 2);
                break;
            default:
                break;
        }

        return s;
    }
}
