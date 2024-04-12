package com.militarystore.port.out.product;

import com.militarystore.entity.product.ProductStockDetails;

import java.util.List;

public interface ProductStockDetailsPort {

    void addProductStockDetails(Integer productId, List<ProductStockDetails> productStockDetails);

    void updateProductStockDetails(List<ProductStockDetails> productStockDetails);

    void updateProductStockAvailability(Integer productStockDetailsId, Integer orderedProductQuantity);

    boolean isProductAvailable(Integer productId);

    void deleteProductStockDetails(Integer productId);

    boolean isEnoughProductStockAvailability(Integer productStockDetailsId, Integer orderedProductQuantity);
}
