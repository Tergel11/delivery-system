package mn.delivery.system.dto.auth.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author digz6666
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TwoFaRequest {
    private String email;
    private String using2fa;
}
