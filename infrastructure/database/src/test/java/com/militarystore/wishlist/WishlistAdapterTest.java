package com.militarystore.wishlist;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WishlistAdapterTest {

    private static final Integer PRODUCT_ID = 1;
    private static final Integer USER_ID = 11;

    @Mock
    private WishlistRepository wishlistRepository;

    private WishlistAdapter wishlistAdapter;

    @BeforeEach
    void setUp() {
        wishlistAdapter = new WishlistAdapter(wishlistRepository);
    }

    @Test
    void addProductToUserWishlist() {
        wishlistAdapter.addProductToUserWishlist(PRODUCT_ID, USER_ID);

        verify(wishlistRepository).addProductToWishlist(PRODUCT_ID, USER_ID);
    }

    @Test
    void deleteProductFromUserWishlist() {
        wishlistAdapter.deleteProductFromUserWishlist(PRODUCT_ID, USER_ID);

        verify(wishlistRepository).deleteProductFromWishlist(PRODUCT_ID, USER_ID);
    }

    @Test
    void deleteAllUserProductsFromWishlist() {
        wishlistAdapter.deleteAllUserProductsFromWishlist(USER_ID);

        verify(wishlistRepository).deleteAllUserProductsFromWishlist(USER_ID);
    }

    @Test
    void getUserWishlistProductIds() {
        when(wishlistRepository.getUserWishlistProductIds(USER_ID)).thenReturn(List.of(PRODUCT_ID));

        assertThat(wishlistAdapter.getUserWishlistProductIds(USER_ID)).isEqualTo(List.of(PRODUCT_ID));
    }

    @Test
    void isProductInUserWishlist() {
        when(wishlistRepository.isProductInUserWishlist(PRODUCT_ID, USER_ID)).thenReturn(true);

        assertTrue(wishlistAdapter.isProductInUserWishlist(PRODUCT_ID, USER_ID));
    }

    @Test
    void deleteProductFromWishlist() {
        wishlistAdapter.deleteProductFromWishlist(PRODUCT_ID);

        verify(wishlistRepository).deleteProductFromWishlist(PRODUCT_ID);
    }
}