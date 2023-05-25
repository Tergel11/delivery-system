package mn.delivery.system.exceptionhandler;

import lombok.extern.slf4j.Slf4j;
import mn.delivery.system.exception.TwoFAException;
import mn.delivery.system.dto.auth.response.AuthenticationFailureResponse;
import mn.delivery.system.exception.auth.BusinessRoleException;
import mn.delivery.system.exception.model.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

/**
 * @author digz6666
 */
@Slf4j
@RestControllerAdvice
public class AuthenticationExceptionHandler extends GlobalExceptionHandler {

    @ExceptionHandler({TwoFAException.class})
    protected ResponseEntity<AuthenticationFailureResponse> handleTwoFAException(TwoFAException ex, WebRequest request) {
        log.error("TwoFAException : " + ex.getMessage());
//        return buildResponse(ex, messageService.get(ErrorMessage.TWOFA_AUTH_ERROR), HttpStatus.FORBIDDEN, request);
        return buildResponse(ex, ErrorMessage.TWOFA_AUTH_ERROR.getValue(), HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler({BadCredentialsException.class})
    protected ResponseEntity<AuthenticationFailureResponse> handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        log.error("BadCredentialsException : " + ex.getMessage());
//        return buildResponse(ex, messageService.get(ErrorMessage.BAD_CREDENTIALS), HttpStatus.FORBIDDEN, request);
        return buildResponse(ex, ErrorMessage.BAD_CREDENTIALS.getValue(), HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler({DisabledException.class})
    protected ResponseEntity<AuthenticationFailureResponse> handleDisabledException(DisabledException ex, WebRequest request) {
        log.error("DisabledException : " + ex.getMessage());
//        return buildResponse(ex, messageService.get(ErrorMessage.DISABLED), HttpStatus.FORBIDDEN, request);
        return buildResponse(ex, ErrorMessage.DISABLED.getValue(), HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler({UsernameNotFoundException.class})
    protected ResponseEntity<AuthenticationFailureResponse> handleUsernameNotFoundException(UsernameNotFoundException ex, WebRequest request) {
        log.error("UsernameNotFoundException : " + ex.getMessage());
//        return buildResponse(ex, messageService.get(ErrorMessage.USERNAME_NOTFOUND), HttpStatus.FORBIDDEN, request);
        return buildResponse(ex, ErrorMessage.USERNAME_NOTFOUND.getValue(), HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler({BusinessRoleException.class})
    protected ResponseEntity<AuthenticationFailureResponse> handleBusinessRoleException(BusinessRoleException ex, WebRequest request) {
        log.error("BusinessRoleException : " + ex.getMessage());
        return buildResponse(ex, null, HttpStatus.FORBIDDEN, request);
    }
}
