package com.militarystore.basket;

import com.militarystore.entity.basket.ProductInBasket;
import com.militarystore.exception.MsValidationException;
import com.militarystore.port.in.ProductBasketUseCase;
import com.militarystore.port.out.basket.BasketPort;
import com.militarystore.port.out.product.ProductStockDetailsPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductBasketService implements ProductBasketUseCase {

    private final BasketPort basketPort;
    private final ProductStockDetailsPort productStockDetailsPort;

    @Override
    public void addProductToBasket(Integer productStockDetailsId, Integer userId, Integer quantity) {
        validateQuantity(quantity);
        validateProductStockAvailability(productStockDetailsId, quantity);

        basketPort.addProductToBasket(productStockDetailsId, userId, quantity);
        log.info("Product with id {} was added to basket", productStockDetailsId);
    }

    @Override
    public void updateProductQuantityInBasket(Integer productStockDetailsId, Integer userId, Integer quantity) {
        validateQuantity(quantity);
        validateProductStockAvailability(productStockDetailsId, quantity);

        basketPort.updateProductQuantityInBasket(productStockDetailsId, userId, quantity);
        log.info("Product quantity with id {} was updated in basket", productStockDetailsId);
    }

    @Override
    public void deleteProductFromBasket(Integer productStockDetailsId, Integer userId) {
        basketPort.deleteProductFromBasket(productStockDetailsId, userId);
        log.info("Product with id {} was deleted from basket", productStockDetailsId);
    }

    @Override
    public void deleteUserProductsFromBasket(Integer userId) {
        basketPort.deleteUserProductsFromBasket(userId);
        log.info("All products were deleted from basket for user with id {}", userId);
    }

    @Override
    public void deleteProductFromAllBaskets(Integer productStockDetailsId) {
        basketPort.deleteProductFromAllBaskets(productStockDetailsId);
        log.info("Product with id {} was deleted from all baskets", productStockDetailsId);
    }

    @Override
    public List<ProductInBasket> getUserBasketProducts(Integer userId) {
        return basketPort.getUserBasketProducts(userId);
    }

    private void validateQuantity(Integer quantity) {
        if (quantity <= 0) {
            throw new MsValidationException("Quantity must be greater than 0");
        }
    }

    private void validateProductStockAvailability(Integer productStockDetailsId, Integer quantity) {
        if (!productStockDetailsPort.isEnoughProductStockAvailability(productStockDetailsId, quantity)) {
            throw new MsValidationException("Product with id " + productStockDetailsId + " is not available in such quantity");
        }
    }
}
