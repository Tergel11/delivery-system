package mn.delivery.system.api.request;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * @author Tergel
 */
@Data
public class AppVersionUpdateRequest {
    @NotBlank
    private String version;
}
