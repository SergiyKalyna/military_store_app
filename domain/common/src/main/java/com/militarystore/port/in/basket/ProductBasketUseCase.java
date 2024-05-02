package com.militarystore.port.in.basket;

import com.militarystore.entity.basket.ProductInBasket;

import java.util.List;

public interface ProductBasketUseCase {

    void addProductToBasket(Integer productStockDetailsId, Integer userId, Integer quantity);

    void updateProductQuantityInBasket(Integer productStockDetailsId, Integer userId, Integer quantity);

    void deleteProductFromBasket(Integer productStockDetailsId, Integer userId);

    void deleteUserProductsFromBasket(Integer userId);

    List<ProductInBasket> getUserBasketProducts(Integer userId);
}
