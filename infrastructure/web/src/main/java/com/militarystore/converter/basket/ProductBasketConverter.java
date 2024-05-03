package com.militarystore.converter.basket;

import com.militarystore.entity.basket.ProductInBasket;
import com.militarystore.model.dto.basket.ProductInBasketDto;
import com.militarystore.model.response.ProductBasketResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductBasketConverter {

    public ProductBasketResponse convertToProductBasketResponse(List<ProductInBasket> productsInBasket) {
        var productsInBasketDto = convertToProductInBasketDto(productsInBasket);
        var totalAmount = calculateTotalAmount(productsInBasketDto);

        return new ProductBasketResponse(productsInBasketDto, totalAmount);
    }

    private List<ProductInBasketDto> convertToProductInBasketDto(List<ProductInBasket> productsInBasket) {
        return productsInBasket.stream()
            .map(this::convertToProductInBasketDto)
            .toList();
    }

    private ProductInBasketDto convertToProductInBasketDto(ProductInBasket productInBasket) {
        return ProductInBasketDto.builder()
            .productId(productInBasket.productId())
            .productName(productInBasket.productName())
            .productPrice(productInBasket.productPrice())
            .quantity(productInBasket.quantity())
            .totalAmount(productInBasket.productPrice() * productInBasket.quantity())
            .build();
    }

    private Integer calculateTotalAmount(List<ProductInBasketDto> productsInBasketDto) {
        return productsInBasketDto.stream()
            .mapToInt(ProductInBasketDto::totalAmount)
            .sum();
    }
}
