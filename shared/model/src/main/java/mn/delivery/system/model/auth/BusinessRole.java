package mn.delivery.system.model.auth;

import lombok.*;
import mn.delivery.system.model.auth.enums.ApplicationRole;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

/**
 * @author digz6666
 */
@Document
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessRole implements Serializable {

    @Id
    @NonNull
    private String role;
    private String name;
    private List<ApplicationRole> applicationRoles;
    private List<String> accessibleBusinessRoles;
    private boolean permitAllRoles;

    @Transient
    public String getKey() {
        return this.role;
    }
    @Transient
    public List<String> accessibleRoleNames;
}
