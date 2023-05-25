package mn.delivery.system.model.email;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import mn.delivery.system.model.email.enums.ConfirmationType;
import mn.delivery.system.model.email.enums.EmailType;
import mn.delivery.system.model.BaseEntityWithUser;

/**
 * @author digz6666
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Email extends BaseEntityWithUser {
    private String from;
    private String name;
    private String to;
    private boolean bcc;
    private List<String> emails;
    private String subject;
    private String content;
    private String link;
    private boolean result;
    private LocalDateTime sentDate;
    private String errorMessage;
    private boolean queueSend;
    private boolean confirmationEmail;
    private ConfirmationType confirmationType;
    private EmailType type;

    private String requestId; // EmailRequest.id
}
