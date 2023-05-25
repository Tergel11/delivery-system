package mn.delivery.system.dto.auth.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author digz6666
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationVerifyResponse {

    private String email;
    private boolean using2fa;
    private boolean usingMobile;
    private String mobile;
}
