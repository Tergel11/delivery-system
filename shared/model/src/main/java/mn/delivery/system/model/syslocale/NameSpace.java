package mn.delivery.system.model.syslocale;

import lombok.Data;
import lombok.EqualsAndHashCode;
import mn.delivery.system.model.BaseEntityWithUser;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
@EqualsAndHashCode(callSuper = true)
public class NameSpace extends BaseEntityWithUser {

    private String name;
    @Indexed(unique = true)
    private String value;

    private boolean active;
}

