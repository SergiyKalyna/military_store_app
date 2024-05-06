package com.militarystore.basket;

import com.militarystore.IntegrationTest;
import com.militarystore.entity.basket.ProductInBasket;
import com.militarystore.entity.product.model.ProductSize;
import com.militarystore.entity.product.model.ProductSizeGridType;
import com.militarystore.entity.product.model.ProductTag;
import com.militarystore.entity.user.model.Gender;
import com.militarystore.entity.user.model.Role;
import com.militarystore.exception.MsValidationException;
import com.militarystore.port.in.basket.ProductBasketUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static com.militarystore.jooq.Tables.CATEGORIES;
import static com.militarystore.jooq.Tables.PRODUCTS;
import static com.militarystore.jooq.Tables.PRODUCT_STOCK_DETAILS;
import static com.militarystore.jooq.Tables.SUBCATEGORIES;
import static com.militarystore.jooq.Tables.USERS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductBasketIntegrationTest extends IntegrationTest {

    private static final int SUBCATEGORY_ID = 1;
    private static final int PRODUCT_ID = 10;
    private static final int PRODUCT_STOCK_DETAILS_ID = 11;
    private static final int USER_ID = 111;

    @Autowired
    private ProductBasketUseCase productBasketUseCase;

    @Test
    void addProductToBasket() {
        initializeCategories();
        initializeProduct();
        initializeUser();

        productBasketUseCase.addProductToBasket(PRODUCT_STOCK_DETAILS_ID, USER_ID, 1);

        var expectedProductsInBasket = List.of(
            ProductInBasket.builder()
                .productId(PRODUCT_ID)
                .productName("Product")
                .productPrice(100)
                .quantity(1)
                .build()
        );

        assertThat(productBasketUseCase.getUserBasketProducts(USER_ID)).isEqualTo(expectedProductsInBasket);
    }

    @Test
    void addProductToBasket_shouldIncreaseQuantity_whenProductAlreadyInBasket() {
        initializeCategories();
        initializeProduct();
        initializeUser();

        productBasketUseCase.addProductToBasket(PRODUCT_STOCK_DETAILS_ID, USER_ID, 1);
        productBasketUseCase.addProductToBasket(PRODUCT_STOCK_DETAILS_ID, USER_ID, 1);

        var expectedProductsInBasket = List.of(
            ProductInBasket.builder()
                .productId(PRODUCT_ID)
                .productName("Product")
                .productPrice(100)
                .quantity(2)
                .build()
        );

        assertThat(productBasketUseCase.getUserBasketProducts(USER_ID)).isEqualTo(expectedProductsInBasket);
    }

    @Test
    void addProductToBasket_shouldThrowException_whenProductNotInStock() {
        initializeCategories();
        initializeProduct();
        initializeUser();

        assertThrows(
            MsValidationException.class,
            () -> productBasketUseCase.addProductToBasket(PRODUCT_STOCK_DETAILS_ID, USER_ID, 11)
        );
    }

    @Test
    void updateProductQuantityInBasket() {
        initializeCategories();
        initializeProduct();
        initializeUser();

        productBasketUseCase.addProductToBasket(PRODUCT_STOCK_DETAILS_ID, USER_ID, 1);
        productBasketUseCase.updateProductQuantityInBasket(PRODUCT_STOCK_DETAILS_ID, USER_ID, 2);

        var expectedProductsInBasket = List.of(
            ProductInBasket.builder()
                .productId(PRODUCT_ID)
                .productName("Product")
                .productPrice(100)
                .quantity(2)
                .build()
        );

        assertThat(productBasketUseCase.getUserBasketProducts(USER_ID)).isEqualTo(expectedProductsInBasket);
    }

    @Test
    void updateProductQuantityInBasket_shouldThrowException_whenProductNotInBasket() {
        initializeCategories();
        initializeProduct();
        initializeUser();

        assertThrows(
            MsValidationException.class,
            () -> productBasketUseCase.updateProductQuantityInBasket(PRODUCT_STOCK_DETAILS_ID, USER_ID, 11)
        );
    }

    @Test
    void deleteProductFromBasket() {
        initializeCategories();
        initializeProduct();
        initializeUser();

        productBasketUseCase.addProductToBasket(PRODUCT_STOCK_DETAILS_ID, USER_ID, 1);
        productBasketUseCase.deleteProductFromBasket(PRODUCT_STOCK_DETAILS_ID, USER_ID);

        assertThat(productBasketUseCase.getUserBasketProducts(USER_ID)).isEmpty();
    }

    @Test
    void deleteUsersProductsFromBasket() {
        initializeCategories();
        initializeProduct();
        initializeUser();

        productBasketUseCase.addProductToBasket(PRODUCT_STOCK_DETAILS_ID, USER_ID, 1);
        productBasketUseCase.deleteUserProductsFromBasket(USER_ID);

        assertThat(productBasketUseCase.getUserBasketProducts(USER_ID)).isEmpty();
    }

    @Test
    void getUserBasketProducts() {
        initializeCategories();
        initializeProduct();
        initializeUser();

        productBasketUseCase.addProductToBasket(PRODUCT_STOCK_DETAILS_ID, USER_ID, 1);

        var expectedProductsInBasket = List.of(
            ProductInBasket.builder()
                .productId(PRODUCT_ID)
                .productStockDetailsId(PRODUCT_STOCK_DETAILS_ID)
                .productName("Product")
                .productPrice(100)
                .quantity(1)
                .build()
        );

        assertThat(productBasketUseCase.getUserBasketProducts(USER_ID)).isEqualTo(expectedProductsInBasket);
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

    private void initializeUser() {
        dslContext.insertInto(USERS)
            .set(USERS.ID, USER_ID)
            .set(USERS.LOGIN, "login")
            .set(USERS.PASSWORD, "password")
            .set(USERS.EMAIL, "email")
            .set(USERS.FIRST_NAME, "firstName")
            .set(USERS.SECOND_NAME, "secondName")
            .set(USERS.PHONE, "+380935334711")
            .set(USERS.BIRTHDAY_DATE, LocalDate.EPOCH)
            .set(USERS.ROLE, Role.USER.name())
            .set(USERS.GENDER, Gender.MALE.name())
            .set(USERS.IS_BANNED, false)
            .execute();
    }
}
