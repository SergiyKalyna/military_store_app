package com.militarystore.wishlist;

import com.militarystore.converter.product.ProductConverter;
import com.militarystore.model.dto.product.ProductDto;
import com.militarystore.port.in.wishlist.UserWishlistUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("users/wishlist")
public class UserWishlistController {

    private final UserWishlistUseCase userWishlistUseCase;
    private final ProductConverter productConverter;

    @PostMapping("/product/{productId}/user/{userId}")
    public void addProductToUserWishlist(
        @PathVariable("productId") Integer productId,
        @PathVariable("userId") Integer userId
    ) {
        userWishlistUseCase.addProductToUserWishlist(productId, userId);
    }

    @DeleteMapping("/product/{productId}/user/{userId}")
    public void deleteProductFromUserWishlist(
        @PathVariable("productId") Integer productId,
        @PathVariable("userId") Integer userId
    ) {
        userWishlistUseCase.deleteProductFromUserWishlist(productId, userId);
    }

    @DeleteMapping("/user/{userId}")
    public void deleteAllUserProductsFromWishlist(@PathVariable("userId") Integer userId) {
        userWishlistUseCase.deleteAllUserProductsFromWishlist(userId);
    }

    @GetMapping("/user/{userId}")
    public List<ProductDto> getUserWishlistProducts(@PathVariable("userId") Integer userId) {
        var products = userWishlistUseCase.getUserWishlistProducts(userId);

        return products.stream()
            .map(productConverter::convertToSearchProductDto)
            .toList();
    }
}
