package mn.delivery.system.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import mn.delivery.system.exception.auth.Secured2FAException;
import mn.delivery.system.service.permission.BasePermissionService;
import mn.delivery.system.util.LocalizationUtil;
import mn.delivery.system.service.TwoFAService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Component
public class Secured2FAHandler {

    private final LocalizationUtil localizationUtil;
    private final ObjectMapper objectMapper;
    private final BasePermissionService basePermissionService;
    private final TwoFAService twoFAService;

    public Secured2FAHandler(
            final LocalizationUtil localizationUtil,
            final ObjectMapper objectMapper,
            final BasePermissionService basePermissionService,
            final TwoFAService twoFAService
    ) {

        this.objectMapper = objectMapper;
        this.localizationUtil = localizationUtil;
        this.basePermissionService = basePermissionService;
        this.twoFAService = twoFAService;
    }

    @Around(" @annotation(mn.delivery.system.annotations.Secured2FA) && args(.., @RequestBody body)")
    public Object validateAspect(ProceedingJoinPoint pjp, final Object body)
            throws Secured2FAException {

        final HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        try {
            if (request.getMethod().equals("GET"))
                return pjp.proceed();

            if (body == null)
                throw new Secured2FAException(
                        localizationUtil.build("auth.two-fa.code-required", ""));

            // POST, PUT, DELETE
            final var loggedPrincipal = SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getPrincipal();

            if (loggedPrincipal instanceof String)
                throw new Secured2FAException(
                        localizationUtil.build("auth.bearer-token.required", ""));

            final var springUser = ((org.springframework.security.core.userdetails.User) loggedPrincipal);
            final String json = objectMapper.writeValueAsString(body);
            final ObjectNode node = objectMapper.readValue(json, ObjectNode.class);
            final var authCode = node.get("authCode");
            if (authCode.isNull())
                throw new Secured2FAException(
                        localizationUtil.build("auth.two-fa.code-required", ""));

            final var user = basePermissionService.checkPermission(springUser.getUsername());
            if (!twoFAService.verify(user, authCode.textValue()))
                throw new Secured2FAException(
                        localizationUtil.build("auth.two-fa.code-invalid", ""));

            return pjp.proceed();
        } catch (Throwable t) {
            log.error(localizationUtil.build("error.2fa-handler", ""));
            throw new Secured2FAException(t.getMessage());
        }
    }
}
