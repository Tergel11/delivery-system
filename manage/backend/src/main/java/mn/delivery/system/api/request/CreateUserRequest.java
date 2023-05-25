package mn.delivery.system.api.request;

import lombok.Data;

/**
 * @author digz6666
 */
@Data
public class CreateUserRequest {
    private String id;
    private String email;
    private String registryNumber;
    private String mobile;
    private String password;
    private String lastName;
    private String firstName;
    private String address;

    private String role;
    private boolean active;
}
