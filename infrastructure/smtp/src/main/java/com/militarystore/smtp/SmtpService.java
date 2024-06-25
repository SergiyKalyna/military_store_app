package com.militarystore.smtp;

import com.militarystore.entity.email.EmailDetails;
import com.militarystore.port.out.email.SendEmailPort;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.militarystore.smtp.util.EmailContentBuilder.buildContent;

@Service
@Slf4j
public class SmtpService implements SendEmailPort {

    private static final String CONTENT_TYPE = "text/html; charset=utf-8";

    private final String emailFrom;
    private final JavaMailSender emailSender;

    public SmtpService(
        @Value("${spring.mail.username}") String emailFrom,
        JavaMailSender emailSender
    ) {
        this.emailFrom = emailFrom;
        this.emailSender = emailSender;
    }

    @Override
    public void sendEmail(EmailDetails emailDetails) {
        try {
            var message = createMessage(emailDetails);

            emailSender.send(message);
            log.info("Email was sent to {}", emailDetails.recipientEmail());
        } catch (Exception e) {
            log.error("Failed to send email to {}", emailDetails.recipientEmail(), e);
        }
    }

    private MimeMessage createMessage(EmailDetails emailDetails) throws MessagingException, IOException {
        var message = emailSender.createMimeMessage();
        var content = buildContent(emailDetails.message(), emailDetails.username());

        message.setFrom(emailFrom);
        message.addRecipients(Message.RecipientType.TO, emailDetails.recipientEmail());
        message.setSubject(emailDetails.subject());
        message.setContent(content, CONTENT_TYPE);

        return message;
    }
}
