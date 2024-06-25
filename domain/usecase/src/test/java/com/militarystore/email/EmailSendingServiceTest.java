package com.militarystore.email;

import com.militarystore.entity.email.EmailDetails;
import com.militarystore.entity.user.User;
import com.militarystore.port.out.email.SendEmailPort;
import com.militarystore.port.out.user.UserPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailSendingServiceTest {

    @Mock
    private UserPort userPort;

    @Mock
    private SendEmailPort sendEmailPort;

    private EmailSendingService emailSendingService;

    @BeforeEach
    void setUp() {
        emailSendingService = new EmailSendingService(userPort, sendEmailPort);
    }

    @Test
    void sendEmail() {
        var orderId = 1;
        var message = "your order is ready";
        var user = User.builder()
            .email("test@gmail.com")
            .firstName("John")
            .secondName("Doe")
            .build();

        var emailDetails = EmailDetails.builder()
            .message(message)
            .recipientEmail(user.email())
            .subject("Military Store Order Details")
            .username("John Doe")
            .build();

        when(userPort.getUserByOrderId(orderId)).thenReturn(user);

        emailSendingService.sendEmail(orderId, message);

        verify(sendEmailPort).sendEmail(emailDetails);
    }
}