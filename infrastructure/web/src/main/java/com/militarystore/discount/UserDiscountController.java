package com.militarystore.discount;

import com.militarystore.converter.discount.DiscountConverter;
import com.militarystore.entity.user.User;
import com.militarystore.model.dto.discount.DiscountDto;
import com.militarystore.port.in.discount.DiscountUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/discount")
public class UserDiscountController {

    private final DiscountUseCase discountUseCase;
    private final DiscountConverter discountConverter;

    @PostMapping
    public String createUserDiscountCode(@AuthenticationPrincipal User user) {
       return discountUseCase.createUserDiscountCode(user.id());
    }

    @GetMapping
    public List<DiscountDto> getUserDiscounts(@AuthenticationPrincipal User user) {
        var userDiscounts = discountUseCase.getUserDiscounts(user.id());

        return userDiscounts.stream()
            .map(discountConverter::toDto)
            .toList();
    }

    @GetMapping("/discount-code/{discountCode}")
    public Double getUserDiscountByCode(
        @PathVariable("discountCode") String discountCode,
        @AuthenticationPrincipal User user
    ) {
        return discountUseCase.getUserDiscountByCode(discountCode, user.id());
    }
}
