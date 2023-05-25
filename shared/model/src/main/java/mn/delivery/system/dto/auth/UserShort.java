package mn.delivery.system.dto.auth;

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
public class UserShort {
    private boolean using2fa;
}
