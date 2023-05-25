package mn.delivery.system.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.List;

/**
 * @author Tergel
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardBaseCount implements Serializable {

    @Field("_id")
    private String key;
    private int count;
    private List<DashboardBaseCountItem> items;

}
