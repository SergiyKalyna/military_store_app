package com.militarystore.port.in.product;

public interface UpdateProductAvailabilityUseCase {
    void updateProductStockAvailability(Integer productId, Integer productStockDetailsId, Integer orderedProductQuantity);
}
