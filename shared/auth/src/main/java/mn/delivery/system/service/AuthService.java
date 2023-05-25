package mn.delivery.system.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mn.delivery.system.dto.auth.response.AuthenticationFailureResponse;
import mn.delivery.system.exception.model.ErrorMessage;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

/**
 * @author digz6666
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class AuthService {

    private static final String ERR_MSG = " error : {}";

    private final ObjectMapper objectMapper;

//    public void sendAuthenticationResponse(HttpServletRequest request, HttpServletResponse response, Exception e) {
//        String errMessage = e.getMessage();
//        int status = 401;
//        if (e instanceof IllegalArgumentException) {
//            status = 400;
//            errMessage = ErrorMessage.BAD_REQUEST.getValue();
//        }
//        if (e instanceof MismatchedInputException) {
//            status = 400;
//            errMessage = ErrorMessage.BAD_REQUEST.getValue();
//        }
//        if (e instanceof BadCredentialsException) {
//            status = 403;
//            errMessage = ErrorMessage.BAD_CREDENTIALS.getValue();
//        }
//        if (e instanceof DisabledException) {
//            status = 403;
//            errMessage = ErrorMessage.DISABLED.getValue();
//        }
//        if (e instanceof UsernameNotFoundException) {
//            status = 403;
//            errMessage = ErrorMessage.USERNAME_NOTFOUND.getValue();
//        }
//        if (e instanceof BusinessRoleException) {
//            status = 403;
//            errMessage = ErrorMessage.BUSINESS_ROLE_NOTFOUND.getValue();
//        }
//        if (e instanceof UserEmailNotVerifiedException) {
//            status = 403;
//            errMessage = ErrorMessage.USER_EMAIL_NOT_VERIFIED.name();
//        }
//        sendResponse(request, response, e, status, errMessage, "Authentication");
//    }

    public void sendAuthorizationResponse(HttpServletRequest request, HttpServletResponse response, Exception e) {
        String errMessage = e.getMessage();
        int status = 401;
        if (e instanceof MalformedJwtException) {
            errMessage = ErrorMessage.MALFORMED_JWT.getValue();
        }
        if (e instanceof ExpiredJwtException) {
            errMessage = ErrorMessage.EXPIRED_JWT.getValue();
        }
        if (e instanceof MongoException) {
            status = 500;
            errMessage = ErrorMessage.DATABASE.getValue();
        }

        sendResponse(request, response, e, status, errMessage, "Authorization");
    }

    public void sendResponse(
            HttpServletRequest request,
            HttpServletResponse response,
            Exception e,
            int status,
            String errMessage,
            String type) {
        log.error(type + ERR_MSG, errMessage);
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try {
            objectMapper.writeValue(response.getOutputStream(), AuthenticationFailureResponse.builder()
                    .status(status)
                    .timestamp(Instant.now().toEpochMilli())
                    .error(e.getClass().getSimpleName())
                    .message(errMessage)
                    .path(request.getRequestURI())
                    .build());
        } catch (IOException ex) {
            log.error(type + ERR_MSG, ex.getMessage());
        }
    }
}
