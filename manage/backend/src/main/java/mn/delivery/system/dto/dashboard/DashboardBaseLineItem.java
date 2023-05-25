package mn.delivery.system.dto.dashboard;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

/**
 * @author Tergel
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardBaseLineItem implements Serializable {

    @Field("_id")
    private String x;
    private Integer y;

    @JsonIgnore
    private Integer count1;
    @JsonIgnore
    private Integer count2;
}
