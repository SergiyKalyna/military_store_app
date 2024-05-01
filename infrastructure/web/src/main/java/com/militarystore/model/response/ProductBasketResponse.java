package com.militarystore.model.response;

import com.militarystore.model.dto.basket.ProductInBasketDto;

import java.util.List;

public record ProductBasketResponse(
    List<ProductInBasketDto> productsInBasket,
    Integer totalBasketAmount
) {
}
