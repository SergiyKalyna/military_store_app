package com.militarystore.model.dto.product;

import lombok.Builder;

import java.util.List;

@Builder
public record ProductDto(
    Integer id,
    String name,
    String description,
    Integer price,
    Integer subcategoryId,
    ProductSizeGridTypeDto sizeGridType,
    ProductTagDto tag,
    List<ProductStockDetailsDto> stockDetails,
    boolean isInStock,
    double avgRate
) {
}
