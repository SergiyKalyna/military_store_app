package com.militarystore.wishlist;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.militarystore.jooq.Tables.WISHLISTS;

@Repository
@RequiredArgsConstructor
public class WishlistRepository {

    private final DSLContext dslContext;

    public void addProductToWishlist(Integer productId, Integer userId) {
        dslContext.insertInto(WISHLISTS)
                .set(WISHLISTS.PRODUCT_ID, productId)
                .set(WISHLISTS.USER_ID, userId)
                .execute();
    }

    public void deleteProductFromWishlist(Integer productId, Integer userId) {
        dslContext.deleteFrom(WISHLISTS)
                .where(WISHLISTS.PRODUCT_ID.eq(productId))
                .and(WISHLISTS.USER_ID.eq(userId))
                .execute();
    }

    public void deleteAllUserProductsFromWishlist(Integer userId) {
        dslContext.deleteFrom(WISHLISTS)
                .where(WISHLISTS.USER_ID.eq(userId))
                .execute();
    }

    public List<Integer> getUserWishlistProductIds(Integer userId) {
        return dslContext.select(WISHLISTS.PRODUCT_ID)
                .from(WISHLISTS)
                .where(WISHLISTS.USER_ID.eq(userId))
                .fetch(WISHLISTS.PRODUCT_ID);
    }

    public boolean isProductInUserWishlist(Integer productId, Integer userId) {
        return dslContext.fetchExists(
                dslContext.selectFrom(WISHLISTS)
                        .where(WISHLISTS.PRODUCT_ID.eq(productId))
                        .and(WISHLISTS.USER_ID.eq(userId))
        );
    }
}
