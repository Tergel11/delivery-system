package mn.delivery.system.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author Tergel
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardBaseAmount implements Serializable {

    @Field("_id")
    private String key;
    @Field("amount")
    private BigDecimal count;
    private List<DashboardBaseAmountItem> items;

}
