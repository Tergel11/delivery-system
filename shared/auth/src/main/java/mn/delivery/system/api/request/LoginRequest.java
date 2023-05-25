package mn.delivery.system.api.request;

import lombok.Data;

/**
 * @author digz6666
 */
@Data
public class LoginRequest {
    private String username;
    private String password;
    private String code; // 2fa code
}
