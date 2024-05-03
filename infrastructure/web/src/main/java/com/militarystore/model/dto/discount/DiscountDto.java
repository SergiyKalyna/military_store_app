package com.militarystore.model.dto.discount;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record DiscountDto(
    Integer userId,
    String discountCode,
    double discount,
    int usageLimit,
    LocalDateTime expirationDate
) {
}
