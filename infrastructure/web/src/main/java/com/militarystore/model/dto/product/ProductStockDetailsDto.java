package com.militarystore.model.dto.product;

import lombok.Builder;

@Builder
public record ProductStockDetailsDto(
    Integer id,
    Integer productId,
    Integer stockAvailability,
    ProductSizeDto productSize
) {
}
