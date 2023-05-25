package mn.delivery.system.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author digz6666
 */
@Slf4j
@Component
public class RegNumUtil {

    public static final Pattern REGEX = Pattern.compile("^[а-яА-ЯёүөЁҮӨ]{2}[0-9]{8}$", Pattern.UNICODE_CHARACTER_CLASS);

    public static boolean isValid(String registryNumber) {
        if (ObjectUtils.isEmpty(registryNumber))
            return false;

        Matcher matcher = REGEX.matcher(registryNumber);
        return matcher.find();
    }
}
