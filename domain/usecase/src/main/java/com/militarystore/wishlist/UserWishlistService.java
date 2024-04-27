package com.militarystore.wishlist;

import com.militarystore.entity.product.Product;
import com.militarystore.exception.MsNotFoundException;
import com.militarystore.port.in.wishlist.UserWishlistUseCase;
import com.militarystore.port.out.product.ProductPort;
import com.militarystore.port.out.wishlist.WishlistPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserWishlistService implements UserWishlistUseCase {

    private final WishlistPort wishlistPort;
    private final ProductPort productPort;

    @Override
    public void addProductToUserWishlist(Integer productId, Integer userId) {
        validateProductExisting(productId);

        var isProductInUserWishlist = wishlistPort.isProductInUserWishlist(productId, userId);

        if (!isProductInUserWishlist) {
            wishlistPort.addProductToUserWishlist(productId, userId);
            log.info("Product with id {} added to wishlist for user with id {}", productId, userId);
        }
    }

    @Override
    public void deleteProductFromUserWishlist(Integer productId, Integer userId) {
        validateIfProductInUserWishlist(productId, userId);

        wishlistPort.deleteProductFromUserWishlist(productId, userId);
        log.info("Product with id {} deleted from wishlist for user with id {}", productId, userId);
    }

    @Override
    public void deleteAllUserProductsFromWishlist(Integer userId) {
        wishlistPort.deleteAllUserProductsFromWishlist(userId);
        log.info("All products deleted from wishlist for user with id {}", userId);
    }

    @Override
    public List<Product> getUserWishlistProducts(Integer userId) {
        var productIds = wishlistPort.getUserWishlistProductIds(userId);

        return productPort.getProductsByIds(productIds);
    }

    private void validateProductExisting(Integer productId) {
        if (!productPort.isProductExist(productId)) {
            throw new MsNotFoundException("Product with id " + productId + " not found");
        }
    }

    private void validateIfProductInUserWishlist(Integer productId, Integer userId) {
        if (!wishlistPort.isProductInUserWishlist(productId, userId)) {
            throw new MsNotFoundException("Product with id " + productId + " not found in wishlist for user with id " + userId);
        }
    }
}
