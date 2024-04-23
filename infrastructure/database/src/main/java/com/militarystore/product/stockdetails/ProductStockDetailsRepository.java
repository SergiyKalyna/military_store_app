package com.militarystore.product.stockdetails;

import com.militarystore.jooq.tables.records.ProductStockDetailsRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.militarystore.jooq.Tables.PRODUCT_STOCK_DETAILS;

@Repository
@RequiredArgsConstructor
public class ProductStockDetailsRepository {

    private static final int OUT_OF_STOCK_QUANTITY = 0;

    private final DSLContext dslContext;

    public void addProductStockDetails(List<ProductStockDetailsRecord> productStockDetailsRecords) {
        dslContext.batchInsert(productStockDetailsRecords).execute();
    }

    public void updateProductStockDetails(List<ProductStockDetailsRecord> productStockDetailsRecords) {
        dslContext.batchUpdate(productStockDetailsRecords).execute();
    }

    public void updateProductStockAvailability(Integer productStockDetailsId, Integer orderedProductQuantity) {
        dslContext.update(PRODUCT_STOCK_DETAILS)
            .set(PRODUCT_STOCK_DETAILS.STOCK_AVAILABILITY, PRODUCT_STOCK_DETAILS.STOCK_AVAILABILITY.minus(orderedProductQuantity))
            .where(PRODUCT_STOCK_DETAILS.ID.eq(productStockDetailsId))
            .execute();
    }

    public boolean isProductAvailable(Integer productId) {
        return dslContext.fetchExists(dslContext.selectFrom(PRODUCT_STOCK_DETAILS)
            .where(PRODUCT_STOCK_DETAILS.PRODUCT_ID.eq(productId)
                .and(PRODUCT_STOCK_DETAILS.STOCK_AVAILABILITY.greaterThan(OUT_OF_STOCK_QUANTITY))));
    }

    public void deleteProductStockDetails(Integer productId) {
        dslContext.deleteFrom(PRODUCT_STOCK_DETAILS)
            .where(PRODUCT_STOCK_DETAILS.PRODUCT_ID.eq(productId))
            .execute();
    }

    public boolean isEnoughProductStockAvailability(Integer productStockDetailsId, Integer orderedProductQuantity) {
        return dslContext.fetchExists(dslContext.selectFrom(PRODUCT_STOCK_DETAILS)
            .where(PRODUCT_STOCK_DETAILS.ID.eq(productStockDetailsId)
                .and(PRODUCT_STOCK_DETAILS.STOCK_AVAILABILITY.greaterOrEqual(orderedProductQuantity))));
    }

    public List<ProductStockDetailsRecord> getProductStockDetailsByProductId(Integer productId) {
        return dslContext.selectFrom(PRODUCT_STOCK_DETAILS)
            .where(PRODUCT_STOCK_DETAILS.PRODUCT_ID.eq(productId))
            .fetch();
    }
}
