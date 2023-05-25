package mn.delivery.system.model.syslanguage;

import lombok.Data;
import lombok.EqualsAndHashCode;
import mn.delivery.system.model.BaseEntityWithUser;

@Data
@EqualsAndHashCode(callSuper = true)
public class SysLanguage extends BaseEntityWithUser {

    public static final String KNOWLEDGE_LEVEL = "KNOWLEDGE_LEVEL";

    private String name;
    private String code;
    private Integer order;

    private boolean active;
}

