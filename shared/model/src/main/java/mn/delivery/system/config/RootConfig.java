package mn.delivery.system.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * @author digz6666
 */
@Slf4j
@Configuration
public class RootConfig {

    @Bean
    public MessageSource messageSource() {
        log.info("Configuring message source...");
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
        messageSource.setBasename("classpath:/messages/messages");
        messageSource.setCacheSeconds(0);
//        messageSource.setBasename("https://dev-config.astvision.mn/config-api/messages/messages");
//        messageSource.setCacheSeconds(30 * 60); // 30 minutes
        messageSource.setUseCodeAsDefaultMessage(true); // don't throw exception when code is not found
        return messageSource;
    }

    @Bean
    public LocaleResolver localeResolver() {
        log.info("Configuring locale resolver...");
        // locale comes from Accept-Language header
        AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
        localeResolver.setDefaultLocale(new Locale("mn"));
        return localeResolver;
    }
}
