package mn.delivery.system.model.email;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import mn.delivery.system.model.email.enums.EmailReceiverType;
import mn.delivery.system.model.email.enums.EmailSendType;
import mn.delivery.system.model.email.enums.EmailType;
import mn.delivery.system.model.BaseEntityWithUser;
import org.springframework.data.annotation.Transient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Tergel
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EmailRequest extends BaseEntityWithUser {

    public static final int PRIORITY_HIGH = 100;
    public static final int PRIORITY_NORMAL = 50;
    public static final int PRIORITY_LOW = 0;

    @NotBlank
    private String subject;
    private String content;

    // base & action
    private String title;
    // action
    private String link;
    private String linkText;

    @NotNull
    private EmailType type;
    @NotNull
    private EmailReceiverType receiverType;
    @NotNull
    private EmailSendType sendType;

    private List<String> nftCollectionIds; // receiverType == OWNER
    private List<String> emails; // receiverType == EMAIL

    private LocalDateTime scheduledDate; // sendType == SCHEDULED
    private int priority;

    private String emailId; // Email.id
    private Boolean sendResult;
    private LocalDateTime sentDate;
    private String errorMessage;
    private boolean sending;

    @Transient
    private String email;
}
