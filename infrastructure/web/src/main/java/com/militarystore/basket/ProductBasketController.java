package com.militarystore.basket;

import com.militarystore.converter.basket.ProductBasketConverter;
import com.militarystore.entity.user.User;
import com.militarystore.model.response.basket.ProductBasketResponse;
import com.militarystore.port.in.basket.ProductBasketUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products/basket")
public class ProductBasketController {

    private final ProductBasketUseCase productBasketUseCase;
    private final ProductBasketConverter productBasketConverter;

    @PostMapping("/product-details-id/{product-details-id}")
    public void addProductToBasket(
        @PathVariable("product-details-id") Integer productStockDetailsId,
        @RequestParam("quantity") Integer quantity,
        @AuthenticationPrincipal User user
    ) {
        productBasketUseCase.addProductToBasket(productStockDetailsId, user.id(), quantity);
    }

    @PutMapping("/product-details-id/{product-details-id}")
    public void updateProductQuantityInBasket(
        @PathVariable("product-details-id") Integer productStockDetailsId,
        @RequestParam("quantity") Integer quantity,
        @AuthenticationPrincipal User user
    ) {
        productBasketUseCase.updateProductQuantityInBasket(productStockDetailsId, user.id(), quantity);
    }

    @DeleteMapping("/product-details-id/{product-details-id}")
    public void deleteProductFromBasket(
        @PathVariable("product-details-id") Integer productStockDetailsId,
        @AuthenticationPrincipal User user
    ) {
        productBasketUseCase.deleteProductFromBasket(productStockDetailsId, user.id());
    }

    @DeleteMapping
    public void deleteUserProductsFromBasket(@AuthenticationPrincipal User user) {
        productBasketUseCase.deleteUserProductsFromBasket(user.id());
    }

    @GetMapping
    public ProductBasketResponse getUserBasketProducts(@AuthenticationPrincipal User user) {
       var basketProducts = productBasketUseCase.getUserBasketProducts(user.id());

       return productBasketConverter.convertToProductBasketResponse(basketProducts);
    }
}
