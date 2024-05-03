package com.militarystore.model.dto.delivery;

import lombok.Builder;

@Builder
public record DeliveryDetailsDto(
    Integer id,
    String city,
    Integer postNumber,
    String recipientName,
    String recipientPhone
) {
}
