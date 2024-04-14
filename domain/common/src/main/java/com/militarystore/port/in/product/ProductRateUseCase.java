package com.militarystore.port.in.product;

public interface ProductRateUseCase {

    void rateProduct(Integer productId, Integer userId, double productRate);

    double getAverageRateByProductId(Integer productId);
}
