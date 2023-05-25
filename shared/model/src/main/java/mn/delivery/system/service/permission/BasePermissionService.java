package mn.delivery.system.service.permission;

import mn.delivery.system.model.auth.User;
import mn.delivery.system.util.LocalizationUtil;
import mn.delivery.system.exception.auth.AuthorizationException;
import mn.delivery.system.exception.permission.PermissionException;
import mn.delivery.system.repository.auth.UserRepository;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.security.Principal;
import java.util.Locale;

/**
 * @author Tergel
 */
@Component
public class BasePermissionService {

    private final MessageSource messageSource;
    private final LocalizationUtil localizationUtil;
    private final UserRepository userRepository;

    public BasePermissionService(
            final MessageSource messageSource,
            final LocalizationUtil localizationUtil,
            final UserRepository userRepository
    ) {
        this.messageSource = messageSource;
        this.localizationUtil = localizationUtil;
        this.userRepository = userRepository;
    }

    public User user(Principal principal, Locale locale) throws PermissionException {
        if (principal == null || ObjectUtils.isEmpty(principal.getName()))
            error(locale);

        User user = userRepository.findByEmail(principal.getName().toLowerCase());
        if (user == null)
            error(locale);

        return user;
    }

    public User findUser(Principal principal) throws PermissionException {
        if (principal == null || ObjectUtils.isEmpty(principal.getName()))
            throw new PermissionException();

        User user = userRepository.findByEmail(principal.getName().toLowerCase());
        if (user == null)
            throw new PermissionException();

        return user;
    }

    public User checkPermission(String email) throws AuthorizationException, PermissionException {
        User user = userRepository.findByEmail(email.toLowerCase());

        if (user == null) {
            error(Locale.getDefault());
        } else {
            if (!user.isUsing2fa()) {
                throw new AuthorizationException(
                        localizationUtil.build("auth.two-fa.not-enabled", null));
            }
        }
        return user;
    }

    public User user(Principal principal) {
        if (principal == null || ObjectUtils.isEmpty(principal.getName()))
            return null;

        return userRepository.findByEmail(principal.getName().toLowerCase());
    }

    public String getUserId(Principal principal) {
        if (principal == null || ObjectUtils.isEmpty(principal.getName()))
            return null;

        User user = userRepository.findByEmail(principal.getName().toLowerCase());
        return user != null ? user.getId() : null;
    }

    protected void error(String message, Locale locale)
            throws PermissionException {
        throw new PermissionException(
                ObjectUtils.isEmpty(message) ?
                        messageSource.getMessage("error.permission", null, locale)
                        : message
        );
    }

    protected void error(Locale locale) throws PermissionException {
        error(null, locale);
    }
}
