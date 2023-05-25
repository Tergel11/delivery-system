package mn.delivery.system.model.email.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author digz6666
 */
@Getter
@AllArgsConstructor
public enum ConfirmationType {
    RESET_PASSWORD,
    VERIFY_EMAIL,
    REMOVE_MOBILE,
}
