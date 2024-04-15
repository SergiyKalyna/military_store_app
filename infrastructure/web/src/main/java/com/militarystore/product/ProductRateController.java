package com.militarystore.product;

import com.militarystore.port.in.product.ProductRateUseCase;
import lombok.RequiredArgsConstructor;
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

    //@TODO: User id should be replaced with user context after adding security
    @PostMapping("/{productId}")
    public void rateProduct(
        @PathVariable("productId") Integer productId,
        @RequestParam("userId") Integer userId,
        @RequestParam("productRate") double productRate
    ) {
        productRateUseCase.rateProduct(productId, userId, productRate);
    }

    @GetMapping("/{productId}")
    public double getAverageRateByProductId(@PathVariable("productId") Integer productId) {
        return productRateUseCase.getAverageRateByProductId(productId);
    }
}
