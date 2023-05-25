package mn.delivery.system.exceptionhandler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.mongodb.MongoException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import mn.delivery.system.dto.auth.response.AuthenticationFailureResponse;
import mn.delivery.system.exception.MessageException;
import mn.delivery.system.exception.aspect.ValidateUserException;
import mn.delivery.system.exception.auth.ModuleException;
import mn.delivery.system.exception.auth.Secured2FAException;
import mn.delivery.system.exception.model.ErrorMessage;
import mn.delivery.system.exception.permission.PermissionException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.NestedServletException;

/**
 * @author digz6666
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
        AccessDeniedException.class,
        PermissionException.class
    })
    protected ResponseEntity<AuthenticationFailureResponse> handleAccessDeniedError(
        Exception ex, WebRequest request) {
        log.error("AccessDeniedException : " + ex.getMessage());
        return buildResponse(ex, ErrorMessage.FORBIDDEN.getValue(),
            HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler({
        Secured2FAException.class,
        ValidateUserException.class,
        ModuleException.class
    })
    protected ResponseEntity<AuthenticationFailureResponse> handlePermissionAspectExceptions(
        Exception ex, WebRequest request) {
        final var message = !ObjectUtils.isEmpty(ex.getMessage()) ? ex.getMessage()
            : ErrorMessage.FORBIDDEN.getValue();

        log.error("Aspect Exceptions : " + message);
        return buildResponse(ex, message,
            HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler({
        NestedServletException.class,
    })
    protected ResponseEntity<AuthenticationFailureResponse> handleNestedServletException(
        Exception ex, WebRequest request) {
        return buildResponse(ex, ex.getMessage(),
            HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({
        IllegalArgumentException.class,
        MismatchedInputException.class
    })
    protected ResponseEntity<AuthenticationFailureResponse> handleAllRequestBodyError(
        Exception ex, WebRequest request) {
        log.error("Request body error : " + ex.getMessage());
        return buildResponse(ex, ErrorMessage.BAD_REQUEST.getValue(),
            HttpStatus.BAD_REQUEST,
            request);
    }

    @ExceptionHandler({
        MethodArgumentNotValidException.class
    })
    protected ResponseEntity<AuthenticationFailureResponse> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex, WebRequest request) {

        Map<String, String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errorMap.put(error.getField(), error.getDefaultMessage());
        });
        return buildResponse(ex, errorMap.toString(), HttpStatus.BAD_REQUEST,
            request);
    }

    @ExceptionHandler({
        HttpMessageNotReadableException.class,
    })
    protected ResponseEntity<AuthenticationFailureResponse> handleHttpMessageNotReadableException(
        HttpMessageNotReadableException exception,
        WebRequest request) {

        String genericMessage = "Message converter error: " + exception.getMessage();
        String errorDetails = genericMessage;
        if (exception.getCause() instanceof InvalidFormatException) {
            InvalidFormatException ifx = (InvalidFormatException) exception.getCause();
            if (ifx.getTargetType() != null && ifx.getTargetType().isEnum()) {
                errorDetails = String.format(
                    "Invalid enum value: '%s' for the field: '%s'. The value must be one of: %s.",
                    ifx.getValue(),
                    ifx.getPath().get(ifx.getPath().size() - 1).getFieldName(),
                    Arrays.toString(ifx.getTargetType().getEnumConstants()));
            }
        } else {
            errorDetails = ErrorMessage.BAD_REQUEST.getValue();
        }
        return buildResponse(exception, errorDetails, HttpStatus.BAD_REQUEST,
            request);
    }

    @ExceptionHandler({MongoException.class})
    protected ResponseEntity<AuthenticationFailureResponse> handleMongoException(
        Exception ex, WebRequest request) {
        log.error("MongoException : " + ex.getMessage());
        // return buildResponse(ex, messageService.get(ErrorMessage.DATABASE),
        // HttpStatus.BAD_REQUEST, request);
        return buildResponse(ex, ErrorMessage.DATABASE.getValue(),
            HttpStatus.INTERNAL_SERVER_ERROR,
            request);
    }

    @ExceptionHandler({
        NullPointerException.class,
        Exception.class
    })
    protected ResponseEntity<AuthenticationFailureResponse> handleNullPointerException(
        Exception ex, WebRequest request) {
        log.error(ex.getMessage(), ex);

        if (ex instanceof InterruptedException) {
            Thread.currentThread().interrupt();
        }

        // return buildResponse(ex, messageService.get(ErrorMessage.DATABASE),
        // HttpStatus.BAD_REQUEST, request);
        return buildResponse(ex, ErrorMessage.EXCEPTION.getValue(),
            HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler({
        MessageException.class
    })
    protected ResponseEntity<AuthenticationFailureResponse> handleMessageException(
        Exception ex, WebRequest request) {
        return buildResponse(
            ex,
            ex.getMessage(),
            HttpStatus.BAD_REQUEST,
            request);
    }

    protected ResponseEntity<AuthenticationFailureResponse> buildResponse(
        Exception exception,
        String message,
        HttpStatus status,
        WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAcceptCharset(List.of(StandardCharsets.UTF_8));
        return new ResponseEntity<>(
            AuthenticationFailureResponse.builder()
                .status(status.value())
                .timestamp(Instant.now().toEpochMilli())
                .error(exception.getClass().getSimpleName())
                .message(message != null ? message : exception.getMessage())
                .path(request.getContextPath())
                .build(),
            headers,
            status);
    }
}
