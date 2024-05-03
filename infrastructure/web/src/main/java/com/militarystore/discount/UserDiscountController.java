package com.militarystore.discount;

import com.militarystore.converter.discount.DiscountConverter;
import com.militarystore.model.dto.discount.DiscountDto;
import com.militarystore.port.in.discount.DiscountUseCase;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/{userId}")
    public String createUserDiscountCode(@PathVariable("userId") Integer userId) {
       return discountUseCase.createUserDiscountCode(userId);
    }

    @GetMapping("/{userId}")
    public List<DiscountDto> getUserDiscounts(@PathVariable("userId") Integer userId) {
        var userDiscounts = discountUseCase.getUserDiscounts(userId);

        return userDiscounts.stream()
            .map(discountConverter::toDto)
            .toList();
    }
}
