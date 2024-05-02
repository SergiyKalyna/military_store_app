package com.militarystore.discount;

import com.militarystore.entity.discount.Discount;
import com.militarystore.port.out.discount.DiscountPort;
import com.militarystore.utils.DiscountProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
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
        var discountCode = "asdajslkdjaslkdj";
        var expectedDiscount = Discount.builder()
            .userId(USER_ID)
            .discountCode(discountCode)
            .discount(0.03)
            .usageLimit(3)
            .expirationDate(LocalDate.now().plusDays(30))
            .build();

        try (MockedStatic<DiscountProvider> discountProviderMockedStatic = mockStatic(DiscountProvider.class)) {
            discountProviderMockedStatic.when(DiscountProvider::generateDiscountCode).thenReturn(discountCode);

            discountService.createUserDiscountCode(USER_ID);

            verify(discountPort).createUserDiscountCode(expectedDiscount);
        }
    }

    @Test
    void getUserDiscounts() {
        var expectedDiscounts = List.of(Discount.builder().build());

        when(discountPort.getUserDiscounts(USER_ID)).thenReturn(expectedDiscounts);

        assertThat(discountService.getUserDiscounts(USER_ID)).isEqualTo(expectedDiscounts);
    }
}