package com.militarystore.port.in.discount;

import com.militarystore.entity.discount.Discount;

import java.util.List;

public interface DiscountUseCase {

    String createUserDiscountCode(Integer userId);

    List<Discount> getUserDiscounts(Integer userId);

    Double getUserDiscountByCode(String discountCode, Integer userId);
}
