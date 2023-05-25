package mn.delivery.system.model.systemconfig;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mn.delivery.system.model.systemconfig.enums.SystemApiModuleType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author MethoD
 */
@Document
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SystemApiConf {

    @Id
    private SystemApiModuleType moduleType;
    private boolean enabled;
    private LocalDateTime lastRunAt;
}
