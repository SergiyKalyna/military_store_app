package com.militarystore.entity.product;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ProductFeedback(
    Integer id,
    Integer productId,
    Integer userId,
    String feedback,
    LocalDateTime dateTime
) {
}
