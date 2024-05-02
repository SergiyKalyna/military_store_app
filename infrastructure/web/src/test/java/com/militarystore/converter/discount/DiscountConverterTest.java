package com.militarystore.converter.discount;

import com.militarystore.entity.discount.Discount;
import com.militarystore.model.dto.DiscountDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class DiscountConverterTest {

    private DiscountConverter discountConverter;

    @BeforeEach
    void setUp() {
        discountConverter = new DiscountConverter();
    }

    @Test
    void toDto() {
        var expirationDate = LocalDateTime.of(2022, 1, 1, 0, 0);
        var discount = Discount.builder()
            .userId(1)
            .discountCode("code")
            .discount(0.03)
            .usageLimit(1)
            .expirationDate(expirationDate)
            .build();

        var discountDto = DiscountDto.builder()
            .userId(1)
            .discountCode("code")
            .discount(0.03)
            .usageLimit(1)
            .expirationDate(expirationDate)
            .build();

        assertThat(discountConverter.toDto(discount)).isEqualTo(discountDto);
    }
}