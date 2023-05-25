package mn.delivery.system.dto.auth;

import lombok.Builder;
import lombok.Data;
import mn.delivery.system.model.auth.enums.ApplicationRole;

import java.util.List;

/**
 * @author digz6666
 */
@Data
@Builder
public class BusinessRoleDto {
    private String role;
    private List<ApplicationRole> roles;
}
