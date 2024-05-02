package com.militarystore.converter.discount;

import com.militarystore.entity.discount.Discount;
import com.militarystore.model.dto.DiscountDto;
import org.springframework.stereotype.Component;

@Component
public class DiscountConverter {

    public DiscountDto toDto(Discount discount) {
        return DiscountDto.builder()
            .userId(discount.userId())
            .discountCode(discount.discountCode())
            .discount(discount.discount())
            .usageLimit(discount.usageLimit())
            .expirationDate(discount.expirationDate())
            .build();
    }
}
