package mn.delivery.system.model.location;

import lombok.*;
import mn.delivery.system.model.BaseEntityWithoutId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Sharded;

/**
 * Байршил (аймаг, хот, дүүрэг, хороо)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Sharded(shardKey = {"code"})
public class Location extends BaseEntityWithoutId {

    @Id
    private String code; // 2, 4, 6 оронтой код

    private String parentCode;

    private String xypCode;
    private String name;
    private int order;
}
