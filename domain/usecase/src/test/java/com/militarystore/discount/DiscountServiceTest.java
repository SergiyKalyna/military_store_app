package com.militarystore.discount;

import com.militarystore.entity.discount.Discount;
import com.militarystore.exception.MsNotFoundException;
import com.militarystore.port.out.discount.DiscountPort;
import com.militarystore.utils.DiscountProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DiscountServiceTest {

    private static final Integer USER_ID = 1;

    @Mock
    private DiscountPort discountPort;

    private DiscountService discountService;

    @BeforeEach
    void setUp() {
        discountService = new DiscountService(discountPort);
    }

    @Test
    void createUserDiscountCode() {
        var expirationDate = LocalDateTime.now();
        var discountCode = "asdajslkdjaslkdj";
        var expectedDiscount = Discount.builder()
            .userId(USER_ID)
            .discountCode(discountCode)
            .discount(0.03)
            .usageLimit(3)
            .expirationDate(expirationDate.plusDays(30))
            .build();

        try (var discountProviderMockedStatic = mockStatic(DiscountProvider.class);
             var localDateTimeMockedStatic = mockStatic(LocalDateTime.class)
        ) {
            discountProviderMockedStatic.when(DiscountProvider::generateDiscountCode).thenReturn(discountCode);
            localDateTimeMockedStatic.when(LocalDateTime::now).thenReturn(expirationDate);

            when(discountPort.createUserDiscountCode(expectedDiscount)).thenReturn(discountCode);

            assertThat(discountService.createUserDiscountCode(USER_ID)).isEqualTo(discountCode);
        }
    }

    @Test
    void getUserDiscounts() {
        var expectedDiscounts = List.of(Discount.builder().build());

        when(discountPort.getUserDiscounts(USER_ID)).thenReturn(expectedDiscounts);

        assertThat(discountService.getUserDiscounts(USER_ID)).isEqualTo(expectedDiscounts);
    }

    @Test
    void getUserDiscountByCode_shouldReturnDiscount_whenItAvailableForUser() {
        var discountCode = "code";
        var discount = 0.03;

        when(discountPort.isAvailableUserDiscount(discountCode, USER_ID)).thenReturn(true);
        when(discountPort.getUserDiscountByCode(discountCode, USER_ID)).thenReturn(discount);

        assertThat(discountService.getUserDiscountByCode(discountCode, USER_ID)).isEqualTo(discount);
    }

    @Test
    void getUserDiscountByCode_shouldThrowException_whenDiscountIsNotAvailableForUser() {
        var discountCode = "code";

        when(discountPort.isAvailableUserDiscount(discountCode, USER_ID)).thenReturn(false);

        assertThatThrownBy(() -> discountService.getUserDiscountByCode(discountCode, USER_ID))
            .isInstanceOf(MsNotFoundException.class)
            .hasMessage("Discount code is not available for user with id - " + USER_ID);
    }
}