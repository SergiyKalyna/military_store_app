package com.militarystore.discount;

import com.militarystore.IntegrationTest;
import com.militarystore.entity.discount.Discount;
import com.militarystore.entity.user.model.Gender;
import com.militarystore.entity.user.model.Role;
import com.militarystore.port.in.discount.DiscountUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.militarystore.jooq.Tables.DISCOUNTS;
import static com.militarystore.jooq.Tables.USERS;
import static org.assertj.core.api.Assertions.assertThat;

class DiscountIntegrationTest extends IntegrationTest {

    private static final int USER_ID = 1;

    @Autowired
    private DiscountUseCase discountUseCase;

    @Test
    void createUserDiscountCode() {
        initializeUser();

        var discountCode = discountUseCase.createUserDiscountCode(USER_ID);
        var discountFromDb = discountUseCase.getUserDiscounts(USER_ID).get(0);
        var expectedDiscount = Discount.builder()
            .userId(USER_ID)
            .discountCode(discountCode)
            .discount(0.03)
            .usageLimit(3)
            .expirationDate(LocalDateTime.now().plusDays(30))
            .build();

        assertEquals(expectedDiscount, discountFromDb);
    }

    @Test
    void getUserDiscounts_whenDiscountDateExpired_shouldReturnEmptyList() {
        var expirationDate = LocalDateTime.of(2021, 1, 1, 0, 0, 0);
        initializeUser();
        initializeDiscount(3, expirationDate);

        assertThat(discountUseCase.getUserDiscounts(USER_ID)).isEmpty();
    }

    @Test
    void getUserDiscounts_whenUsageLimitsExceeded_shouldReturnEmptyList() {
        var expirationDate = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
        initializeUser();
        initializeDiscount(0, expirationDate);

        assertThat(discountUseCase.getUserDiscounts(USER_ID)).isEmpty();
    }

    @Test
    void getUserDiscounts() {
        var expirationDate = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
        initializeUser();
        initializeDiscount(3, expirationDate);

        var expectedDiscount = Discount.builder()
            .userId(USER_ID)
            .discountCode("asdajslkdjaslkdj")
            .discount(0.03)
            .usageLimit(3)
            .expirationDate(expirationDate)
            .build();

        assertThat(discountUseCase.getUserDiscounts(USER_ID)).isEqualTo(List.of(expectedDiscount));
    }

    private void assertEquals(Discount discount, Discount discountFromDb) {
        assertThat(discountFromDb.discount()).isEqualTo(discount.discount());
        assertThat(discountFromDb.usageLimit()).isEqualTo(discount.usageLimit());
        assertThat(discountFromDb.discountCode()).isEqualTo(discount.discountCode());
        assertThat(discountFromDb.userId()).isEqualTo(discount.userId());
        assertThat(discountFromDb.expirationDate().toLocalDate()).isEqualTo(discount.expirationDate().toLocalDate());
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

    private void initializeDiscount(int limits, LocalDateTime expirationDate) {
        dslContext.insertInto(DISCOUNTS)
            .set(DISCOUNTS.USER_ID, USER_ID)
            .set(DISCOUNTS.DISCOUNT_CODE, "asdajslkdjaslkdj")
            .set(DISCOUNTS.DISCOUNT, 0.03)
            .set(DISCOUNTS.USAGE_LIMIT, limits)
            .set(DISCOUNTS.EXPIRATION_DATE, expirationDate)
            .execute();
    }
}
