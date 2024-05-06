package com.militarystore.model.dto.order;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record OrderDto(
    Integer id,
    LocalDate orderDate,
    OrderStatusDto orderStatusDto,
    String shippingNumber,
    Integer totalAmount
) {
}
