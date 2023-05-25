package mn.delivery.system.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Tergel
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserShortData {

    private String id;
    private String email;
    private String profileImageUrl;
    private String fullName;
}
