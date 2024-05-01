package com.militarystore.user;

import com.militarystore.IntegrationTest;
import com.militarystore.entity.product.model.ProductSize;
import com.militarystore.entity.product.model.ProductSizeGridType;
import com.militarystore.entity.product.model.ProductTag;
import com.militarystore.entity.user.User;
import com.militarystore.entity.user.model.Gender;
import com.militarystore.entity.user.model.Role;
import com.militarystore.exception.MsNotFoundException;
import com.militarystore.port.in.user.DeleteUserUseCase;
import com.militarystore.port.in.user.GetUserUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.militarystore.jooq.Tables.BASKETS;
import static com.militarystore.jooq.Tables.CATEGORIES;
import static com.militarystore.jooq.Tables.PRODUCTS;
import static com.militarystore.jooq.Tables.PRODUCT_FEEDBACKS;
import static com.militarystore.jooq.Tables.PRODUCT_RATES;
import static com.militarystore.jooq.Tables.PRODUCT_STOCK_DETAILS;
import static com.militarystore.jooq.Tables.SUBCATEGORIES;
import static com.militarystore.jooq.Tables.USERS;
import static com.militarystore.jooq.Tables.WISHLISTS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class DeleteUserIntegrationTest extends IntegrationTest {

    private static final Integer SUBCATEGORY_ID = 1;
    private static final Integer PRODUCT_ID = 2;
    private static final Integer PRODUCT_STOCK_DETAILS_ID = 3;
    private static final Integer USER_ID = 1;

    @Autowired
    private DeleteUserUseCase deleteUserUseCase;

    @Autowired
    private GetUserUseCase getUserUseCase;

    @Test
    void whenUserNotFound_shouldThrowUserNotFoundException() {
        assertThatThrownBy(() -> deleteUserUseCase.deleteUser(USER_ID))
            .isInstanceOf(MsNotFoundException.class)
            .hasMessage("User with id [1] is not found");
    }

    @Test
    void deleteUser() {
        var user = User.builder()
            .id(USER_ID)
            .login("johny_iva")
            .password("password")
            .firstName("firstName")
            .secondName("secondName")
            .email("email")
            .gender(Gender.MALE)
            .phone("+(380)935334711")
            .role(Role.USER)
            .birthdayDate(LocalDate.of(2000, 1, 1))
            .isBanned(true)
            .build();

        var userId = createUser(user);

        assertThat(getUserUseCase.getUsers()).isNotEmpty();

        deleteUserUseCase.deleteUser(userId);

        assertThat(getUserUseCase.getUsers()).isEmpty();
    }

    @Test
    void deleteUserWithAllRelations() {
        var user = User.builder()
            .id(USER_ID)
            .login("johny_iva")
            .password("password")
            .firstName("firstName")
            .secondName("secondName")
            .email("email")
            .gender(Gender.MALE)
            .phone("+(380)935334711")
            .role(Role.USER)
            .birthdayDate(LocalDate.of(2000, 1, 1))
            .isBanned(true)
            .build();

        initializeCategories();
        initializeProduct();
        createUser(user);
        initializeProductRates();
        initializeBasket();
        initializeFeedback();
        initializeWishlist();

        assertDoesNotThrow(() -> deleteUserUseCase.deleteUser(USER_ID));
        assertThat(getUserUseCase.getUsers()).isEmpty();
    }

    private Integer createUser(User user) {
        return dslContext.insertInto(USERS)
            .set(USERS.ID, user.id())
            .set(USERS.LOGIN, user.login())
            .set(USERS.PASSWORD, user.password())
            .set(USERS.FIRST_NAME, user.firstName())
            .set(USERS.SECOND_NAME, user.secondName())
            .set(USERS.EMAIL, user.email())
            .set(USERS.PHONE, user.phone())
            .set(USERS.GENDER, user.gender().name())
            .set(USERS.BIRTHDAY_DATE, user.birthdayDate())
            .set(USERS.ROLE, user.role().name())
            .set(USERS.IS_BANNED, user.isBanned())
            .returningResult(USERS.ID)
            .fetchOne(USERS.ID);
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

    private void initializeProductRates() {
        dslContext.insertInto(PRODUCT_RATES)
            .set(PRODUCT_RATES.ID, 1)
            .set(PRODUCT_RATES.PRODUCT_ID, PRODUCT_ID)
            .set(PRODUCT_RATES.USER_ID, USER_ID)
            .set(PRODUCT_RATES.RATE, 5.0)
            .execute();
    }

    private void initializeBasket() {
        dslContext.insertInto(BASKETS)
            .set(BASKETS.PRODUCT_STOCK_DETAILS_ID, PRODUCT_STOCK_DETAILS_ID)
            .set(BASKETS.USER_ID, USER_ID)
            .set(BASKETS.QUANTITY, 1)
            .execute();
    }

    private void initializeFeedback() {
        dslContext.insertInto(PRODUCT_FEEDBACKS)
            .set(PRODUCT_FEEDBACKS.ID, 1)
            .set(PRODUCT_FEEDBACKS.PRODUCT_ID, PRODUCT_ID)
            .set(PRODUCT_FEEDBACKS.USER_ID, USER_ID)
            .set(PRODUCT_FEEDBACKS.FEEDBACK, "Feedback")
            .set(PRODUCT_FEEDBACKS.DATE_TIME, LocalDateTime.now())
            .execute();
    }

    private void initializeWishlist() {
        dslContext.insertInto(WISHLISTS)
            .set(WISHLISTS.PRODUCT_ID, PRODUCT_ID)
            .set(WISHLISTS.USER_ID, USER_ID)
            .execute();
    }
}
