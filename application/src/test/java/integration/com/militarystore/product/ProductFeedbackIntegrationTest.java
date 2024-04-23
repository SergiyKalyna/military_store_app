package com.militarystore.product;

import com.militarystore.IntegrationTest;
import com.militarystore.entity.product.ProductFeedback;
import com.militarystore.entity.product.model.ProductSize;
import com.militarystore.entity.product.model.ProductSizeGridType;
import com.militarystore.entity.product.model.ProductTag;
import com.militarystore.entity.user.model.Gender;
import com.militarystore.entity.user.model.Role;
import com.militarystore.exception.MsAccessDeniedException;
import com.militarystore.port.in.product.ProductFeedbackUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.militarystore.jooq.Tables.CATEGORIES;
import static com.militarystore.jooq.Tables.PRODUCTS;
import static com.militarystore.jooq.Tables.PRODUCT_STOCK_DETAILS;
import static com.militarystore.jooq.Tables.SUBCATEGORIES;
import static com.militarystore.jooq.Tables.USERS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ProductFeedbackIntegrationTest extends IntegrationTest {

    private static final Integer USER_ID = 1;
    private static final Integer SUBCATEGORY_ID = 1;
    private static final Integer PRODUCT_STOCK_DETAILS_ID = 1;
    private static final Integer PRODUCT_ID = 11;
    private static final String FEEDBACK = "Good product";

    @Autowired
    private ProductFeedbackUseCase productFeedbackUseCase;

    @Test
    void saveFeedback() {
        initializeCategories();
        initializeProduct();
        initializeUser(Role.USER, USER_ID, "login");

        var feedback = ProductFeedback.builder()
            .userId(USER_ID)
            .productId(PRODUCT_ID)
            .feedback(FEEDBACK)
            .dateTime(LocalDateTime.of(2021, 1, 1, 0, 0))
            .build();

        var feedbackId = productFeedbackUseCase.saveFeedback(feedback);
        var feedbackFromDb = productFeedbackUseCase.getFeedbackById(feedbackId);

        assertEquals(feedback, feedbackFromDb);
    }

    @Test
    void updateProduct_whenUserLeaveThisFeedback_shouldUpdateProductFeedback() {
        initializeCategories();
        initializeProduct();
        initializeUser(Role.USER, USER_ID, "login");

        var feedback = ProductFeedback.builder()
            .userId(USER_ID)
            .productId(PRODUCT_ID)
            .feedback(FEEDBACK)
            .dateTime(LocalDateTime.of(2021, 1, 1, 0, 0))
            .build();

        var feedbackId = productFeedbackUseCase.saveFeedback(feedback);
        var newFeedback = ProductFeedback.builder()
            .id(feedbackId)
            .userId(USER_ID)
            .productId(PRODUCT_ID)
            .feedback("New feedback")
            .dateTime(LocalDateTime.of(2022, 1, 1, 0, 0))
            .build();

        productFeedbackUseCase.updateFeedback(newFeedback);
        var feedbackFromDb = productFeedbackUseCase.getFeedbackById(feedbackId);

        assertEquals(newFeedback, feedbackFromDb);
    }

    @Test
    void updateProduct_whenUserDontLeaveFeedback_shoulThrowException() {
        initializeCategories();
        initializeProduct();
        initializeUser(Role.USER, USER_ID, "login");

        var feedback = ProductFeedback.builder()
            .userId(USER_ID)
            .productId(PRODUCT_ID)
            .feedback(FEEDBACK)
            .dateTime(LocalDateTime.of(2021, 1, 1, 0, 0))
            .build();

        var feedbackId = productFeedbackUseCase.saveFeedback(feedback);
        var newFeedback = ProductFeedback.builder()
            .id(feedbackId)
            .userId(2)
            .productId(PRODUCT_ID)
            .feedback("New feedback")
            .dateTime(LocalDateTime.of(2022, 1, 1, 0, 0))
            .build();

        assertThatThrownBy(() -> productFeedbackUseCase.updateFeedback(newFeedback))
            .isInstanceOf(RuntimeException.class);
    }

    @Test
    void deleteFeedback_whenUserLeaveFeedback_shouldDeleteFeedback() {
        initializeCategories();
        initializeProduct();
        initializeUser(Role.USER, USER_ID, "login");

        var feedback = ProductFeedback.builder()
            .userId(USER_ID)
            .productId(PRODUCT_ID)
            .feedback(FEEDBACK)
            .dateTime(LocalDateTime.of(2021, 1, 1, 0, 0))
            .build();

        var feedbackId = productFeedbackUseCase.saveFeedback(feedback);
        productFeedbackUseCase.deleteFeedback(feedbackId, USER_ID);

        assertThat(productFeedbackUseCase.getFeedbacksByProductId(feedbackId)).isEmpty();
    }

    @Test
    void deleteFeedback_whenUserDontLeaveFeedback_shouldThrowException() {
        initializeCategories();
        initializeProduct();
        initializeUser(Role.USER, USER_ID, "login");

        var feedback = ProductFeedback.builder()
            .userId(USER_ID)
            .productId(PRODUCT_ID)
            .feedback(FEEDBACK)
            .dateTime(LocalDateTime.of(2021, 1, 1, 0, 0))
            .build();

        var feedbackId = productFeedbackUseCase.saveFeedback(feedback);

        assertThatThrownBy(() -> productFeedbackUseCase.deleteFeedback(feedbackId, 2))
            .isInstanceOf(MsAccessDeniedException.class);
    }

    @Test
    void deleteFeedback_whenUserIsAdmin_shouldDeleteFeedback() {
        initializeCategories();
        initializeProduct();
        initializeUser(Role.USER, USER_ID, "login");
        initializeUser(Role.ADMIN, 2, "admin");

        var feedback = ProductFeedback.builder()
            .userId(USER_ID)
            .productId(PRODUCT_ID)
            .feedback(FEEDBACK)
            .dateTime(LocalDateTime.of(2021, 1, 1, 0, 0))
            .build();

        var feedbackId = productFeedbackUseCase.saveFeedback(feedback);
        productFeedbackUseCase.deleteFeedback(feedbackId, 2);

        assertThat(productFeedbackUseCase.getFeedbacksByProductId(feedbackId)).isEmpty();
    }

    @Test
    void deleteFeedback_whenUserIsSuperAdmin_shouldDeleteFeedback() {
        initializeCategories();
        initializeProduct();
        initializeUser(Role.USER, USER_ID, "login");
        initializeUser(Role.SUPER_ADMIN, 2, "superAdmin");

        var feedback = ProductFeedback.builder()
            .userId(USER_ID)
            .productId(PRODUCT_ID)
            .feedback(FEEDBACK)
            .dateTime(LocalDateTime.of(2021, 1, 1, 0, 0))
            .build();

        var feedbackId = productFeedbackUseCase.saveFeedback(feedback);
        productFeedbackUseCase.deleteFeedback(feedbackId, 2);

        assertThat(productFeedbackUseCase.getFeedbacksByProductId(feedbackId)).isEmpty();
    }

    @Test
    void getFeedbackById() {
        initializeCategories();
        initializeProduct();
        initializeUser(Role.USER, USER_ID, "login");

        var feedback = ProductFeedback.builder()
            .userId(USER_ID)
            .productId(PRODUCT_ID)
            .feedback(FEEDBACK)
            .dateTime(LocalDateTime.of(2021, 1, 1, 0, 0))
            .build();

        var feedbackId = productFeedbackUseCase.saveFeedback(feedback);
        var feedbackFromDb = productFeedbackUseCase.getFeedbackById(feedbackId);

        assertEquals(feedback, feedbackFromDb);
    }

    @Test
    void getFeedbacksByProductId() {
        initializeCategories();
        initializeProduct();
        initializeUser(Role.USER, USER_ID, "login");

        var feedback = ProductFeedback.builder()
            .userId(USER_ID)
            .productId(PRODUCT_ID)
            .feedback(FEEDBACK)
            .dateTime(LocalDateTime.of(2021, 1, 1, 0, 0))
            .build();

        productFeedbackUseCase.saveFeedback(feedback);
        var feedbacks = productFeedbackUseCase.getFeedbacksByProductId(PRODUCT_ID);

        assertThat(feedbacks).hasSize(1);
        assertEquals(feedback, feedbacks.get(0));
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

    private void initializeUser(Role userRole, int userId, String login) {
        dslContext.insertInto(USERS)
            .set(USERS.ID, userId)
            .set(USERS.LOGIN, login)
            .set(USERS.PASSWORD, "password")
            .set(USERS.EMAIL, "email")
            .set(USERS.FIRST_NAME, "firstName")
            .set(USERS.SECOND_NAME, "secondName")
            .set(USERS.PHONE, "+380935334711")
            .set(USERS.BIRTHDAY_DATE, LocalDate.EPOCH)
            .set(USERS.ROLE, userRole.name())
            .set(USERS.GENDER, Gender.MALE.name())
            .set(USERS.IS_BANNED, false)
            .execute();
    }

    private void assertEquals(ProductFeedback expected, ProductFeedback actual) {
        assertThat(actual.userId()).isEqualTo(expected.userId());
        assertThat(actual.feedback()).isEqualTo(expected.feedback());
        assertThat(actual.dateTime()).isEqualTo(expected.dateTime());
    }
}
