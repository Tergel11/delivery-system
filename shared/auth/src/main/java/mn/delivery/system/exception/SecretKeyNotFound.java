package mn.delivery.system.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author digz6666
 */
public class SecretKeyNotFound extends AuthenticationException {
    public SecretKeyNotFound(String msg, Throwable cause) {
        super(msg, cause);
    }

    public SecretKeyNotFound(String msg) {
        super(msg);
    }
}
