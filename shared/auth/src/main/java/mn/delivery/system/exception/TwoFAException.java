package mn.delivery.system.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author digz6666
 */
public class TwoFAException extends AuthenticationException {

    public TwoFAException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public TwoFAException(String msg) {
        super(msg);
    }
}
