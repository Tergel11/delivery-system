package mn.delivery.system.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author digz6666
 */
public class InjectUserException extends AuthenticationException {

    public InjectUserException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public InjectUserException(String msg) {
        super(msg);
    }
}
