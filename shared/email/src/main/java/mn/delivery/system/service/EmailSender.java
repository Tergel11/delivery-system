package mn.delivery.system.service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

//import org.springframework.beans.factory.annotation.Autowired;
import mn.delivery.system.util.EmailUtil;
import mn.delivery.system.model.email.Email;
import mn.delivery.system.api.request.email.CreateEmailRequest;
import mn.delivery.system.repository.email.EmailRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author digz6666
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailSender {

    @Value("${spring.mail.from}")
    private String from;

    @Value("${spring.mail.name}")
    private String name;

    private final EmailRepository emailRepository;
    private final JavaMailSender mailSender;

//    @Autowired(required = false)
//    public void setMailSender(JavaMailSender mailSender) {
//        this.mailSender = mailSender;
//    }

    public Email send(String to, String subject, String content, String link, List<File> attachments) {
        log.info("Sending email: {from: " + from + ", to: " + to + ", subject: " + subject + "}");

        Email email = Email.builder()
                .name(name)
                .from(from)
                .to(to)
                .subject(subject)
                .content(content)
                .link(link)
                .build();

        if (EmailUtil.isValid(to)) {
            try {
                MimeMessagePreparator messagePreparator = mimeMessage -> {
                    MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
                    messageHelper.setFrom(from, name);
                    messageHelper.setTo(to);
                    messageHelper.setSubject(subject);
                    messageHelper.setText(content, true); // html
                    if (attachments != null) {
                        for (File file : attachments) {
                            messageHelper.addAttachment(file.getName(), file);
                        }
                    }
                };
                mailSender.send(messagePreparator);
                email.setCreatedBy(to);
                email.setSentDate(LocalDateTime.now());
                email.setResult(true);
            } catch (MailException e) {
                email.setResult(false);
                email.setQueueSend(true);
                email.setErrorMessage("[MailException] : " + e.getMessage());
                log.error("[MailException] : " + e.getMessage());
            }
        } else {
            email.setErrorMessage("Invalid email address: " + to);
            email.setResult(false);
            log.error("Invalid email address: " + to);
        }

        email.setCreatedDate(LocalDateTime.now());
        return emailRepository.save(email);
    }

    public Email send(CreateEmailRequest emailRequest) {
        log.info("Sending email: {from: " + from + ", to: " + emailRequest.getTo() + ", subject: "
                + emailRequest.getSubject() + ", requestId: " + emailRequest.getRequestId() + "}");

        Email email = Email.builder()
                .name(name)
                .from(from)
                .to(emailRequest.getTo())
                .subject(emailRequest.getSubject())
                .content(emailRequest.getContent())
                .link(emailRequest.getLink())
                .requestId(emailRequest.getRequestId())
                .build();

        if (ObjectUtils.isEmpty(emailRequest.getTo())) {
            email.setBcc(true);
            email.setEmails(emailRequest.getEmails());
        }

        try {
            MimeMessagePreparator messagePreparatory = mimeMessage -> {
                MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
                messageHelper.setFrom(from, name);
                if (ObjectUtils.isEmpty(emailRequest.getTo()))
                    messageHelper.setBcc(emailRequest.getReceiverEmails());
                else
                    messageHelper.setTo(emailRequest.getTo());
                messageHelper.setSubject(emailRequest.getSubject());
                messageHelper.setText(emailRequest.getContent(), true); // html
                if (emailRequest.getAttachments() != null && emailRequest.getAttachments().size() > 0) {
                    for (File file : emailRequest.getAttachments()) {
                        messageHelper.addAttachment(file.getName(), file);
                    }
                }
            };
            mailSender.send(messagePreparatory);
            email.setCreatedBy(emailRequest.getTo());
            email.setSentDate(LocalDateTime.now());
            email.setResult(true);
        } catch (MailException e) {
            email.setResult(false);
            email.setQueueSend(true);
            email.setErrorMessage("[MailException] : " + e.getMessage());
            log.error("[MailException] : " + e.getMessage());
        }

        email.setCreatedDate(LocalDateTime.now());
        return emailRepository.save(email);
    }
}
