package mn.delivery.system.service;

import lombok.RequiredArgsConstructor;
import mn.delivery.system.exception.model.ErrorMessage;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * @author digz6666
 */
@Service
@RequiredArgsConstructor
public class ErrorMessageService {

    private final MessageSource messageSource;

    public String get(ErrorMessage type) {
        if (type == null) return null;
//        return messageSource.getMessage("ErrorMessage." + type.name(), null, new Locale("mn"));
        return messageSource.getMessage("ErrorMessage." + type.name(), null, Locale.getDefault());
    }
}
