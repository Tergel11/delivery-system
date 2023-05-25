package mn.delivery.system.location;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class XypResponse {

    private boolean result;
    private String message;

    // from xyp
    /*
    0 - амжилттай
    1 - мэдээлэл олдсонгүй
    2 - ХУР алдаа
    */
    private int resultCode; // xyp result code
    private Object data; // xyp response data
}
