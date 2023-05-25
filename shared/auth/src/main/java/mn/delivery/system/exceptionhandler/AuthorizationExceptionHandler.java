package mn.delivery.system.exceptionhandler;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import mn.delivery.system.dto.auth.response.AuthenticationFailureResponse;
import mn.delivery.system.exception.auth.AuthorizationException;
import mn.delivery.system.exception.model.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

/**
 * @author digz6666
 */
@Slf4j
@RestControllerAdvice
public class AuthorizationExceptionHandler extends GlobalExceptionHandler {

//    @ExceptionHandler({AccessDeniedException.class})
//    protected ResponseEntity<AuthenticationFailureResponse> handleAuthorizationException(AccessDeniedException ex, WebRequest request) {
//        log.error("AccessDeniedException : " + ex.getMessage());
//        return buildResponse(ex, ErrorMessage.FORBIDDEN, HttpStatus.FORBIDDEN, request);
//    }

    @ExceptionHandler({AuthorizationException.class})
    protected ResponseEntity<AuthenticationFailureResponse> handleAuthorizationException(AuthorizationException ex, WebRequest request) {
        log.error("AuthorizationException : " + ex.getMessage());
//        return buildResponse(ex, ErrorMessage.UNAUTHORIZED), HttpStatus.UNAUTHORIZED, request);
        return buildResponse(ex, ErrorMessage.UNAUTHORIZED.getValue(), HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler({MalformedJwtException.class})
    protected ResponseEntity<AuthenticationFailureResponse> handleMalformedJwtException(MalformedJwtException ex, WebRequest request) {
        log.error("MalformedJwtException : " + ex.getMessage());
//        return buildResponse(ex, ErrorMessage.MALFORMED_JWT), HttpStatus.UNAUTHORIZED, request);
        return buildResponse(ex, ErrorMessage.MALFORMED_JWT.getValue(), HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler({ExpiredJwtException.class})
    protected ResponseEntity<AuthenticationFailureResponse> handleExpiredJwtException(ExpiredJwtException ex, WebRequest request) {
        log.error("ExpiredJwtException : " + ex.getMessage());
//        return buildResponse(ex, ErrorMessage.EXPIRED_JWT), HttpStatus.UNAUTHORIZED, request);
        return buildResponse(ex, ErrorMessage.EXPIRED_JWT.getValue(), HttpStatus.UNAUTHORIZED, request);
    }
}
