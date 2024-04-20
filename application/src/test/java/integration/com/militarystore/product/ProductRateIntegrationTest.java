package com.militarystore.product;

import com.militarystore.IntegrationTest;
import com.militarystore.entity.product.model.ProductSize;
import com.militarystore.entity.product.model.ProductSizeGridType;
import com.militarystore.entity.product.model.ProductTag;
import com.militarystore.port.in.product.ProductRateUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static com.militarystore.jooq.Tables.CATEGORIES;
import static com.militarystore.jooq.Tables.PRODUCTS;
import static com.militarystore.jooq.Tables.PRODUCT_STOCK_DETAILS;
import static com.militarystore.jooq.Tables.SUBCATEGORIES;
import static com.militarystore.jooq.Tables.USERS;
import static org.assertj.core.api.Assertions.assertThat;

class ProductRateIntegrationTest extends IntegrationTest {

    private static final int SUBCATEGORY_ID = 1;
    private static final int PRODUCT_ID = 10;
    private static final int PRODUCT_STOCK_DETAILS_ID = 11;
    private static final int USER_ID = 111;

    @Autowired
    private ProductRateUseCase productRateUseCase;

    @Test
    void rateProduct() {
        initializeCategories();
        initializeProduct();
        initializeUser(USER_ID, "login");

        var rate = 5.5;
        productRateUseCase.rateProduct(PRODUCT_ID, USER_ID, rate);

        assertThat(productRateUseCase.getAverageRateByProductId(PRODUCT_ID)).isEqualTo(rate);
    }

    @Test
    void rateProduct_shouldUpdateRate_whenUserAlreadyRatedProduct() {
        initializeCategories();
        initializeProduct();
        initializeUser(USER_ID, "login");

        var rate = 5.5;
        productRateUseCase.rateProduct(PRODUCT_ID, USER_ID, rate);

        var newRate = 4.5;
        productRateUseCase.rateProduct(PRODUCT_ID, USER_ID, newRate);

        assertThat(productRateUseCase.getAverageRateByProductId(PRODUCT_ID)).isEqualTo(newRate);
    }

    @Test
    void getAverageRateByProductId_shouldReturn0_whenProductNotRated() {
        initializeCategories();
        initializeProduct();

        assertThat(productRateUseCase.getAverageRateByProductId(PRODUCT_ID)).isZero();
    }

    @Test
    void getAverageRateByProductId_shouldReturnCorrectRate_whenProductRated() {
        initializeCategories();
        initializeProduct();
        initializeUser(USER_ID, "login");
        initializeUser(2, "login2");

        var firstRate = 5.5;
        var secondRate = 7.5;
        productRateUseCase.rateProduct(PRODUCT_ID, USER_ID, firstRate);
        productRateUseCase.rateProduct(PRODUCT_ID, 2, secondRate);

        var expectedRate = 6.5;

        assertThat(productRateUseCase.getAverageRateByProductId(PRODUCT_ID)).isEqualTo(expectedRate);
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

    private void initializeProduct() {
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
            .set(PRODUCT_STOCK_DETAILS.STOCK_AVAILABILITY, 10)
            .execute();
    }

    private void initializeUser(int userId, String login) {
        dslContext.insertInto(USERS)
            .set(USERS.ID, userId)
            .set(USERS.LOGIN, login)
            .set(USERS.PASSWORD, "password")
            .set(USERS.EMAIL, "email")
            .set(USERS.FIRST_NAME, "firstName")
            .set(USERS.SECOND_NAME, "secondName")
            .set(USERS.PHONE, "+380935334711")
            .set(USERS.BIRTHDAY_DATE, LocalDate.EPOCH)
            .set(USERS.ROLE, "USER")
            .set(USERS.GENDER, "MAN")
            .set(USERS.IS_BANNED, false)
            .execute();
    }
}
