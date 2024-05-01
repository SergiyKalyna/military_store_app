package com.militarystore.basket;

import com.militarystore.basket.mapper.BasketMapper;
import com.militarystore.entity.basket.ProductInBasket;
import org.jooq.Record4;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BasketAdapterTest {

    private static final Integer PRODUCT_STOCK_DETAILS_ID = 1;
    private static final Integer USER_ID = 1;
    private static final Integer QUANTITY = 1;

    @Mock
    private BasketRepository basketRepository;

    @Mock
    private BasketMapper basketMapper;

    private BasketAdapter basketAdapter;

    @BeforeEach
    void setUp() {
        basketAdapter = new BasketAdapter(basketRepository, basketMapper);
    }


    @Test
    void addProductToBasket() {
        basketAdapter.addProductToBasket(PRODUCT_STOCK_DETAILS_ID, USER_ID, QUANTITY);

        verify(basketRepository).addProductToBasket(PRODUCT_STOCK_DETAILS_ID, USER_ID, QUANTITY);
    }

    @Test
    void updateProductQuantityInBasket() {
        basketAdapter.updateProductQuantityInBasket(PRODUCT_STOCK_DETAILS_ID, USER_ID, QUANTITY);

        verify(basketRepository).updateProductQuantityInBasket(PRODUCT_STOCK_DETAILS_ID, USER_ID, QUANTITY);
    }

    @Test
    void deleteProductFromBasket() {
        basketAdapter.deleteProductFromBasket(PRODUCT_STOCK_DETAILS_ID, USER_ID);

        verify(basketRepository).deleteProductFromBasket(PRODUCT_STOCK_DETAILS_ID, USER_ID);
    }

    @Test
    void deleteUserProductsFromBasket() {
        basketAdapter.deleteUserProductsFromBasket(USER_ID);

        verify(basketRepository).deleteUserProductsFromBasket(USER_ID);
    }

    @Test
    void deleteProductFromAllBaskets() {
        basketAdapter.deleteProductFromAllBaskets(1);

        verify(basketRepository).deleteProductFromAllBaskets(1);
    }

    @Test
    void getUserBasketProducts() {
        var productsInBasket = ProductInBasket.builder().build();
        var mockRecord = mock(Record4.class);

        when(basketRepository.getUserBasketProducts(USER_ID)).thenReturn(List.of(mockRecord));
        when(basketMapper.map(mockRecord)).thenReturn(productsInBasket);

        assertThat(basketAdapter.getUserBasketProducts(USER_ID)).containsExactly(productsInBasket);
    }
}