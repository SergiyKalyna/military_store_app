package com.militarystore.product;

import com.militarystore.entity.product.Product;
import com.militarystore.entity.product.ProductStockDetails;
import com.militarystore.exception.MsValidationException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductValidatorTest {

    @Test
    void validateProduct_whenProductNameIsNull_shouldThrowValidationException() {
        var product = Product.builder().build();

        assertThrows(MsValidationException.class, () -> ProductValidator.validateProduct(product));
    }

    @Test
    void validateProduct_whenProductNameIsEmpty_shouldThrowValidationException() {
        var product = Product.builder().name("").build();

        assertThrows(MsValidationException.class, () -> ProductValidator.validateProduct(product));
    }

    @Test
    void validateProduct_whenProductDescriptionIsNull_shouldThrowValidationException() {
        var product = Product.builder().name("name").build();

        assertThrows(MsValidationException.class, () -> ProductValidator.validateProduct(product));
    }

    @Test
    void validateProduct_whenProductDescriptionIsEmpty_shouldThrowValidationException() {
        var product = Product.builder().name("name").description("").build();

        assertThrows(MsValidationException.class, () -> ProductValidator.validateProduct(product));
    }

    @Test
    void validateProduct_whenProductPriceIsNull_shouldThrowValidationException() {
        var product = Product.builder()
            .name("name")
            .description("description")
            .build();

        assertThrows(MsValidationException.class, () -> ProductValidator.validateProduct(product));
    }

    @Test
    void validateProduct_whenProductPriceIsLessThanZero_shouldThrowValidationException() {
        var product = Product.builder()
            .name("name")
            .description("description")
            .price(-1)
            .build();

        assertThrows(MsValidationException.class, () -> ProductValidator.validateProduct(product));
    }

    @Test
    void validateProduct_whenStockDetailsIsEmpty_shouldThrowValidationException() {
        var product = Product.builder()
            .name("name")
            .description("description")
            .price(1)
            .stockDetails(List.of())
            .build();

        assertThrows(MsValidationException.class, () -> ProductValidator.validateProduct(product));
    }

    @Test
    void validateProduct_whenStockAvailabilityIsLessThanZero_shouldThrowValidationException() {
        var product = Product.builder()
            .name("name")
            .description("description")
            .price(1)
            .stockDetails(List.of(ProductStockDetails.builder().stockAvailability(-1).build()))
            .build();

        assertThrows(MsValidationException.class, () -> ProductValidator.validateProduct(product));
    }
}