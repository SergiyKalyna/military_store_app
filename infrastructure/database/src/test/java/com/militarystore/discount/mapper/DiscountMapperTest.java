package com.militarystore.discount.mapper;

import com.militarystore.entity.discount.Discount;
import com.militarystore.jooq.tables.records.DiscountsRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class DiscountMapperTest {

    private DiscountMapper discountMapper;

    @BeforeEach
    void setUp() {
        discountMapper = new DiscountMapper();
    }

    @Test
    void map() {
        var expirationDate = LocalDateTime.of(2022, 1, 1, 0, 0);
        var discountRecord = new DiscountsRecord();
        discountRecord.setUserId(1);
        discountRecord.setDiscountCode("code");
        discountRecord.setDiscount(0.03);
        discountRecord.setUsageLimit(1);
        discountRecord.setExpirationDate(expirationDate);

        var expectedResult = Discount.builder()
            .userId(1)
            .discountCode("code")
            .discount(0.03)
            .usageLimit(1)
            .expirationDate(expirationDate)
            .build();

        assertThat(discountMapper.map(discountRecord)).isEqualTo(expectedResult);
    }
}