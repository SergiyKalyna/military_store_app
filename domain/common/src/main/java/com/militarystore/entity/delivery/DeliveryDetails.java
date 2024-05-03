package com.militarystore.entity.delivery;

import lombok.Builder;

@Builder
public record DeliveryDetails(
        Integer id,
        String city,
        Integer postNumber,
        String recipientName,
        String recipientPhone
) {
}
