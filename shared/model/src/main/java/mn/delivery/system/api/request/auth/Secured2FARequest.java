package mn.delivery.system.api.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Secured2FARequest {

    // [Readme] : 2FA authentication code [E.G] : 123456
    @NotBlank(message = "2FA authentication code is required")
    private String authCode;
}
