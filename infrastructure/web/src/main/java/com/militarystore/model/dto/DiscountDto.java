package com.militarystore.model.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record DiscountDto(
    Integer userId,
    String discountCode,
    double discount,
    int usageLimit,
    LocalDate expirationDate
) {
}
