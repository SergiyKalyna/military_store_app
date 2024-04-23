package com.militarystore.model.dto.product;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ProductFeedbackDto(
    Integer id,
    Integer userId,
    String userLogin,
    String feedback,
    LocalDateTime dateTime
) {
}
