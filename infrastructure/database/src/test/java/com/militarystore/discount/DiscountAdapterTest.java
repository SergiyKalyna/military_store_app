package com.militarystore.discount;

import com.militarystore.discount.mapper.DiscountMapper;
import com.militarystore.entity.discount.Discount;
import com.militarystore.jooq.tables.records.DiscountsRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DiscountAdapterTest {

    @Mock
    private DiscountRepository discountRepository;

    @Mock
    private DiscountMapper discountMapper;

    private DiscountAdapter discountAdapter;

    @BeforeEach
    void setUp() {
        discountAdapter = new DiscountAdapter(discountRepository, discountMapper);
    }

    @Test
    void createUserDiscountCode() {
        var discount = Discount.builder().build();
        var discountCode = "code";

        when(discountRepository.createUserDiscountCode(discount)).thenReturn(discountCode);

        assertThat(discountAdapter.createUserDiscountCode(discount)).isEqualTo(discountCode);
    }

    @Test
    void updateDiscountUsageLimit() {
        discountAdapter.updateDiscountUsageLimit("code", 1);

        verify(discountRepository).updateDiscountUsageLimit("code", 1);
    }

    @Test
    void deleteUserDiscounts() {
        discountAdapter.deleteUserDiscounts(1);

        verify(discountRepository).deleteUserDiscounts(1);
    }

    @Test
    void isAvailableUserDiscount() {
        discountAdapter.isAvailableUserDiscount("code", 1);

        verify(discountRepository).isAvailableUserDiscount("code", 1);
    }

    @Test
    void getUserDiscounts() {
        var discountRecord = new DiscountsRecord();
        var discount = Discount.builder().build();

        when(discountRepository.getUserDiscounts(1)).thenReturn(List.of(discountRecord));
        when(discountMapper.map(discountRecord)).thenReturn(discount);

        assertThat(discountAdapter.getUserDiscounts(1)).containsExactly(discount);
    }
}