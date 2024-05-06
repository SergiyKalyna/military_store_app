package com.militarystore.port.out.discount;

import com.militarystore.entity.discount.Discount;

import java.util.List;

public interface DiscountPort {

    String createUserDiscountCode(Discount discount);

    void updateDiscountUsageLimit(String discountCode, Integer userId);

    void deleteUserDiscounts(Integer userId);

    boolean isAvailableUserDiscount(String discountCode, Integer userId);

    List<Discount> getUserDiscounts(Integer userId);

    Double getUserDiscountByCode(String discountCode, Integer userId);
}
