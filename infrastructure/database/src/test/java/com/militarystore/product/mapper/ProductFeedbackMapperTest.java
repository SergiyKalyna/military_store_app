package com.militarystore.product.mapper;

import com.militarystore.entity.product.ProductFeedback;
import com.militarystore.jooq.tables.records.ProductFeedbacksRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ProductFeedbackMapperTest {

    private ProductFeedbackMapper productFeedbackMapper;

    @BeforeEach
    void setUp() {
        productFeedbackMapper = new ProductFeedbackMapper();
    }

    @Test
    void map() {
        var feedbackDateTime = LocalDateTime.now();
        var feedbackRecord = new ProductFeedbacksRecord();
        feedbackRecord.setId(1);
        feedbackRecord.setProductId(1);
        feedbackRecord.setUserId(1);
        feedbackRecord.setFeedback("Good product");
        feedbackRecord.setDateTime(feedbackDateTime);

        var expectedResult = ProductFeedback.builder()
            .id(1)
            .productId(1)
            .userId(1)
            .feedback("Good product")
            .dateTime(feedbackDateTime)
            .build();

        assertThat(productFeedbackMapper.map(feedbackRecord)).isEqualTo(expectedResult);
    }
}