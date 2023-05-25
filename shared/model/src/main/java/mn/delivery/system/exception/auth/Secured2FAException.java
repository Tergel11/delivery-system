package mn.delivery.system.exception.auth;

public class Secured2FAException extends RuntimeException {

    public Secured2FAException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public Secured2FAException(String msg) {
        super(msg);
    }
}
