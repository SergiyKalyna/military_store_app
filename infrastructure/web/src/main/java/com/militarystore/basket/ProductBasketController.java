package com.militarystore.basket;

import com.militarystore.converter.ProductBasketConverter;
import com.militarystore.model.response.ProductBasketResponse;
import com.militarystore.port.in.basket.ProductBasketUseCase;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/user/{userId}/product-details-id/{product-details-id}")
    public void addProductToBasket(
        @PathVariable("product-details-id") Integer productStockDetailsId,
        @PathVariable("userId") Integer userId,
        @RequestParam("quantity") Integer quantity
    ) {
        productBasketUseCase.addProductToBasket(productStockDetailsId, userId, quantity);
    }

    @PutMapping("/user/{userId}/product-details-id/{product-details-id}")
    public void updateProductQuantityInBasket(
        @PathVariable("product-details-id") Integer productStockDetailsId,
        @PathVariable("userId") Integer userId,
        @RequestParam("quantity") Integer quantity
    ) {
        productBasketUseCase.updateProductQuantityInBasket(productStockDetailsId, userId, quantity);
    }

    @DeleteMapping("/user/{userId}/product-details-id/{product-details-id}")
    public void deleteProductFromBasket(
        @PathVariable("product-details-id") Integer productStockDetailsId,
        @PathVariable("userId") Integer userId
    ) {
        productBasketUseCase.deleteProductFromBasket(productStockDetailsId, userId);
    }

    @DeleteMapping("/user/{userId}")
    public void deleteUserProductsFromBasket(@PathVariable("userId") Integer userId) {
        productBasketUseCase.deleteUserProductsFromBasket(userId);
    }

    @GetMapping("/user/{userId}")
    public ProductBasketResponse getUserBasketProducts(@PathVariable("userId") Integer userId) {
       var basketProducts = productBasketUseCase.getUserBasketProducts(userId);

       return productBasketConverter.convertToProductBasketResponse(basketProducts);
    }
}
