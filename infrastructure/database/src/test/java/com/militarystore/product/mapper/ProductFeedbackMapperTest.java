package com.militarystore.product.mapper;

import com.militarystore.entity.product.ProductFeedback;
import org.jooq.Record;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.militarystore.jooq.Tables.PRODUCT_FEEDBACKS;
import static com.militarystore.jooq.Tables.USERS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProductFeedbackMapperTest {

    private ProductFeedbackMapper productFeedbackMapper;

    @BeforeEach
    void setUp() {
        productFeedbackMapper = new ProductFeedbackMapper();
    }

    @Test
    void map() {
        var feedbackDateTime = LocalDateTime.now();
        var mockRecord = mock(Record.class);
        when(mockRecord.get(PRODUCT_FEEDBACKS.ID)).thenReturn(1);
           when(mockRecord.get(PRODUCT_FEEDBACKS.USER_ID)).thenReturn(1);
            when(mockRecord.get(PRODUCT_FEEDBACKS.FEEDBACK)).thenReturn("Good product");
            when(mockRecord.get(PRODUCT_FEEDBACKS.DATE_TIME)).thenReturn(feedbackDateTime);
            when(mockRecord.get(USERS.LOGIN)).thenReturn("login");


        var expectedResult = ProductFeedback.builder()
            .id(1)
            .userId(1)
            .feedback("Good product")
            .dateTime(feedbackDateTime)
            .userLogin("login")
            .build();

        assertThat(productFeedbackMapper.map(mockRecord)).isEqualTo(expectedResult);
    }
}