package mn.delivery.system.api;

import com.mongodb.MongoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mn.delivery.system.api.request.LoginRequest;
import mn.delivery.system.api.response.AuthResponse;
import mn.delivery.system.exception.model.ErrorMessage;
import mn.delivery.system.model.auth.BusinessRole;
import mn.delivery.system.model.auth.User;
import mn.delivery.system.repository.auth.BusinessRoleRepository;
import mn.delivery.system.repository.auth.UserRepository;
import mn.delivery.system.util.JwtTokenUtil;
import mn.delivery.system.service.TwoFAService;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

/**
 * @author digz6
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthApi {

    private final BusinessRoleRepository businessRoleRepository;
    private final MessageSource messageSource;
    private final JwtTokenUtil tokenUtil;
    private final PasswordEncoder passwordEncoder;
    private final TwoFAService twoFAService;
    private final UserRepository userRepository;

    @PostMapping("login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest loginRequest,
            Locale locale) {
        log.debug("Login request: " + loginRequest.getUsername());

        try {
            User user = userRepository.findByEmailAndDeletedFalse(loginRequest.getUsername());
            if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()))
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(messageSource.getMessage("login.usernameOrPasswordWrong", null, locale));

            if (!user.isActive())
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(messageSource.getMessage("login.accountDisabled", null, locale));

            if (!user.isEmailVerified())
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ErrorMessage.USER_EMAIL_NOT_VERIFIED.getValue());

            if (user.isUsing2fa()) {
                if (ObjectUtils.isEmpty(user.getSecretKey()))
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(ErrorMessage.SECRET_KEY_NOTFOUND.getValue());

                try {
                    boolean verified = twoFAService.verify(
                            loginRequest.getUsername().toLowerCase(),
                            user.getSecretKey(),
                            loginRequest.getCode());
                    if (!verified)
                        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                .body(ErrorMessage.TWOFA_AUTH_ERROR.getValue());
                } catch (URISyntaxException e) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(ErrorMessage.TWOFA_AUTH_ERROR.getValue());
                }
            }

            Optional<BusinessRole> businessRoleOpt = businessRoleRepository.findById(user.getRole());
            if (businessRoleOpt.isEmpty())
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(messageSource.getMessage("error.permission", null, locale));

            AuthResponse authResponse = new AuthResponse();
            authResponse.setId(user.getId());
            authResponse.setName(user.getFullName());
            authResponse.setEmail(user.getEmail());
            authResponse.setImage(null); // TODO
            authResponse.setToken(tokenUtil.generateToken(user.getEmail()));
            authResponse.setBusinessRole(businessRoleOpt.get());

            return ResponseEntity.ok(authResponse);
        } catch (MongoException | DataAccessException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(messageSource.getMessage("error.database", null, locale));
        }
    }

    @GetMapping("check-token")
    public ResponseEntity<?> checkToken(String token) {
        // log.debug("token: " + token);
        String username = tokenUtil.getUsernameFromToken(token);
        Date expirationDate = tokenUtil.getExpirationDateFromToken(token);
//        log.debug("expirationDate: " + expirationDate);
        if (!userRepository.existsByEmailAndDeletedFalse(username)
                || (expirationDate != null && expirationDate.before(new Date())))
            return ResponseEntity.badRequest().body(false);

        return ResponseEntity.ok(true);
    }

//    @GetMapping("refresh")
//    public ResponseEntity<?> refreshToken(HttpServletRequest request, Locale locale) {
//        String token = request.getHeader(tokenHeader);
//        if (ObjectUtils.isEmpty(token)) {
//            return ResponseEntity.badRequest().body("Token empty");
//        }
//
//        String username = tokenUtil.getUsernameFromToken(token);
//        try {
//            User user = userRepository.findByEmailAndDeletedFalse(username);
//            if (user == null || !user.isActive()) {
//                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not found");
//            }
//            if (!tokenUtil.canTokenBeRefreshed(token, user.getPasswordChangeDate())) {
//                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Password changed");
//            }
//
//            Optional<BusinessRole> businessRoleOpt = businessRoleRepository.findById(user.getBusinessRole());
//            if (businessRoleOpt.isEmpty()) {
//                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Role not found");
//            }
//
//            AuthResponse authResponse = new AuthResponse();
//            authResponse.setToken(tokenUtil.refreshToken(token));
//            authResponse.setBusinessRole(businessRoleOpt.get());
//
//            return ResponseEntity.ok(authResponse);
//        } catch (MongoException | DataAccessException e) {
//            log.error(e.getMessage(), e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageSource.getMessage("error.database", null, locale));
//        }
//    }

//    @PostMapping("reset-password")
//    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest resetRequest, Locale locale) {
//        try {
//            User user = userRepository.findByEmailAndDeletedFalse(resetRequest.getUsername());
//            if (user == null || !user.isActive()) {
//                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(messageSource.getMessage("error.permission", null, locale));
//            }
//
//            String passwordResetToken = RandomStringUtils.random(32, true, true);
//            user.setPasswordResetToken(passwordResetToken);
//            user.setPasswordResetTokenExpiryDate(LocalDateTime.now().plusMinutes(5L));
//            userRepository.save(user);
//
//            String url = String.format(passwordResetUrl, resetRequest.getUsername(), passwordResetToken);
//            boolean result = emailSenderService.send(
//                    "no-reply@astvision.mn",
//                    "no-reply@astvision.mn",
//                    resetRequest.getUsername(),
//                    "Нууц үг сэргээх",
//                    emailTemplateService.passwordReset(url),
//                    null);
//            if (!result) {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("И-мэйл явуулахад алдаа гарлаа");
//            }
//
//            return ResponseEntity.ok("И-мэйл илгээлээ");
//        } catch (MongoException | DataAccessException e) {
//            log.error(e.getMessage(), e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageSource.getMessage("error.database", null, locale));
//        }
//    }

//    @PostMapping("check-password-reset-token")
//    public ResponseEntity<?> checkPasswordResetToken(@RequestBody PasswordResetRequest resetRequest, Locale locale) {
//        try {
//            User user = userRepository.findByEmailAndDeletedFalse(resetRequest.getUsername());
//            if (user == null || !user.isActive()) {
//                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(messageSource.getMessage("error.permission", null, locale));
//            }
//
//            if (!Objects.equals(user.getPasswordResetToken(), resetRequest.getResetToken())
//                    || user.getPasswordResetTokenExpiryDate() == null
//                    || user.getPasswordResetTokenExpiryDate().isAfter(LocalDateTime.now())) {
//                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token буруу байна");
//            }
//
//            return ResponseEntity.ok(true);
//        } catch (MongoException | DataAccessException e) {
//            log.error(e.getMessage(), e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageSource.getMessage("error.database", null, locale));
//        }
//    }

//    @Secured("ROLE_DEFAULT")
//    @PostMapping("update-password")
//    public ResponseEntity<?> setPassword(@RequestBody LoginRequest updateRequest, Locale locale) {
//        try {
//            User user = userRepository.findByEmailAndDeletedFalse(updateRequest.getUsername());
//            if (user == null || !user.isActive()) {
//                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(messageSource.getMessage("error.permission", null, locale));
//            }
//
//            user.setPassword(passwordEncoder.encode(updateRequest.getPassword()));
//            user.setPasswordChangeDate(LocalDateTime.now());
//            userRepository.save(user);
//
//            return ResponseEntity.ok(true);
//        } catch (MongoException | DataAccessException e) {
//            log.error(e.getMessage(), e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageSource.getMessage("error.database", null, locale));
//        }
//    }
}
