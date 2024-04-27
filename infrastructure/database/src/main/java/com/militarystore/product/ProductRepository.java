package com.militarystore.product;

import com.militarystore.entity.product.Product;
import com.militarystore.jooq.tables.records.ProductsRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record6;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

import static com.militarystore.jooq.Tables.PRODUCTS;
import static com.militarystore.jooq.Tables.PRODUCT_RATES;

@Repository
@RequiredArgsConstructor
public class ProductRepository {

    public static final String AVG_RATE = "avg_rate";

    private final DSLContext dslContext;

    public Integer addProduct(Product product) {
        return dslContext.insertInto(PRODUCTS)
            .set(PRODUCTS.NAME, product.name())
            .set(PRODUCTS.DESCRIPTION, product.description())
            .set(PRODUCTS.PRICE, product.price())
            .set(PRODUCTS.SUBCATEGORY_ID, product.subcategoryId())
            .set(PRODUCTS.SIZE_GRID_TYPE, product.sizeGridType().name())
            .set(PRODUCTS.PRODUCT_TAG, product.tag().name())
            .returning(PRODUCTS.ID)
            .fetchOne(PRODUCTS.ID);
    }

    public ProductsRecord getProductById(Integer productId) {
        return dslContext.selectFrom(PRODUCTS)
            .where(PRODUCTS.ID.eq(productId))
            .fetchOne();
    }

    public List<Record6<Integer, String, Integer, String, Boolean, BigDecimal>> getProductsBySubcategoryId(Integer subcategoryId) {
        return dslContext.select(
                PRODUCTS.ID,
                PRODUCTS.NAME,
                PRODUCTS.PRICE,
                PRODUCTS.PRODUCT_TAG,
                PRODUCTS.IS_IN_STOCK,
                DSL.avg(PRODUCT_RATES.RATE).as(AVG_RATE)
            )
            .from(PRODUCTS)
            .leftJoin(PRODUCT_RATES).on(PRODUCTS.ID.eq(PRODUCT_RATES.PRODUCT_ID))
            .where(PRODUCTS.SUBCATEGORY_ID.eq(subcategoryId))
            .groupBy(PRODUCTS.ID)
            .fetch();
    }

    public List<Record6<Integer, String, Integer, String, Boolean, BigDecimal>> getProductsByName(String name) {
        return dslContext.select(
                PRODUCTS.ID,
                PRODUCTS.NAME,
                PRODUCTS.PRICE,
                PRODUCTS.PRODUCT_TAG,
                PRODUCTS.IS_IN_STOCK,
                DSL.avg(PRODUCT_RATES.RATE).as(AVG_RATE)
            )
            .from(PRODUCTS)
            .leftJoin(PRODUCT_RATES).on(PRODUCTS.ID.eq(PRODUCT_RATES.PRODUCT_ID))
            .where(PRODUCTS.NAME.containsIgnoreCase(name))
            .groupBy(PRODUCTS.ID)
            .fetch();
    }

    public List<Record6<Integer, String, Integer, String, Boolean, BigDecimal>> getProductsByIds(List<Integer> productIds) {
        return dslContext.select(
                PRODUCTS.ID,
                PRODUCTS.NAME,
                PRODUCTS.PRICE,
                PRODUCTS.PRODUCT_TAG,
                PRODUCTS.IS_IN_STOCK,
                DSL.avg(PRODUCT_RATES.RATE).as(AVG_RATE)
            )
            .from(PRODUCTS)
            .leftJoin(PRODUCT_RATES).on(PRODUCTS.ID.eq(PRODUCT_RATES.PRODUCT_ID))
            .where(PRODUCTS.ID.in(productIds))
            .groupBy(PRODUCTS.ID)
            .fetch();
    }

    public void updateProduct(Product product) {
        dslContext.update(PRODUCTS)
            .set(PRODUCTS.NAME, product.name())
            .set(PRODUCTS.DESCRIPTION, product.description())
            .set(PRODUCTS.PRICE, product.price())
            .set(PRODUCTS.SUBCATEGORY_ID, product.subcategoryId())
            .set(PRODUCTS.SIZE_GRID_TYPE, product.sizeGridType().name())
            .set(PRODUCTS.PRODUCT_TAG, product.tag().name())
            .where(PRODUCTS.ID.eq(product.id()))
            .execute();
    }

    public void updateStockAvailability(Integer productId, boolean isInStock) {
        dslContext.update(PRODUCTS)
            .set(PRODUCTS.IS_IN_STOCK, isInStock)
            .where(PRODUCTS.ID.eq(productId))
            .execute();
    }

    public void deleteProduct(Integer productId) {
        dslContext.deleteFrom(PRODUCTS)
            .where(PRODUCTS.ID.eq(productId))
            .execute();
    }

    public boolean isProductExist(Integer productId) {
        return dslContext.fetchExists(dslContext.selectFrom(PRODUCTS)
            .where(PRODUCTS.ID.eq(productId))
        );
    }
}
