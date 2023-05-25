package mn.delivery.system.api.auth;

import lombok.extern.slf4j.Slf4j;
import mn.delivery.system.model.auth.enums.ApplicationRole;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author MethoD
 */
@Slf4j
@RestController
@RequestMapping("/v1/application-role")
@Secured({"ROLE_BUSINESS_ROLE_VIEW", "ROLE_BUSINESS_ROLE_MANAGE"})
public class ApplicationRoleApi {

    @GetMapping
    public ApplicationRole[] list() {
        return ApplicationRole.values();
    }
}
