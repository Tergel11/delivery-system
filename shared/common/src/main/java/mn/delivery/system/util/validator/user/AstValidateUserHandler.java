package mn.delivery.system.util.validator.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mn.delivery.system.exception.aspect.ValidateUserException;
import mn.delivery.system.model.auth.User;
import mn.delivery.system.repository.auth.UserRepository;
import mn.delivery.system.util.LocalizationUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AstValidateUserHandler {

    private final LocalizationUtil localizationUtil;
    private final UserRepository userRepository;

    @Around("@annotation(mn.delivery.system.util.validator.user.AstValidateUser)")
    public Object validateAspect(final ProceedingJoinPoint pjp)
        throws ValidateUserException {

        try {
            final var loggedPrincipal = SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

            if (loggedPrincipal instanceof String) {
                throw new ValidateUserException(
                    localizationUtil.build("auth.bearer-token.required",
                        ""));
            }

            final var springUser = ((org.springframework.security.core.userdetails.User) loggedPrincipal);
            final User user = userRepository.findByEmailAndDeletedFalse(
                springUser.getUsername());
            if (user == null) {
                throw new ValidateUserException(
                    localizationUtil.build("wallet.user.no-permission",
                        null));
            }

            return pjp.proceed();
        } catch (Throwable t) {
            log.error("Throw error {}", t.getCause() != null ? t.getCause() : t.getMessage());
            log.error(localizationUtil.build("system.error", ""));
            throw new ValidateUserException(t.getMessage());
        }
    }
}
