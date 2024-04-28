package com.militarystore.port.out.wishlist;

import java.util.List;

public interface WishlistPort {

    void addProductToUserWishlist(Integer productId, Integer userId);

    void deleteProductFromUserWishlist(Integer productId, Integer userId);

    void deleteAllUserProductsFromWishlist(Integer userId);

    void deleteProductFromWishlist(Integer productId);

    List<Integer> getUserWishlistProductIds(Integer userId);

    boolean isProductInUserWishlist(Integer productId, Integer userId);
}
