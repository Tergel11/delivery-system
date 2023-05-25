package mn.delivery.system.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mn.delivery.system.model.auth.BusinessRole;

import java.time.LocalDateTime;

/**
 * @author digz6666
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    /*
    nextjs хэрэглэгчийн дата
     */
    private String id; // хэрэглэгчийн ID
    private String name; // харуулах нэр
    private String email; // мэйл хаяг
    private String image; // профайл зурагны URL
    private String avatarUrl;
    private String mobile;
//    private List<Tenant> tenantList;

    /*
    нэмэлт дата
     */
    private String token;
    private LocalDateTime expires;
    private BusinessRole businessRole;
}
