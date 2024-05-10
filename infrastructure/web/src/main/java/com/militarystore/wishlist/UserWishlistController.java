package com.militarystore.wishlist;

import com.militarystore.converter.product.ProductConverter;
import com.militarystore.entity.user.User;
import com.militarystore.model.dto.product.ProductDto;
import com.militarystore.port.in.wishlist.UserWishlistUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @PostMapping("/product/{productId}")
    public void addProductToUserWishlist(
        @PathVariable("productId") Integer productId,
        @AuthenticationPrincipal User user
    ) {
        userWishlistUseCase.addProductToUserWishlist(productId, user.id());
    }

    @DeleteMapping("/product/{productId}")
    public void deleteProductFromUserWishlist(
        @PathVariable("productId") Integer productId,
        @AuthenticationPrincipal User user
    ) {
        userWishlistUseCase.deleteProductFromUserWishlist(productId, user.id());
    }

    @DeleteMapping
    public void deleteAllUserProductsFromWishlist(@AuthenticationPrincipal User user) {
        userWishlistUseCase.deleteAllUserProductsFromWishlist(user.id());
    }

    @GetMapping
    public List<ProductDto> getUserWishlistProducts(@AuthenticationPrincipal User user) {
        var products = userWishlistUseCase.getUserWishlistProducts(user.id());

        return products.stream()
            .map(productConverter::convertToSearchProductDto)
            .toList();
    }
}
