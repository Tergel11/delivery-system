package mn.delivery.system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificationTextDto {

    private String title;
    private String body;
}
