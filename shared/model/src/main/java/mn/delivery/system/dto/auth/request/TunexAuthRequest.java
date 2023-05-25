package mn.delivery.system.dto.auth.request;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TunexAuthRequest implements Serializable {

    private String email;
    private String password;
}
