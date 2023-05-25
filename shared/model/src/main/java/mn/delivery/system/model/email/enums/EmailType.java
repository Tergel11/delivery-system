package mn.delivery.system.model.email.enums;

import lombok.Getter;

/**
 * @author Tergel
 */
@Getter
public enum EmailType {

    BASE("BASE", null, null),
    BASE_ACTION("BASE_ACTION", null, null),
    VERIFY_EMAIL("VERIFY_EMAIL", "Имэйл хаяг баталгаажуулалт", "/user/email-verification/"),
    NEW_USER("NEW_USER", "Шинэ хэрэглэгч болсон танд баярлалаа", "/login"),
    RESET_PASSWORD("RESET_PASSWORD", "Нууц үг сэргээх хүсэлт", "/user/reset-password/verification/"),
    PLAIN_TEXT("PLAIN_TEXT", null, null),
    REMOVE_MOBILE("REMOVE_MOBILE", "Утасны дугаар устгах", "/user/remove-mobile/");

    final String value;
    final String text;
    final String url;

    EmailType(String value, String text, String url) {
        this.value = value;
        this.text = text;
        this.url = url;
    }

}
