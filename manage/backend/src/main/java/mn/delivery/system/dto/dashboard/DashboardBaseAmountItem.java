package mn.delivery.system.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Tergel
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardBaseAmountItem implements Serializable {

    private String itemId;
    private String name;
    private String color;
    @Field("amount")
    private BigDecimal count;
    private String countText;

}
