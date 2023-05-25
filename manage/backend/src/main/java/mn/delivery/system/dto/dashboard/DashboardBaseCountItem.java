package mn.delivery.system.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Tergel
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardBaseCountItem implements Serializable {

    private String itemId;
    private String name;
    private String color;
    private int count;
    private String countText;

}
