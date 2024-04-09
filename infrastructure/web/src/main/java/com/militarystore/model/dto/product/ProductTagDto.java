package com.militarystore.model.dto.product;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ProductTagDto {
    NEW("New"),
    SALE("Sale"),
    BESTSELLER("Bestseller"),
    NONE("");

    private final String label;
}
