package mn.delivery.system.model.syslocale;

import lombok.*;
import mn.delivery.system.model.BaseEntityWithUser;
import org.springframework.data.annotation.Transient;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SysLocale extends BaseEntityWithUser {

    private String nsId;
    private String lng;
    private String field;
    private String code; // fieldCode
    private String translation;

    private boolean active;

    @Transient
    private String nsName;

    @Transient
    private String lngName;
}

