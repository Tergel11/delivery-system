package mn.delivery.system.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

/**
 * @author digz6666
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class BaseEntity extends BaseEntityWithoutId {

    @Id
    private String id;

    @Transient
    public String getKey() {
        return this.id;
    }
}
