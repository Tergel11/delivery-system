package mn.delivery.system.util;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * @author digz6666
 */
@Slf4j
@Service
@AllArgsConstructor
public class TemplateUtil {

    private final TemplateEngine templateEngine;

    public String resetPasswordTemplate(String link) {
        Context context = new Context();
        context.setVariable("link", link);
        return templateEngine.process("resetPasswordRaw", context);
    }

    public String verifyEmailTemplate(String link) {
        Context context = new Context();
        context.setVariable("link", link);
        return templateEngine.process("verifyEmailRaw", context);
    }

    public String removeMobileEmailTemplate(String link, String mobile) {
        Context context = new Context();
        context.setVariable("link", link);
        context.setVariable("mobile", mobile);
        return templateEngine.process("removeMobileEmailRaw", context);
    }

    public String newUserEmailTemplate(String password) {
        Context context = new Context();
        context.setVariable("password", password);
        return templateEngine.process("newUserRaw", context);
    }
}
