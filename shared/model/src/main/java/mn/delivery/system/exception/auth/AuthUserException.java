package mn.delivery.system.exception.auth;

/**
 * @author digz6666
 */
public class AuthUserException extends RuntimeException {
    public AuthUserException(String message) {
        super(message);
    }

    public AuthUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
