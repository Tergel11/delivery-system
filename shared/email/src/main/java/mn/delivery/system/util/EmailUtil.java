package mn.delivery.system.util;

import org.springframework.util.StringUtils;

import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author digz6666
 */
public class EmailUtil {

    public static final Pattern EMAIL_REGEX = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");

    public static boolean isValid(String email) {
        return isValidEmail(email);
    }

    public static boolean isValidEmail(String email) {
        if (!StringUtils.hasText(email)) {
            return false;
        }
        Matcher matcher = EMAIL_REGEX.matcher(email);
        return matcher.find();
    }

    public static boolean isValidInternetAddress(String email) {
        try {
            InternetAddress emailAddress = new InternetAddress(email);
            emailAddress.validate();
            return true;
        } catch (AddressException e) {
            return false;
        }
    }


}
