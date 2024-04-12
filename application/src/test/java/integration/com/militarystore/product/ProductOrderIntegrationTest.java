package com.militarystore.product;

import com.militarystore.IntegrationTest;
import com.militarystore.entity.product.model.ProductSize;
import com.militarystore.entity.product.model.ProductSizeGridType;
import com.militarystore.entity.product.model.ProductTag;
import com.militarystore.port.in.product.ProductOrderUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.militarystore.jooq.Tables.CATEGORIES;
import static com.militarystore.jooq.Tables.PRODUCTS;
import static com.militarystore.jooq.Tables.PRODUCT_STOCK_DETAILS;
import static com.militarystore.jooq.Tables.SUBCATEGORIES;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductOrderIntegrationTest extends IntegrationTest {

    private static final int SUBCATEGORY_ID = 1;
    private static final int PRODUCT_ID = 10;
    private static final int PRODUCT_STOCK_DETAILS_ID = 11;

    @Autowired
    private ProductOrderUseCase productOrderUseCase;

    @Test
    void updateProductStockAvailability_shouldMakeProductAvailable_whenAfterOrderingProductAvailable() {
        initializeCategories();
        initializeProduct(2);

        productOrderUseCase.updateProductStockAvailability(PRODUCT_ID, PRODUCT_STOCK_DETAILS_ID, 1);

        var isProductAvailable = isProductAvailable();

        assertTrue(isProductAvailable);
    }

    @Test
    void updateProductStockAvailability_shouldMakeProductUnavailable_whenAfterOrderingProductUnavailable() {
        initializeCategories();
        initializeProduct(1);

        productOrderUseCase.updateProductStockAvailability(PRODUCT_ID, PRODUCT_STOCK_DETAILS_ID, 1);

        var isProductAvailable = isProductAvailable();

        assertFalse(isProductAvailable);
    }

    @Test
    void isProductInStock_shouldReturnFalse_whenStockAvailability0() {
        initializeCategories();
        initializeProduct(0);

        assertFalse(productOrderUseCase.isEnoughProductStockAvailability(PRODUCT_STOCK_DETAILS_ID, 1));
    }

    @Test
    void isProductInStock_shouldReturnTrue_whenStockAvailability1() {
        initializeCategories();
        initializeProduct(1);

        assertTrue(productOrderUseCase.isEnoughProductStockAvailability(PRODUCT_STOCK_DETAILS_ID, 1));
    }

    private void initializeCategories() {
        dslContext.insertInto(CATEGORIES)
            .set(CATEGORIES.ID, 1)
            .set(CATEGORIES.NAME, "Category")
            .execute();

        dslContext.insertInto(SUBCATEGORIES)
            .set(SUBCATEGORIES.ID, SUBCATEGORY_ID)
            .set(SUBCATEGORIES.NAME, "Subcategory")
            .set(SUBCATEGORIES.CATEGORY_ID, 1)
            .execute();
    }

    private void initializeProduct(int productStockAvailabilities) {
        dslContext.insertInto(PRODUCTS)
            .set(PRODUCTS.ID, PRODUCT_ID)
            .set(PRODUCTS.NAME, "Product")
            .set(PRODUCTS.DESCRIPTION, "Product description")
            .set(PRODUCTS.PRICE, 100)
            .set(PRODUCTS.SUBCATEGORY_ID, SUBCATEGORY_ID)
            .set(PRODUCTS.SIZE_GRID_TYPE, ProductSizeGridType.CLOTHES.name())
            .set(PRODUCTS.PRODUCT_TAG, ProductTag.NEW.name())
            .set(PRODUCTS.IS_IN_STOCK, true)
            .execute();

        dslContext.insertInto(PRODUCT_STOCK_DETAILS)
            .set(PRODUCT_STOCK_DETAILS.ID, PRODUCT_STOCK_DETAILS_ID)
            .set(PRODUCT_STOCK_DETAILS.PRODUCT_ID, PRODUCT_ID)
            .set(PRODUCT_STOCK_DETAILS.PRODUCT_SIZE, ProductSize.M.name())
            .set(PRODUCT_STOCK_DETAILS.STOCK_AVAILABILITY, productStockAvailabilities)
            .execute();
    }

    private boolean isProductAvailable() {
        return Boolean.TRUE.equals(dslContext.select(PRODUCTS.IS_IN_STOCK)
            .from(PRODUCTS)
            .where(PRODUCTS.ID.eq(PRODUCT_ID))
            .fetchOne(PRODUCTS.IS_IN_STOCK));
    }
}
