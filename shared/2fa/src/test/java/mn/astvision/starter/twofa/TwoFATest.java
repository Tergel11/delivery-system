package mn.astvision.starter.twofa;

import com.bastiaanjansen.otp.SecretGenerator;
import com.bastiaanjansen.otp.TOTPGenerator;
import lombok.extern.slf4j.Slf4j;
import mn.delivery.system.service.TwoFAService;
import org.apache.commons.codec.binary.Base32;
import org.junit.jupiter.api.*;

import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

/**
 * @author digz6666
 */
@Slf4j
@Disabled
public class TwoFATest {

    private static TwoFAService twoFAService;

//    public TwoFATest(TwoFAService twoFAService) {
//        this.twoFAService = twoFAService;
//    }

    @BeforeAll
    static void setup() {
        if (twoFAService == null) {
            twoFAService = new TwoFAService();
        }
        log.info("@BeforeAll - executes once before all test methods in this class");
    }

    @BeforeEach
    void init() {
        log.info("@BeforeEach - executes before each test method in this class");
    }

    @Test
    void testTOTPGeneratorTest() {
        byte[] secret = SecretGenerator.generate();
        String secretStr = new String(secret, StandardCharsets.UTF_8);
        log.info("secretStr : " + secretStr);
//        log.info("generateSecretKey secret : " + generateSecretKey());
    }

    @Test
    void testTOTPAuthUriTest() throws URISyntaxException {
        byte[] secret = SecretGenerator.generate();
        String authUri = twoFAService.createNewAuthUri("galt@gmail.com", new String(secret));
        log.info("authUri : " + authUri);

    }

    @Test
    void testTOTPTest() throws URISyntaxException {
        String secret = "WSPW26D5DVQQX3SYKLGZJ2TGRS2CXA6T";
        TOTPGenerator totpGenerator = twoFAService.totpGeneratorInstance("galt@gmail.com", secret);
        log.info("authUri : " + totpGenerator.verify("566428"));
    }

    @AfterEach
    void tearDown() {
        log.info("@AfterEach - executed after each test method.");
    }

    @AfterAll
    static void done() {
        log.info("@AfterAll - executed after all test methods.");
    }

    //For the SHA256 algorithm
    String generateSecretKey() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[256 / 8];
        random.nextBytes(bytes);
        Base32 base32 = new Base32();
        return base32.encodeToString(bytes);
    }
}
