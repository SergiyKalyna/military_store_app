package com.militarystore.entity.discount;

import java.time.LocalDate;

public record Discount(
    Integer userId,
    String discountCode,
    double discount,
    int usageLimit,
    LocalDate expirationDate
) {
}
