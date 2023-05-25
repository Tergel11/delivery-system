package mn.delivery.system.api.request;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;

/**
 * @author digz6666
 */
@Data
public class IdRequest {
    @NotBlank
    private String id;
}
