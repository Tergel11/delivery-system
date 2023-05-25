package mn.delivery.system.model.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.Builder.Default;
import mn.delivery.system.model.auth.enums.RegisterType;
import mn.delivery.system.model.BaseEntityWithUser;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Sharded;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * @author digz6666
 */
@Sharded(shardKey = { "id", "email" })
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntityWithUser {

    private String email;
    private String registryNumber;
    private String mobile;

    private String password;
    private String lastName;
    private String firstName;
    private String address;

    // used for 2fa auth
    @JsonIgnore
    private String secretKey;
    private boolean using2fa;

    // for role
    private String role;
    private String profileImageUrl;
    private boolean active;

    private boolean emailVerified;
    private boolean mobileVerified;
    private boolean kycVerified;

    private LocalDateTime lastPasswordResetDate;
    private LocalDateTime lastForgotPasswordResetDate;

    // хэрэглэгчийн бүртгүүлсэн төрөл
    private RegisterType type;
    private boolean registerEmailSent;

    // хэрэглэгчийн оршин суугаа хаяг
    @Default
    private String addressCountryCode = "1";
    private String addressProvinceCode;
    private String addressDistrictCode;
    private String addressQuarterCode;
    private String addressDetail;

    /*
     * Referral дата
     */
    private String referredUserId; // refer хийсэн хэрэглэгчийн ID
    private boolean referApproved; // refer баталгаажсан

    // firebase data
    @JsonIgnore
    private UserSource source; // EMAIL, FIREBASE
    @JsonIgnore
    private String externalId; // firebase uid

    @Transient
    public String getFullName() {
        boolean hasFirstName = StringUtils.hasText(firstName);
        boolean hasLastName = StringUtils.hasText(lastName);
        if (hasFirstName && hasLastName) {
            return lastName.charAt(0) + "." + firstName;
        }
        return hasFirstName ? firstName : email;
    }

    @Transient
    public String fullAddress;

    @Transient
    public boolean isAddressFilled() {
        return StringUtils.hasLength(addressCountryCode) && StringUtils.hasLength(addressProvinceCode)
                && StringUtils.hasLength(addressDistrictCode)
                && StringUtils.hasLength(addressQuarterCode) && StringUtils.hasLength(addressDetail);
    }
}
