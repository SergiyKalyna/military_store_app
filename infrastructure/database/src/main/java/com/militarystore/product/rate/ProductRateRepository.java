package com.militarystore.product.rate;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import static com.militarystore.jooq.Tables.PRODUCT_RATES;

@Repository
@RequiredArgsConstructor
public class ProductRateRepository {

    private final DSLContext dslContext;

    public void saveOrUpdateRate(Integer userId, Integer productId, double productRate) {
        dslContext.insertInto(PRODUCT_RATES)
            .set(PRODUCT_RATES.USER_ID, userId)
            .set(PRODUCT_RATES.PRODUCT_ID, productId)
            .set(PRODUCT_RATES.RATE, productRate)
            .onConflict(PRODUCT_RATES.USER_ID, PRODUCT_RATES.PRODUCT_ID)
            .doUpdate()
            .set(PRODUCT_RATES.RATE, productRate)
            .execute();
    }

    public double getAverageRateByProductId(Integer productId) {
        return dslContext.select(DSL.avg(PRODUCT_RATES.RATE))
            .from(PRODUCT_RATES)
            .where(PRODUCT_RATES.PRODUCT_ID.eq(productId))
            .fetchOptionalInto(Double.class)
            .orElse(0.0);
    }

    public void deleteRate(Integer productId) {
        dslContext.deleteFrom(PRODUCT_RATES)
            .where(PRODUCT_RATES.PRODUCT_ID.eq(productId))
            .execute();
    }

    public void deleteRatesByUserId(Integer userId) {
        dslContext.deleteFrom(PRODUCT_RATES)
            .where(PRODUCT_RATES.USER_ID.eq(userId))
            .execute();
    }
}
