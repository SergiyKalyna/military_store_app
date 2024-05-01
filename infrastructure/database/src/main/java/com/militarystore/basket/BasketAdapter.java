package com.militarystore.basket;

import com.militarystore.basket.mapper.BasketMapper;
import com.militarystore.entity.basket.ProductInBasket;
import com.militarystore.port.out.basket.BasketPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BasketAdapter implements BasketPort {

    private final BasketRepository basketRepository;
    private final BasketMapper basketMapper;

    @Override
    public void addProductToBasket(Integer productStockDetailsId, Integer userId, Integer quantity) {
        basketRepository.addProductToBasket(productStockDetailsId, userId, quantity);
    }

    @Override
    public void updateProductQuantityInBasket(Integer productStockDetailsId, Integer userId, Integer quantity) {
        basketRepository.updateProductQuantityInBasket(productStockDetailsId, userId, quantity);
    }

    @Override
    public void deleteProductFromBasket(Integer productStockDetailsId, Integer userId) {
        basketRepository.deleteProductFromBasket(productStockDetailsId, userId);
    }

    @Override
    public void deleteUserProductsFromBasket(Integer userId) {
        basketRepository.deleteUserProductsFromBasket(userId);
    }

    @Override
    public void deleteProductFromAllBaskets(Integer productId) {
        basketRepository.deleteProductFromAllBaskets(productId);
    }

    @Override
    public List<ProductInBasket> getUserBasketProducts(Integer userId) {
        var productsInBasket = basketRepository.getUserBasketProducts(userId);

        return productsInBasket.stream()
            .map(basketMapper::map)
            .toList();
    }
}
