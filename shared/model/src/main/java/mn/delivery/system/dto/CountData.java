package mn.delivery.system.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CountData {

    @JsonProperty("_id")
    private String value;
    private long count;
}
