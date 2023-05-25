package mn.delivery.system.service;

import com.bastiaanjansen.otp.SecretGenerator;
//import com.bastiaanjansen.otp.TOTP;
import com.bastiaanjansen.otp.TOTPGenerator;
import java.net.URI;
import java.net.URISyntaxException;
import lombok.extern.slf4j.Slf4j;
import mn.delivery.system.model.auth.User;
import org.springframework.stereotype.Service;

/**
 * @author digz6666
 */
@Slf4j
@Service
public class TwoFAService {

    //    @Value("${2fa.issuer}")
    private static final String issuer = "Astvision";
    private static final String BASE_URL = "otpauth://totp/%s?secret=%s&issuer=%s&algorithm=SHA1&digits=6&period=30";
    private static final String QR_PREFIX = "https://chart.googleapis.com/chart?chs=300x300&chld=M%%7C0&cht=qr&chl=";

    public TOTPGenerator totpGeneratorInstance(String email, String secret)
            throws URISyntaxException {
//        TOTP totp = new TOTP.Builder(secret.getBytes()).build();
        log.info("2FA Instance email : " + email);
//        return totp.getURI(issuer, email);
        return TOTPGenerator
                .Builder
                .fromOTPAuthURI(buildAuthUri(email, secret));
    }

    public boolean verify(User user, String code) throws URISyntaxException {
//        TOTP totp = new TOTP.Builder(secret.getBytes()).build();

        return TOTPGenerator
                .Builder
                .fromOTPAuthURI(buildAuthUri(user.getEmail(), user.getSecretKey()))
                .verify(code);
    }

    public boolean verify(String email, String secret, String code)
        throws URISyntaxException {
        return TOTPGenerator
                .Builder
                .fromOTPAuthURI(buildAuthUri(email, secret))
                .verify(code);
    }

    /*
     *  For user create new 2FA
     * */
    public String createNewAuthUri(String email, String secret) {
        log.info("Create new 2FA auth uri for email : " + email);
        String baseUrl = String.format(BASE_URL, getName(email), secret, issuer);
        return QR_PREFIX + baseUrl;
    }

    /*
     * For user authenticate using 2FA
     * */
    public URI buildAuthUri(String email, String secret)
        throws URISyntaxException {
        String url = String.format(BASE_URL, getName(email), secret, issuer);
        return new URI(url);
    }

    public byte[] generateSecretKey() {
        return SecretGenerator.generate();
    }

    private String getName(String email) {
        return issuer + "%20%28" + email + "%29";
    }
}
