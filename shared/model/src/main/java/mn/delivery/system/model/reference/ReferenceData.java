package mn.delivery.system.model.reference;

import lombok.Data;
import lombok.EqualsAndHashCode;
import mn.delivery.system.model.BaseEntityWithUser;
import mn.delivery.system.model.FileData;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class ReferenceData extends BaseEntityWithUser {

    @Indexed
    private String typeCode; // reference type code
    @Indexed
    private Map<String, String> name;
    private String code;
    private Map<String, String> description;
    private Integer order;
    private FileData icon;
    private boolean used = true;
    private boolean isName = true;
    private boolean active = true;

    @Transient
    private String createdUserFullName;
    @Transient
    private Map<String, String> typeName;
}
