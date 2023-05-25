package mn.delivery.system.api.request.email;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;

/**
 * @author digz6666
 */
@Data
public class RemoveMobileConfirmationRequest {
    @NotBlank
    private String token;
}
