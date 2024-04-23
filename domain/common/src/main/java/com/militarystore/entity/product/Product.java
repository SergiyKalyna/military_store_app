package com.militarystore.entity.product;

import com.militarystore.entity.product.model.ProductSizeGridType;
import com.militarystore.entity.product.model.ProductTag;
import lombok.Builder;

import java.util.List;

@Builder
public record Product(
    Integer id,
    String name,
    String description,
    Integer price,
    Integer subcategoryId,
    ProductSizeGridType sizeGridType,
    ProductTag tag,
    List<ProductStockDetails> stockDetails,
    Double avgRate,
    boolean isInStock,
    List<ProductFeedback> feedbacks
) {
}
