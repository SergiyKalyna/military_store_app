package com.militarystore.discount;

import com.militarystore.discount.mapper.DiscountMapper;
import com.militarystore.entity.discount.Discount;
import com.militarystore.port.out.discount.DiscountPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DiscountAdapter implements DiscountPort {

    private final DiscountRepository discountRepository;
    private final DiscountMapper discountMapper;

    @Override
    public String createUserDiscountCode(Discount discount) {
       return discountRepository.createUserDiscountCode(discount);
    }

    @Override
    public void updateDiscountUsageLimit(String discountCode, Integer userId) {
        discountRepository.updateDiscountUsageLimit(discountCode, userId);
    }

    @Override
    public void deleteUserDiscounts(Integer userId) {
        discountRepository.deleteUserDiscounts(userId);
    }

    @Override
    public boolean isAvailableUserDiscount(String discountCode, Integer userId) {
        return discountRepository.isAvailableUserDiscount(discountCode, userId);
    }

    @Override
    public List<Discount> getUserDiscounts(Integer userId) {
        return discountRepository.getUserDiscounts(userId).stream()
            .map(discountMapper::map)
            .toList();
    }
}
