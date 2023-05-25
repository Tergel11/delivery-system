package mn.delivery.system.exception.auth;

public class ModuleException extends RuntimeException {

    public ModuleException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public ModuleException(String msg) {
        super(msg);
    }
}
