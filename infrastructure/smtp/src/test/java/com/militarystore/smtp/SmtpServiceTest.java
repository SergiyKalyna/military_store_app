package com.militarystore.smtp;

import com.militarystore.entity.email.EmailDetails;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import static jakarta.mail.Message.RecipientType.TO;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SmtpServiceTest {

    private static final String EMAIL_FROM = "email-from@gmail.com";

    @Mock
    private JavaMailSender emailSender;

    private SmtpService smtpService;

    @BeforeEach
    void setUp() {
        System.setProperty("spring.mail.username", EMAIL_FROM);
        smtpService = new SmtpService(EMAIL_FROM, emailSender);
    }

    @Test
    void sendEmail() throws MessagingException {
        var emailDetails = EmailDetails.builder()
            .message("your order is ready")
            .recipientEmail("recipient@gmail.com")
            .subject("Test email")
            .username("Mr Smith")
            .build();

        var message = mock(MimeMessage.class);
        when(emailSender.createMimeMessage()).thenReturn(message);

        smtpService.sendEmail(emailDetails);

        verify(message).setFrom(EMAIL_FROM);
        verify(message).addRecipients(TO, "recipient@gmail.com");
        verify(message).setSubject("Test email");
        verify(message).setContent(getContent(), "text/html; charset=utf-8");
    }

    private String getContent() {
        return "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <title>Military Store Order Details</title>\n" +
            "    <style>\n" +
            "        body {\n" +
            "            font-family: Arial, sans-serif;\n" +
            "            line-height: 1.6;\n" +
            "        }\n" +
            "        .container {\n" +
            "            width: 80%;\n" +
            "            margin: auto;\n" +
            "            padding: 20px;\n" +
            "            border: 1px solid #ddd;\n" +
            "            border-radius: 8px;\n" +
            "            background-color: #f9f9f9;\n" +
            "        }\n" +
            "        .header {\n" +
            "            text-align: center;\n" +
            "            margin-bottom: 20px;\n" +
            "        }\n" +
            "        .footer {\n" +
            "            margin-top: 20px;\n" +
            "            text-align: center;\n" +
            "        }\n" +
            "        .footer a {\n" +
            "            color: #007BFF;\n" +
            "            text-decoration: none;\n" +
            "        }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "<div class=\"container\">\n" +
            "    <div class=\"header\">\n" +
            "        <h2>Military Store Order Details</h2>\n" +
            "    </div>\n" +
            "    <p>Dear Mr Smith,</p>\n" +
            "    <p>We are glad to notify you that your order is ready.</p>\n" +
            "    <p>Thank you for your order.</p>\n" +
            "    <p>If you have any questions, please email us at <a href=\"mailto:militarystorenotificator@gmail.com\">militarystorenotificator@gmail.com</a>.</p>\n" +
            "    <div class=\"footer\">\n" +
            "        <p>Best regards,<br>\n" +
            "            Administration of the Military Store</p>\n" +
            "    </div>\n" +
            "</div>\n" +
            "</body>\n" +
            "</html>\n";
    }
}