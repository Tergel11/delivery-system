package mn.delivery.system.dto.auth.response;

import lombok.Builder;
import lombok.Data;

/**
 * @author digz6666
 */
@Data
@Builder
public class AuthenticationFailureResponse {
    private int status;
    private long timestamp;
    private String error;
    private String message;
    private String path;
}
