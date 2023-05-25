package mn.delivery.system.model.email;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import mn.delivery.system.model.email.enums.ConfirmationType;
import mn.delivery.system.model.BaseEntity;

/**
 * @author digz6666
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EmailConfirmation extends BaseEntity {
    private String email;
    private String token;
    private ConfirmationType type;
    private LocalDateTime expiredDate;
    private LocalDateTime confirmedDate;
}
