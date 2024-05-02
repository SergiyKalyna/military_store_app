package com.militarystore.entity.discount;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record Discount(
    Integer userId,
    String discountCode,
    double discount,
    int usageLimit,
    LocalDate expirationDate
) {
}
