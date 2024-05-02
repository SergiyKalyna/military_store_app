package com.militarystore.discount;

import com.militarystore.entity.discount.Discount;
import com.militarystore.port.in.discount.DiscountUseCase;
import com.militarystore.port.out.discount.DiscountPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.militarystore.utils.DiscountProvider.generateDiscountCode;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiscountService implements DiscountUseCase {
    private static final int USAGE_LIMIT = 3;
    private static final double DISCOUNT = 0.03;

    private final DiscountPort discountPort;

    @Override
    public String createUserDiscountCode(Integer userId) {
        Discount discount = Discount.builder()
            .userId(userId)
            .discountCode(generateDiscountCode())
            .discount(DISCOUNT)
            .usageLimit(USAGE_LIMIT)
            .expirationDate(LocalDateTime.now().plusDays(30))
            .build();

        var discountCode = discountPort.createUserDiscountCode(discount);
        log.info("Discount code created '{}' for user with id '{}'", discount.discountCode(), userId);

        return discountCode;
    }

    @Override
    public List<Discount> getUserDiscounts(Integer userId) {
        return discountPort.getUserDiscounts(userId);
    }
}
