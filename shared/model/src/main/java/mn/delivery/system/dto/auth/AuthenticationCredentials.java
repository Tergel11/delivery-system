package mn.delivery.system.dto.auth;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import mn.delivery.system.model.auth.BusinessRole;
import mn.delivery.system.model.auth.User;
import mn.delivery.system.model.auth.enums.ApplicationRole;
import org.springframework.util.StringUtils;

import lombok.Builder;
import lombok.Data;

/**
 * @author digz6666
 */
@Data
@Builder
public class AuthenticationCredentials {
    private String userId;
    private String email;
    private String fullName;
    private String firstName;
    private Boolean using2fa;
    private String businessRole;
    private String businessRoleName;
    private List<ApplicationRole> applicationRoles;

    public static AuthenticationCredentials buildCredentials(User user, BusinessRole businessRole) {
        AuthenticationCredentials credentials = AuthenticationCredentials.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .using2fa(user.isUsing2fa())
                .businessRole(businessRole.getRole())
                .businessRoleName(businessRole.getName())
                .applicationRoles(businessRole.getApplicationRoles().stream()
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()))
                .build();
        credentials.setFullName(credentials.getFullName(user));
        return credentials;
    }

    public String getFullName(User user) {
        boolean hasFirstName = StringUtils.hasText(user.getFirstName());
        boolean hasLastName = StringUtils.hasText(user.getLastName());
        if (hasFirstName && hasLastName) {
            return user.getLastName().charAt(0) + "." + user.getFirstName();
        }
        return hasFirstName ? user.getFirstName() : user.getEmail();
    }
}
