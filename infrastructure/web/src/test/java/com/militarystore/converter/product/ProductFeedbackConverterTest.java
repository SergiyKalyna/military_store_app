package com.militarystore.converter.product;

import com.militarystore.entity.product.ProductFeedback;
import com.militarystore.model.dto.product.ProductFeedbackDto;
import com.militarystore.model.request.product.ProductFeedbackRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ProductFeedbackConverterTest {

    private static final Integer USER_ID = 1;
    private static final Integer FEEDBACK_ID = 10;
    private static final Integer PRODUCT_ID = 11;
    private static final String FEEDBACK = "Good product";

    private ProductFeedbackConverter productFeedbackConverter;

    @BeforeEach
    void setUp() {
        productFeedbackConverter = new ProductFeedbackConverter();
    }

    @Test
    void convertToProductFeedback() {
        var productFeedbackRequest = new ProductFeedbackRequest(PRODUCT_ID, FEEDBACK);
        var expectedResult = ProductFeedback.builder()
            .userId(USER_ID)
            .productId(PRODUCT_ID)
            .feedback(FEEDBACK)
            .dateTime(LocalDateTime.now())
            .build();

        var result = productFeedbackConverter.convertToProductFeedback(productFeedbackRequest, USER_ID);

        assertEquals(expectedResult, result);
    }

    @Test
    void testConvertToProductFeedback() {
        var productFeedbackRequest = new ProductFeedbackRequest(PRODUCT_ID, FEEDBACK);
        var expectedResult = ProductFeedback.builder()
            .id(FEEDBACK_ID)
            .userId(USER_ID)
            .productId(PRODUCT_ID)
            .feedback(FEEDBACK)
            .dateTime(LocalDateTime.now())
            .build();

        var result = productFeedbackConverter.convertToProductFeedback(productFeedbackRequest, FEEDBACK_ID, USER_ID);

        assertEquals(expectedResult, result);
    }

    @Test
    void convertToProductFeedbackDto() {
        var productFeedback = ProductFeedback.builder()
            .id(FEEDBACK_ID)
            .userId(USER_ID)
            .productId(PRODUCT_ID)
            .userLogin("login")
            .feedback(FEEDBACK)
            .dateTime(LocalDateTime.of(2021, 1, 1, 0, 0, 0))
            .build();

        var expectedResult = ProductFeedbackDto.builder()
            .id(FEEDBACK_ID)
            .userId(USER_ID)
            .userLogin("login")
            .feedback(FEEDBACK)
            .dateTime(LocalDateTime.of(2021, 1, 1, 0, 0, 0))
            .build();

        assertThat(productFeedbackConverter.convertToProductFeedbackDto(productFeedback)).isEqualTo(expectedResult);
    }

    private void assertEquals(ProductFeedback expected, ProductFeedback actual) {
        assertThat(actual.id()).isEqualTo(expected.id());
        assertThat(actual.userId()).isEqualTo(expected.userId());
        assertThat(actual.productId()).isEqualTo(expected.productId());
        assertThat(actual.feedback()).isEqualTo(expected.feedback());
        assertThat(actual.dateTime().toLocalDate()).isEqualTo(expected.dateTime().toLocalDate());
        assertThat(actual.userLogin()).isEqualTo(expected.userLogin());
    }
}