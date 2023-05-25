package mn.delivery.system.dto;

import lombok.Data;

@Data
public class FaceDetectResult {

    private int count;
    private boolean sunglasses;
}
