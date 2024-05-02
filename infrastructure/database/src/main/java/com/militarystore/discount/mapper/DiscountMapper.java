package com.militarystore.discount.mapper;

import com.militarystore.entity.discount.Discount;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.stereotype.Component;

import static com.militarystore.jooq.Tables.DISCOUNTS;

@Component
public class DiscountMapper implements RecordMapper<Record, Discount> {

    @Override
    public Discount map(Record discountRecord) {
        return Discount.builder()
            .userId(discountRecord.get(DISCOUNTS.USER_ID))
            .discountCode(discountRecord.get(DISCOUNTS.DISCOUNT_CODE))
            .discount(discountRecord.get(DISCOUNTS.DISCOUNT))
            .usageLimit(discountRecord.get(DISCOUNTS.USAGE_LIMIT))
            .expirationDate(discountRecord.get(DISCOUNTS.EXPIRATION_DATE).toLocalDate())
            .build();
    }
}
