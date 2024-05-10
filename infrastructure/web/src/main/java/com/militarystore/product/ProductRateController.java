package com.militarystore.product;

import com.militarystore.entity.user.User;
import com.militarystore.port.in.product.ProductRateUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products/rate")
public class ProductRateController {

    private final ProductRateUseCase productRateUseCase;

    @PostMapping("/{productId}")
    public void rateProduct(
        @PathVariable("productId") Integer productId,
        @RequestParam("productRate") double productRate,
        @AuthenticationPrincipal User user
    ) {
        productRateUseCase.rateProduct(productId, user.id(), productRate);
    }

    @GetMapping("/{productId}")
    public double getAverageRateByProductId(@PathVariable("productId") Integer productId) {
        return productRateUseCase.getAverageRateByProductId(productId);
    }
}
