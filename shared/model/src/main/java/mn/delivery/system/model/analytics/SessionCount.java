package mn.delivery.system.model.analytics;

import lombok.Data;
import lombok.EqualsAndHashCode;
import mn.delivery.system.model.BaseEntity;

import java.time.LocalDate;

/**
 * @author digz6666
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SessionCount extends BaseEntity {

    private LocalDate date;
    private long count;
}
