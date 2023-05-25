package mn.delivery.system.service;

import java.time.LocalDateTime;

import mn.delivery.system.util.TemplateUtil;
import mn.delivery.system.model.email.Email;
import mn.delivery.system.model.email.EmailConfirmation;
import mn.delivery.system.model.email.enums.ConfirmationType;
import mn.delivery.system.model.email.enums.EmailType;
import mn.delivery.system.api.request.email.CreateEmailRequest;
import mn.delivery.system.repository.email.EmailConfirmationRepository;
import mn.delivery.system.repository.email.EmailRepository;
import mn.delivery.system.util.UUIDGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author digz6666
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${auth.frontend.url}")
    private String url;

    private final TemplateUtil templateUtil;
    private final EmailSender emailSender;
    private final EmailRepository emailRepository;
    private final EmailConfirmationRepository confirmationRepository;

    public boolean sendVerifyEmail(String userEmail) {
        String token = UUIDGenerator.generateType4UUID().toString();
        String link = url + EmailType.VERIFY_EMAIL.getUrl() + token;
        String content = templateUtil.verifyEmailTemplate(link);

        CreateEmailRequest request = new CreateEmailRequest();
        request.setTo(userEmail);
        request.setSubject(EmailType.VERIFY_EMAIL.getText());
        request.setContent(content);
        request.setLink(link);

        Email email = emailSender.send(request);
        if (email.isResult()) {
            EmailConfirmation confirmation = EmailConfirmation.builder()
                    .email(userEmail)
                    .token(token)
                    .type(ConfirmationType.VERIFY_EMAIL)
                    .expiredDate(LocalDateTime.now().plusHours(1))
                    .build();
            confirmation.setCreatedDate(LocalDateTime.now());
            confirmationRepository.save(confirmation);
        }

        email.setConfirmationEmail(true);
        email.setConfirmationType(ConfirmationType.VERIFY_EMAIL);
        email.setType(EmailType.VERIFY_EMAIL);
        emailRepository.save(email);

        return email.isResult();
    }

    @Transactional
    public boolean sendRemoveMobileEmail(String userEmail, String userMobile) {
        String token = UUIDGenerator.generateType4UUID().toString();
        String link = url + EmailType.REMOVE_MOBILE.getUrl() + token;
        String content = templateUtil.removeMobileEmailTemplate(link, userMobile);

        CreateEmailRequest request = new CreateEmailRequest();
        request.setTo(userEmail);
        request.setSubject(EmailType.REMOVE_MOBILE.getText());
        request.setContent(content);
        request.setLink(link);
        request.setMobile(userMobile);

        Email email = emailSender.send(request);
        if (email.isResult()) {
            EmailConfirmation confirmation = EmailConfirmation.builder()
                    .email(userEmail)
                    .token(token)
                    .type(ConfirmationType.REMOVE_MOBILE)
                    .expiredDate(LocalDateTime.now().plusHours(1))
                    .build();
            confirmation.setCreatedDate(LocalDateTime.now());
            confirmationRepository.save(confirmation);
        }

        email.setConfirmationEmail(true);
        email.setConfirmationType(ConfirmationType.REMOVE_MOBILE);
        email.setType(EmailType.REMOVE_MOBILE);
        emailRepository.save(email);

        return email.isResult();
    }

    public boolean sendNewUserEmail(String userEmail, String password) {
        String content = templateUtil.newUserEmailTemplate(password);
        String publicUrl = url;

        CreateEmailRequest request = new CreateEmailRequest();
        request.setTo(userEmail);
        request.setSubject(EmailType.NEW_USER.getText());
        request.setContent(content);
        request.setLink(publicUrl + EmailType.NEW_USER.getUrl());

        Email email = emailSender.send(request);
        email.setType(EmailType.NEW_USER);
        emailRepository.save(email);
        return email.isResult();
    }

    public boolean sendPasswordResetEmail(String userEmail) {
        String token = UUIDGenerator.generateType4UUID().toString();

        String link = url + EmailType.RESET_PASSWORD.getUrl() + token;
        String content = templateUtil.resetPasswordTemplate(link);

        CreateEmailRequest request = new CreateEmailRequest();
        request.setTo(userEmail);
        request.setSubject(EmailType.RESET_PASSWORD.getText());
        request.setContent(content);
        request.setLink(link);

        Email email = emailSender.send(request);
        if (email.isResult()) {
            EmailConfirmation confirmation = EmailConfirmation.builder()
                    .email(userEmail)
                    .token(token)
                    .type(ConfirmationType.RESET_PASSWORD)
                    .expiredDate(LocalDateTime.now().plusHours(1))
                    .build();
            confirmation.setCreatedDate(LocalDateTime.now());
            confirmationRepository.save(confirmation);
        }

        email.setConfirmationEmail(true);
        email.setConfirmationType(ConfirmationType.RESET_PASSWORD);
        email.setType(EmailType.RESET_PASSWORD);
        emailRepository.save(email);

        return email.isResult();
    }
}
