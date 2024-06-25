package com.militarystore.port.out.email;

import com.militarystore.entity.email.EmailDetails;

public interface SendEmailPort {

    void sendEmail(EmailDetails emailDetails);
}
