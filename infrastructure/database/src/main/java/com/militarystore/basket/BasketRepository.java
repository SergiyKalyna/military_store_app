package com.militarystore.basket;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record4;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.militarystore.jooq.Tables.BASKETS;
import static com.militarystore.jooq.Tables.PRODUCTS;
import static com.militarystore.jooq.Tables.PRODUCT_STOCK_DETAILS;

@Repository
@RequiredArgsConstructor
public class BasketRepository {

    private final DSLContext dslContext;

    public void addProductToBasket(Integer productStockDetailsId, Integer userId, Integer quantity) {
        dslContext.insertInto(BASKETS)
            .set(BASKETS.PRODUCT_STOCK_DETAILS_ID, productStockDetailsId)
            .set(BASKETS.USER_ID, userId)
            .set(BASKETS.QUANTITY, quantity)
            .onConflict(BASKETS.PRODUCT_STOCK_DETAILS_ID, BASKETS.USER_ID)
            .doUpdate()
            .set(BASKETS.QUANTITY, BASKETS.QUANTITY.plus(quantity))
            .execute();
    }

    public void updateProductQuantityInBasket(Integer productStockDetailsId, Integer userId, Integer quantity) {
        dslContext.update(BASKETS)
            .set(BASKETS.QUANTITY, quantity)
            .where(BASKETS.PRODUCT_STOCK_DETAILS_ID.eq(productStockDetailsId))
            .and(BASKETS.USER_ID.eq(userId))
            .execute();
    }

    public void deleteProductFromBasket(Integer productStockDetailsId, Integer userId) {
        dslContext.deleteFrom(BASKETS)
            .where(BASKETS.PRODUCT_STOCK_DETAILS_ID.eq(productStockDetailsId))
            .and(BASKETS.USER_ID.eq(userId))
            .execute();
    }

    public void deleteUserProductsFromBasket(Integer userId) {
        dslContext.deleteFrom(BASKETS)
            .where(BASKETS.USER_ID.eq(userId))
            .execute();
    }

    public void deleteProductFromAllBaskets(Integer productStockDetailsId) {
        dslContext.deleteFrom(BASKETS)
            .where(BASKETS.PRODUCT_STOCK_DETAILS_ID.eq(productStockDetailsId))
            .execute();
    }

    public List<Record4<Integer, Integer, String, Integer>> getUserBasketProducts(Integer userId) {
        return dslContext.select(
                BASKETS.QUANTITY,
                PRODUCTS.ID,
                PRODUCTS.NAME,
                PRODUCTS.PRICE
            )
            .from(BASKETS)
            .innerJoin(PRODUCT_STOCK_DETAILS).on(BASKETS.PRODUCT_STOCK_DETAILS_ID.eq(PRODUCT_STOCK_DETAILS.ID))
            .innerJoin(PRODUCTS).on(PRODUCT_STOCK_DETAILS.PRODUCT_ID.eq(PRODUCTS.ID))
            .where(BASKETS.USER_ID.eq(userId))
            .fetch();
    }
}
