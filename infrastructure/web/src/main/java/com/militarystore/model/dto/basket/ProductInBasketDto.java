package com.militarystore.model.dto.basket;

import lombok.Builder;

@Builder
public record ProductInBasketDto(
    Integer productId,
    Integer productStockDetailsId,
    String productName,
    Integer productPrice,
    Integer quantity,
    Integer totalAmount
) {
}
