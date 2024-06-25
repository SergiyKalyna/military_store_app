package com.militarystore.entity.email;

import lombok.Builder;

@Builder
public record EmailDetails(
    String recipientEmail,
    String subject,
    String username,
    String message
) {
}
