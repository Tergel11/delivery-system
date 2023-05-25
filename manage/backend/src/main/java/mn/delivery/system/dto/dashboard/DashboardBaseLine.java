package mn.delivery.system.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author Tergel
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardBaseLine implements Serializable {

    private String id;
    private String color;
    private List<DashboardBaseLineItem> data;
    private Integer count;

}
