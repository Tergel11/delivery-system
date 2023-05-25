package mn.delivery.system.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import mn.delivery.system.constants.GlobalDateFormat;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author digz6666
 */
@Document
@Data
@NoArgsConstructor
public class BaseEntityWithoutId implements Serializable {

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @JsonIgnore
    private boolean deleted;

    @Transient
    public String getCreatedDateText() {
        if (createdDate != null) {
            return GlobalDateFormat.DATE_TIME.format(createdDate);
        } else {
            return null;
        }
    }
}
