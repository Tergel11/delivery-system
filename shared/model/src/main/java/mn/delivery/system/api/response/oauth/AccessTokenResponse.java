package mn.delivery.system.api.response.oauth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author digz6666
 */
@Data
public class AccessTokenResponse {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("token_type")
    private String tokenType; // Bearer

    @JsonProperty("expires_in")
    private int expiresIn; // 60s

    @JsonProperty("refresh_token")
    private String refreshToken;

    private String scope;
}
