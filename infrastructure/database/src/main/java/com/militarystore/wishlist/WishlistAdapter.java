package com.militarystore.wishlist;

import com.militarystore.port.out.wishlist.WishlistPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WishlistAdapter implements WishlistPort {

    private final WishlistRepository wishlistRepository;

    @Override
    public void addProductToUserWishlist(Integer productId, Integer userId) {
        wishlistRepository.addProductToWishlist(productId, userId);
    }

    @Override
    public void deleteProductFromUserWishlist(Integer productId, Integer userId) {
        wishlistRepository.deleteProductFromWishlist(productId, userId);
    }

    @Override
    public void deleteAllUserProductsFromWishlist(Integer userId) {
        wishlistRepository.deleteAllUserProductsFromWishlist(userId);
    }

    @Override
    public void deleteProductFromWishlist(Integer productId) {
        wishlistRepository.deleteProductFromWishlist(productId);
    }

    @Override
    public List<Integer> getUserWishlistProductIds(Integer userId) {
        return wishlistRepository.getUserWishlistProductIds(userId);
    }

    @Override
    public boolean isProductInUserWishlist(Integer productId, Integer userId) {
        return wishlistRepository.isProductInUserWishlist(productId, userId);
    }
}
