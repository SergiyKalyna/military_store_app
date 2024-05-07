package com.militarystore.basket.mapper;

import com.militarystore.entity.basket.ProductInBasket;
import org.jooq.Record4;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.militarystore.jooq.Tables.BASKETS;
import static com.militarystore.jooq.Tables.PRODUCTS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BasketMapperTest {

    private BasketMapper basketMapper;

    @BeforeEach
    void setUp() {
        basketMapper = new BasketMapper();
    }

    @Test
    void map() {
        var record = mock(Record4.class);
        when(record.get(PRODUCTS.ID)).thenReturn(1);
        when(record.get(BASKETS.PRODUCT_STOCK_DETAILS_ID)).thenReturn(2);
        when(record.get(PRODUCTS.NAME)).thenReturn("product");
        when(record.get(PRODUCTS.PRICE)).thenReturn(100);
        when(record.get(BASKETS.QUANTITY)).thenReturn(11);

        var expectedProductInBasket = ProductInBasket.builder()
            .productId(1)
            .productStockDetailsId(2)
            .productName("product")
            .productPrice(100)
            .quantity(11)
            .build();

        assertThat(basketMapper.map(record)).isEqualTo(expectedProductInBasket);
    }
}