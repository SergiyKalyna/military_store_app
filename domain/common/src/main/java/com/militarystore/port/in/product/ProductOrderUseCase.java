package com.militarystore.port.in.product;

public interface ProductOrderUseCase {
    void updateProductStockAvailability(Integer productId, Integer productStockDetailsId, Integer orderedProductQuantity);

    boolean isEnoughProductStockAvailability(Integer productStockDetailsId, Integer orderedProductQuantity);
}
