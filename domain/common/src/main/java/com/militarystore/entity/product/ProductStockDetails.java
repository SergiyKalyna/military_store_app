package com.militarystore.entity.product;

import com.militarystore.entity.product.model.ProductSize;
import lombok.Builder;

@Builder
public record ProductStockDetails(
    Integer id,
    Integer productId,
    Integer stockAvailability,
    ProductSize productSize
) {
}
