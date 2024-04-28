package com.militarystore.wishlist;

import com.militarystore.IntegrationTest;
import com.militarystore.entity.product.Product;
import com.militarystore.entity.product.model.ProductSizeGridType;
import com.militarystore.entity.product.model.ProductTag;
import com.militarystore.port.in.wishlist.UserWishlistUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static com.militarystore.jooq.Tables.CATEGORIES;
import static com.militarystore.jooq.Tables.PRODUCTS;
import static com.militarystore.jooq.Tables.SUBCATEGORIES;
import static com.militarystore.jooq.Tables.USERS;
import static org.assertj.core.api.Assertions.assertThat;

class UserWishlistIntegrationTest extends IntegrationTest {

    private static final Integer USER_ID = 1;
    private static final Integer SUBCATEGORY_ID = 1;
    private static final Integer PRODUCT_ID = 11;

    @Autowired
    private UserWishlistUseCase userWishlistUseCase;

    @Test
    void addProductToUserWishlist() {
        initializeCategories();
        initializeProduct();
        initializeUser();

        userWishlistUseCase.addProductToUserWishlist(PRODUCT_ID, USER_ID);

        var expectedUserWishlistProducts = List.of(Product.builder()
            .id(PRODUCT_ID)
            .name("Product")
            .price(100)
            .isInStock(true)
            .tag(ProductTag.NEW)
            .build());

        assertThat(userWishlistUseCase.getUserWishlistProducts(USER_ID)).isEqualTo(expectedUserWishlistProducts);
    }

    @Test
    void deleteProductFromUserWishlist() {
        initializeCategories();
        initializeProduct();
        initializeUser();

        userWishlistUseCase.addProductToUserWishlist(PRODUCT_ID, USER_ID);
        assertThat(userWishlistUseCase.getUserWishlistProducts(USER_ID)).hasSize(1);

        userWishlistUseCase.deleteProductFromUserWishlist(PRODUCT_ID, USER_ID);
        assertThat(userWishlistUseCase.getUserWishlistProducts(USER_ID)).isEmpty();
    }

    @Test
    void deleteAllUserProductsFromWishlist() {
        initializeCategories();
        initializeProduct();
        initializeUser();

        userWishlistUseCase.addProductToUserWishlist(PRODUCT_ID, USER_ID);
        assertThat(userWishlistUseCase.getUserWishlistProducts(USER_ID)).hasSize(1);

        userWishlistUseCase.deleteAllUserProductsFromWishlist(USER_ID);
        assertThat(userWishlistUseCase.getUserWishlistProducts(USER_ID)).isEmpty();
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
            .set(USERS.ROLE, "USER")
            .set(USERS.GENDER, "MAN")
            .set(USERS.IS_BANNED, false)
            .execute();
    }
}
