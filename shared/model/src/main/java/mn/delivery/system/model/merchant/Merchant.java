package mn.delivery.system.model.merchant;

import lombok.*;
import mn.delivery.system.model.BaseEntityWithUser;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Merchant extends BaseEntityWithUser {
    private String userId;
    private String name;
    private String bundleId; //Багц хэрэглэх бол ашиглана
    private String wareHouseId;
    private List<String> storeIds;
}
