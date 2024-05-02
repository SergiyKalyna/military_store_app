package com.militarystore.entity.discount;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record Discount(
    Integer userId,
    String discountCode,
    double discount,
    int usageLimit,
    LocalDateTime expirationDate
) {
}
