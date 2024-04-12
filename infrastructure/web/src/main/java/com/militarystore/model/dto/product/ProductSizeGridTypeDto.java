package com.militarystore.model.dto.product;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ProductSizeGridTypeDto {
    SHOES("Shoes"),
    CLOTHES("Clothes"),
    HATS("Hats"),
    GLOVES("Gloves"),
    HELMETS("Helmets"),
    COMMON("Common");

    private final String label;
}
