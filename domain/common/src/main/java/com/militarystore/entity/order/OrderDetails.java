package com.militarystore.entity.order;

import com.militarystore.entity.product.model.ProductSize;
import lombok.Builder;

@Builder
public record OrderDetails(
    Integer orderId,
    Integer productStockDetailsId,
    String productName,
    ProductSize productSize,
    Integer quantity,
    Integer productPrice
) {
}
