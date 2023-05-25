package mn.delivery.system.model.systemconfig;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@Data
@NoArgsConstructor
public class SystemKeyValue {

    @Id
    private String key;
    private Object value; // String, Integer, Double, BigDecimal, ... ямар ч төрлийн утга байж болох тул анхааралтай байна уу

    private LocalDateTime createdDate;
    private LocalDateTime expiryDate;

    public SystemKeyValue(String key) {
        this.key = key;
    }
}
