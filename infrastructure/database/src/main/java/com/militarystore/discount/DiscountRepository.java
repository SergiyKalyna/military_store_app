package com.militarystore.discount;

import com.militarystore.entity.discount.Discount;
import com.militarystore.jooq.tables.records.DiscountsRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.militarystore.jooq.Tables.DISCOUNTS;

@Repository
@RequiredArgsConstructor
public class DiscountRepository {

    private static final Integer USAGE_LIMIT = 1;

    private final DSLContext dslContext;

    public String createUserDiscountCode(Discount discount) {
        return dslContext.insertInto(DISCOUNTS)
            .set(DISCOUNTS.USER_ID, discount.userId())
            .set(DISCOUNTS.DISCOUNT_CODE, discount.discountCode())
            .set(DISCOUNTS.DISCOUNT, discount.discount())
            .set(DISCOUNTS.USAGE_LIMIT, discount.usageLimit())
            .set(DISCOUNTS.EXPIRATION_DATE, discount.expirationDate())
            .returning(DISCOUNTS.DISCOUNT_CODE)
            .fetchOne(DISCOUNTS.DISCOUNT_CODE);
    }

    public void updateDiscountUsageLimit(String discountCode, Integer userId) {
        dslContext.update(DISCOUNTS)
            .set(DISCOUNTS.USAGE_LIMIT, DISCOUNTS.USAGE_LIMIT.minus(USAGE_LIMIT))
            .where(DISCOUNTS.DISCOUNT_CODE.eq(discountCode)
                .and(DISCOUNTS.USER_ID.eq(userId))
                .and(DISCOUNTS.USAGE_LIMIT.greaterOrEqual(USAGE_LIMIT)))
            .execute();
    }

    public void deleteUserDiscounts(Integer userId) {
        dslContext.deleteFrom(DISCOUNTS)
            .where(DISCOUNTS.USER_ID.eq(userId))
            .execute();
    }

    public List<DiscountsRecord> getUserDiscounts(Integer userId) {
        return dslContext.selectFrom(DISCOUNTS)
            .where(DISCOUNTS.USER_ID.eq(userId)
                .and(DISCOUNTS.USAGE_LIMIT.greaterOrEqual(USAGE_LIMIT))
                .and(DISCOUNTS.EXPIRATION_DATE.greaterOrEqual(LocalDateTime.now())))
            .fetch();
    }

    public boolean isAvailableUserDiscount(String discountCode, Integer userId) {
        return dslContext.fetchExists(
            dslContext.selectOne()
                .from(DISCOUNTS)
                .where(DISCOUNTS.DISCOUNT_CODE.eq(discountCode)
                    .and(DISCOUNTS.USER_ID.eq(userId))
                    .and(DISCOUNTS.USAGE_LIMIT.greaterOrEqual(USAGE_LIMIT))
                    .and(DISCOUNTS.EXPIRATION_DATE.greaterOrEqual(LocalDateTime.now())))
        );
    }

    public Double getDiscountByCode(String discountCode, Integer userId) {
        return dslContext.select(DISCOUNTS.DISCOUNT)
            .from(DISCOUNTS)
            .where(DISCOUNTS.DISCOUNT_CODE.eq(discountCode).and(DISCOUNTS.USER_ID.eq(userId)))
            .fetchOne(DISCOUNTS.DISCOUNT);
    }
}
