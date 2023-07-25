package mn.delivery.system.datafiller.auth;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mn.delivery.system.model.auth.BusinessRole;
import mn.delivery.system.model.auth.User;
import mn.delivery.system.model.auth.enums.ApplicationRole;
import mn.delivery.system.repository.auth.BusinessRoleRepository;
import mn.delivery.system.repository.auth.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author digz6666
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserDataFiller {

    private static final String EMAIL = "@test.mn";

    private final UserRepository userRepository;
    private final BusinessRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    private void fill() {
        try {
            createRolesAndUsers();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void createRolesAndUsers() {
        if (userRepository.count() == 0 && roleRepository.count() == 0) {
            log.info("Creating roles and users...");
            roleRepository.save(BusinessRole.builder()
                    .role("SUPER_ADMIN")
                    .name("SUPER_ADMIN")
                    .applicationRoles(ApplicationRole.getSuperAdminRole())
                    .build());
            roleRepository.save(BusinessRole.builder()
                    .role("CUSTOMER")
                    .name("CUSTOMER")
                    .applicationRoles(ApplicationRole.getCustomerRole())
                    .build());

            User dev = User.builder()
                    .email("dev" + EMAIL)
                    .mobile("88016606")
                    .password(passwordEncoder.encode("dev123"))
                    .lastName("Ast")
                    .firstName("Digz")
                    .role("SUPER_ADMIN")
                    .active(true)
                    .emailVerified(true)
                    .build();
            User customer = User.builder()
                    .email("user" + EMAIL)
                    .mobile("88881234")
                    .password(passwordEncoder.encode("user123"))
                    .lastName("Ast")
                    .firstName("Dev")
                    .role("CUSTOMER")
                    .active(true)
                    .emailVerified(true)
                    .build();

            userRepository.saveAll(List.of(dev, customer));
        }
    }
}
