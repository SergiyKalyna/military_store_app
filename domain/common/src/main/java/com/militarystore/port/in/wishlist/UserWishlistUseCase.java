package com.militarystore.port.in.wishlist;

import com.militarystore.entity.product.Product;

import java.util.List;

public interface UserWishlistUseCase {

    void addProductToUserWishlist(Integer productId, Integer userId);

    void deleteProductFromUserWishlist(Integer productId, Integer userId);

    void deleteAllUserProductsFromWishlist(Integer userId);

    List<Product> getUserWishlistProducts(Integer userId);
}
