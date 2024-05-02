package com.militarystore.converter.basket;

import com.militarystore.converter.ProductBasketConverter;
import com.militarystore.entity.basket.ProductInBasket;
import com.militarystore.model.dto.basket.ProductInBasketDto;
import com.militarystore.model.response.ProductBasketResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProductBasketConverterTest {

    private ProductBasketConverter productBasketConverter;

    @BeforeEach
    void setUp() {
        productBasketConverter = new ProductBasketConverter();
    }

    @Test
    void convertToProductBasketResponse() {
        var productsInBasket = List.of(
            ProductInBasket.builder()
                .productId(1)
                .productName("product1")
                .productPrice(100)
                .quantity(2)
                .build(),
            ProductInBasket.builder()
                .productId(2)
                .productName("product2")
                .productPrice(200)
                .quantity(3)
                .build()
        );

        var expectedResponse = new ProductBasketResponse(
            List.of(
                ProductInBasketDto.builder()
                    .productId(1)
                    .productName("product1")
                    .productPrice(100)
                    .quantity(2)
                    .totalAmount(200)
                    .build(),
                ProductInBasketDto.builder()
                    .productId(2)
                    .productName("product2")
                    .productPrice(200)
                    .quantity(3)
                    .totalAmount(600)
                    .build()
            ),
            800
        );

        assertThat(productBasketConverter.convertToProductBasketResponse(productsInBasket)).isEqualTo(expectedResponse);
    }
}