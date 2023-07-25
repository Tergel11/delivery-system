package mn.delivery.system.model.auth.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * @author digz6666
 */
@Getter
@AllArgsConstructor
public enum ApplicationRole {

    /*
     *  default role
     */
    ROLE_DEFAULT("ROLE_DEFAULT"),
    /*
     * customer roles
     */
    ROLE_CUSTOMER("ROLE_CUSTOMER"),

    /*
     * manage roles
     */
    ROLE_MANAGE_DEFAULT("ROLE_MANAGE_DEFAULT"),

    // dashboard
    ROLE_DASHBOARD_VIEW("ROLE_DASHBOARD_VIEW"),

    // хэрэглэгчийн төрөл
    ROLE_BUSINESS_ROLE_VIEW("ROLE_BUSINESS_ROLE_VIEW"),
    ROLE_BUSINESS_ROLE_MANAGE("ROLE_BUSINESS_ROLE_MANAGE"),

    // Лавлах сан дата
    ROLE_REFERENCE_DATA_VIEW("ROLE_REFERENCE_DATA_VIEW"),
    ROLE_REFERENCE_DATA_MANAGE("ROLE_REFERENCE_DATA_MANAGE"),

    // Лавлах сан төрөл
    ROLE_REFERENCE_TYPE_VIEW("ROLE_REFERENCE_TYPE_VIEW"),
    ROLE_REFERENCE_TYPE_MANAGE("ROLE_REFERENCE_TYPE_MANAGE"),

    // Системийн хэл
    ROLE_LOCALE_VIEW("ROLE_LOCALE_VIEW"),
    ROLE_LOCALE_MANAGE("ROLE_LOCALE_MANAGE"),

    // хэрэглэгч
    ROLE_USER_VIEW("ROLE_USER_VIEW"),
    ROLE_USER_MANAGE("ROLE_USER_MANAGE"),

    // push notification
    ROLE_DEVICE_TOKEN_VIEW("ROLE_DEVICE_TOKEN_VIEW"),
    ROLE_PUSH_NOTIFICATION_VIEW("ROLE_PUSH_NOTIFICATION_VIEW"),
    ROLE_PUSH_NOTIFICATION_MANAGE("ROLE_PUSH_NOTIFICATION_MANAGE"),

    // article
    ROLE_ARTICLE_VIEW("ROLE_ARTICLE_VIEW"), // харах
    ROLE_ARTICLE_MANAGE("ROLE_ARTICLE_MANAGE"), // удирдах

    // Mercahnt
    ROLE_MERCHANT_VIEW("ROLE_MERCHANT_VIEW"), // харах
    ROLE_MERCHANT_MANAGE("ROLE_MERCHANT_MANAGE"), // удирдах
    ;

    private final String value;

    public static List<ApplicationRole> getSuperAdminRole() {
        return List.of(
                ROLE_MANAGE_DEFAULT,
                ROLE_DASHBOARD_VIEW,
                ROLE_BUSINESS_ROLE_VIEW,
                ROLE_BUSINESS_ROLE_MANAGE,
                ROLE_REFERENCE_DATA_VIEW,
                ROLE_REFERENCE_DATA_MANAGE,
                ROLE_LOCALE_VIEW,
                ROLE_LOCALE_MANAGE,
                ROLE_USER_VIEW,
                ROLE_USER_MANAGE,
                ROLE_ARTICLE_MANAGE,
                ROLE_ARTICLE_VIEW,
                ROLE_MERCHANT_VIEW,
                ROLE_MERCHANT_MANAGE
        );
    }

    public static List<ApplicationRole> getCustomerRole() {
        return List.of(ROLE_CUSTOMER);
    }

    public static ApplicationRole fromString(String value) {
        try {
            return ApplicationRole.valueOf(value);
        } catch (Exception e) {
            return null;
        }
    }
}
