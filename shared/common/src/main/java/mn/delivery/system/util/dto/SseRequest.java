package mn.delivery.system.util.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SseRequest<T> {

    private String channelName;

    private int totalPercent;
    private int previousPercent;

    private T data;
}
