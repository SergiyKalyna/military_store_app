package com.militarystore.port.out.product;

public interface ProductRatePort {

    void saveOrUpdateRate(Integer userId, Integer productId, double productRate);

    double getAverageRateByProductId(Integer productId);

    void deleteRate(Integer productId);

    void deleteRatesByUserId(Integer userId);
}
