package mn.delivery.system.model.systemconfig;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mn.delivery.system.model.systemconfig.enums.SystemCronType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * @author MethoD
 */
@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemCron {

    @Id
    private SystemCronType cronType;
    private boolean enabled;
    private LocalDateTime lastRunAt;
}
