package com.militarystore.entity.basket;

import lombok.Builder;

@Builder
public record ProductInBasket(
    Integer productId,
    String productName,
    Integer productPrice,
    Integer quantity
) {
}
