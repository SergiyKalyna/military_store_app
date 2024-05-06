package com.militarystore.model.dto.order;

import com.militarystore.model.dto.product.ProductSizeDto;
import lombok.Builder;

@Builder
public record OrderDetailsDto(
    Integer productId,
    Integer productStockDetailsId,
    String productName,
    Integer productQuantity,
    Integer productPrice,
    ProductSizeDto productSizeDto,
    Integer totalAmount
) {
}
