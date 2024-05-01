package com.militarystore.basket;

import com.militarystore.entity.basket.ProductInBasket;
import com.militarystore.exception.MsValidationException;
import com.militarystore.port.out.basket.BasketPort;
import com.militarystore.port.out.product.ProductStockDetailsPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductBasketServiceTest {

    private static final Integer PRODUCT_STOCK_DETAILS_ID = 1;
    private static final Integer USER_ID = 11;

    @Mock
    private BasketPort basketPort;

    @Mock
    private ProductStockDetailsPort productStockDetailsPort;

    private ProductBasketService productBasketService;

    @BeforeEach
    void setUp() {
        productBasketService = new ProductBasketService(basketPort, productStockDetailsPort);
    }

    @Test
    void addProductToBasket_whenQuantityIsNotValid_shouldThrowException() {
        assertThrows(
            MsValidationException.class,
            () -> productBasketService.addProductToBasket(PRODUCT_STOCK_DETAILS_ID, USER_ID, 0)
        );
    }

    @Test
    void addProductToBasket_whenProductStockIsNotAvailable_shouldThrowException() {
        when(productStockDetailsPort.isEnoughProductStockAvailability(PRODUCT_STOCK_DETAILS_ID, 2))
            .thenReturn(false);

        assertThrows(
            MsValidationException.class,
            () -> productBasketService.addProductToBasket(PRODUCT_STOCK_DETAILS_ID, USER_ID, 2)
        );
    }

    @Test
    void addProductToBasket_shouldAddProductToBasket() {
        when(productStockDetailsPort.isEnoughProductStockAvailability(PRODUCT_STOCK_DETAILS_ID, 1))
            .thenReturn(true);

        productBasketService.addProductToBasket(PRODUCT_STOCK_DETAILS_ID, USER_ID, 1);

         verify(basketPort).addProductToBasket(PRODUCT_STOCK_DETAILS_ID, USER_ID, 1);
    }

    @Test
    void updateProductQuantityInBasket_whenQuantityIsNotValid_shouldThrowException() {
        assertThrows(
            MsValidationException.class,
            () -> productBasketService.updateProductQuantityInBasket(PRODUCT_STOCK_DETAILS_ID, USER_ID, -1)
        );
    }

    @Test
    void updateProductQuantityInBasket_whenProductStockIsNotAvailable_shouldThrowException() {
        when(productStockDetailsPort.isEnoughProductStockAvailability(PRODUCT_STOCK_DETAILS_ID, 2))
            .thenReturn(false);

        assertThrows(
            MsValidationException.class,
            () -> productBasketService.updateProductQuantityInBasket(PRODUCT_STOCK_DETAILS_ID, USER_ID, 2)
        );
    }

    @Test
    void updateProductQuantityInBasket_shouldUpdateProductQuantityInBasket() {
        when(productStockDetailsPort.isEnoughProductStockAvailability(PRODUCT_STOCK_DETAILS_ID, 1))
            .thenReturn(true);

        productBasketService.updateProductQuantityInBasket(PRODUCT_STOCK_DETAILS_ID, USER_ID, 1);

        verify(basketPort).updateProductQuantityInBasket(PRODUCT_STOCK_DETAILS_ID, USER_ID, 1);
    }

    @Test
    void deleteProductFromBasket() {
        productBasketService.deleteProductFromBasket(PRODUCT_STOCK_DETAILS_ID, USER_ID);

        verify(basketPort).deleteProductFromBasket(PRODUCT_STOCK_DETAILS_ID, USER_ID);
    }

    @Test
    void deleteUserProductsFromBasket() {
        productBasketService.deleteUserProductsFromBasket(USER_ID);

        verify(basketPort).deleteUserProductsFromBasket(USER_ID);
    }

    @Test
    void deleteProductFromAllBaskets() {
        productBasketService.deleteProductFromAllBaskets(PRODUCT_STOCK_DETAILS_ID);

        verify(basketPort).deleteProductFromAllBaskets(PRODUCT_STOCK_DETAILS_ID);
    }

    @Test
    void getUserBasketProducts() {
        var productsInBasket = List.of(ProductInBasket.builder().build());

        when(basketPort.getUserBasketProducts(USER_ID)).thenReturn(productsInBasket);

        assertThat(productBasketService.getUserBasketProducts(USER_ID)).isEqualTo(productsInBasket);
    }
}