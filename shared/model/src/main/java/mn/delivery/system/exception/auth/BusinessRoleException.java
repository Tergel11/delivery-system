package mn.delivery.system.exception.auth;

/**
 * @author digz6666
 */
public class BusinessRoleException extends RuntimeException {

    public BusinessRoleException(String message) {
        super(message);
    }

    public BusinessRoleException(String message, Throwable cause) {
        super(message, cause);
    }
}
