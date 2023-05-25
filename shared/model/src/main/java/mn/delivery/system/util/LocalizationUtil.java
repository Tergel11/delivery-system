package mn.delivery.system.util;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class LocalizationUtil {

    private final MessageSource messageSource;

    public String buildMessage(final String templateName) {
        return buildMessage(templateName, null);
    }

    public String buildMessage(final String templateName, final String[] additionalParam) {
        return this.messageSource.getMessage(templateName, additionalParam, LocaleContextHolder.getLocale());
    }

    public String build(final String templateName, final String additionalParam) {
        final var params = List.of(additionalParam != null ? additionalParam.split(",") : "");
        return this.messageSource.getMessage(templateName, params.toArray(), LocaleContextHolder.getLocale());
    }
}
