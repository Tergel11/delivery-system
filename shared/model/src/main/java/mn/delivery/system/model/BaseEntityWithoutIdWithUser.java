package mn.delivery.system.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

/**
 * @author digz6666
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class BaseEntityWithoutIdWithUser extends BaseEntityWithoutId {

    @CreatedBy
    @JsonIgnore
    private String createdBy;

    @LastModifiedBy
    @JsonIgnore
    private String modifiedBy;
}
