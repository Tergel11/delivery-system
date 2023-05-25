package mn.delivery.system.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import mn.delivery.system.service.permission.BasePermissionService;
import mn.delivery.system.util.LocalizationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Locale;

@Slf4j
public class BaseController {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    protected LocalizationUtil localizationUtil;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected BasePermissionService basePermissionService;

    public String getMessage(Locale locale, String code) {
        return messageSource.getMessage(code, null, locale);
    }

    public String getMessage(Locale locale, String code, String param) {
        return messageSource.getMessage(code, null, locale) + ": " + param;
    }

    public ResponseEntity<?> badRequest(String message) {
        return ResponseEntity.badRequest().body(message);
    }

    public ResponseEntity<?> badRequest() {
        return ResponseEntity.badRequest().body(
                localizationUtil.buildMessage("error.bad-request")
        );
    }

    public ResponseEntity<?> badRequestMessage(String message) {
        return ResponseEntity.badRequest().body(localizationUtil.buildMessage(message));
    }

    public ResponseEntity<?> badRequestMessage(String param, String arg) {
        return ResponseEntity.badRequest().body(localizationUtil.build(param, arg));
    }

    public ResponseEntity<?> badRequestLocale(Locale locale, String param) {
        return ResponseEntity.badRequest().body(messageSource.getMessage(param, null, locale));
    }

    public ResponseEntity<?> errorInvalidRequest(Locale locale) {
        return ResponseEntity.status(400).body(messageSource.getMessage("error.invalidRequest", null, locale));
    }

    public ResponseEntity<?> errorPermission(Locale locale) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(messageSource.getMessage("error.permission", null, locale));
    }

    public ResponseEntity<?> errorPermission() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(localizationUtil.buildMessage("error.permission"));
    }

    public ResponseEntity<?> errorApp(Locale locale) {
        return ResponseEntity.status(500).body(messageSource.getMessage("error.app", null, locale));
    }

    public ResponseEntity<?> errorNotFound(Locale locale) {
        return ResponseEntity.status(500).body(messageSource.getMessage("data.notFound", null, locale));
    }

    public ResponseEntity<?> errorDataExists(Locale locale) {
        return ResponseEntity.status(500).body(messageSource.getMessage("data.exists", null, locale));
    }

    public ResponseEntity<?> errorDatabase(Locale locale) {
        return ResponseEntity.status(500).body(messageSource.getMessage("error.database", null, locale));
    }

    public ResponseEntity<?> serverError(String message) {
        return ResponseEntity.status(500).body(message);
    }

    public ResponseEntity<?> serverErrorLocale(Locale locale, String param) {
        return ResponseEntity.status(500).body(messageSource.getMessage(param, null, locale));
    }
}
