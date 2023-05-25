package mn.delivery.system.model.reference;

import lombok.Data;
import lombok.EqualsAndHashCode;
import mn.delivery.system.model.BaseEntityWithUser;
import mn.delivery.system.model.FileData;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class ReferenceType extends BaseEntityWithUser {

    public static final String KNOWLEDGE_LEVEL = "KNOWLEDGE_LEVEL";

    private Map<String, String> name;
    private String code;
    private Map<String, String> description;
    private FileData icon;

    private boolean active;
}

