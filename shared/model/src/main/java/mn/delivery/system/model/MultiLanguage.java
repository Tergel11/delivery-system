package mn.delivery.system.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
@EqualsAndHashCode(callSuper = true)
public class MultiLanguage extends BaseEntityWithUser {

    @Indexed
    private String code; // reference type code
    private String app;
    private String name;
    private String nomenclatureMN;
    private String nomenclatureEN;
    private Integer order;
    private boolean active = true;

    @Transient
    private String createdUserFullName;
    @Transient
    private String typeName;
}
