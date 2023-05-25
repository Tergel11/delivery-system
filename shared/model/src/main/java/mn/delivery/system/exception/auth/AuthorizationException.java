package mn.delivery.system.exception.auth;

/**
 * @author digz6666
 */
public class AuthorizationException extends RuntimeException {

    public AuthorizationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public AuthorizationException(String msg) {
        super(msg);
    }
}
