package com.militarystore.port.out.basket;

import com.militarystore.entity.basket.ProductInBasket;

import java.util.List;

public interface BasketPort {

    void addProductToBasket(Integer productStockDetailsId, Integer userId, Integer quantity);

    void updateProductQuantityInBasket(Integer productStockDetailsId, Integer userId, Integer quantity);

    void deleteProductFromBasket(Integer productStockDetailsId, Integer userId);

    void deleteUserProductsFromBasket(Integer userId);

    void deleteProductFromAllBaskets(Integer productStockDetailsId);

    List<ProductInBasket> getUserBasketProducts(Integer userId);
}
