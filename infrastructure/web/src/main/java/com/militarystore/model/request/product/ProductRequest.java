package com.militarystore.model.request.product;

import com.militarystore.model.dto.product.ProductSizeGridTypeDto;
import com.militarystore.model.dto.product.ProductStockDetailsDto;
import com.militarystore.model.dto.product.ProductTagDto;

import java.util.List;

public record ProductRequest(
    String name,
    String description,
    Integer price,
    Integer subcategoryId,
    ProductSizeGridTypeDto sizeGridType,
    ProductTagDto tag,
    List<ProductStockDetailsDto> stockDetails
) {
}
