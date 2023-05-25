package mn.delivery.system.auth;

import lombok.extern.slf4j.Slf4j;
import mn.delivery.system.model.auth.User;
import mn.delivery.system.repository.auth.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@Disabled
@Slf4j
@SpringBootTest
public class PasswordEncoderTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Disabled
    @Test
    public void testFillUser() {
        userRepository.save(User.builder()
                .email("orgil.tuul1985@gmail.com")
                .password(passwordEncoder.encode(RandomStringUtils.randomAlphanumeric(16)))
                .role("CUSTOMER")
                .active(true)
                .emailVerified(true)
                .build());
    }
}
