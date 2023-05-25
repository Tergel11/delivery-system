package mn.delivery.system.api.request.email;

import java.io.File;
import java.util.List;

import jakarta.validation.constraints.NotBlank;

import org.springframework.util.ObjectUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author digz6666
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateEmailRequest {

    @NotBlank
    private String to;
    @NotBlank
    private String subject;
    private String content;
    private List<File> attachments;

    private String title;
    private String description;

    // action
    private String linkText;
    private String link;
    private String mobile;

    private List<String> emails;

    public String[] getReceiverEmails() {
        if (ObjectUtils.isEmpty(emails))
            return null;

        return emails.toArray(new String[0]);
    }

    private String requestId;
}
