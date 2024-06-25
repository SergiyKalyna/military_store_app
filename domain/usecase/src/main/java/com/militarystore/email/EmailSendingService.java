package com.militarystore.email;

import com.militarystore.entity.email.EmailDetails;
import com.militarystore.port.out.email.SendEmailPort;
import com.militarystore.port.out.user.UserPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSendingService {

    private static final String EMAIL_SUBJECT = "Military Store Order Details";

    private final UserPort userPort;
    private final SendEmailPort sendEmailPort;

    public void sendEmail(Integer orderId, String message) {
        var user = userPort.getUserByOrderId(orderId);
        var emailDetails = EmailDetails.builder()
            .message(message)
            .recipientEmail(user.email())
            .subject(EMAIL_SUBJECT)
            .username(String.format("%s %s", user.firstName(), user.secondName()))
            .build();

        sendEmailPort.sendEmail(emailDetails);
    }
}
