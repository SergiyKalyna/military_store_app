package com.militarystore.wishlist;

import com.militarystore.entity.product.Product;
import com.militarystore.exception.MsNotFoundException;
import com.militarystore.port.out.product.ProductPort;
import com.militarystore.port.out.wishlist.WishlistPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserWishlistServiceTest {

    private static final Integer PRODUCT_ID = 1;
    private static final Integer USER_ID = 11;

    @Mock
    private WishlistPort wishlistPort;

    @Mock
    private ProductPort productPort;

    private UserWishlistService userWishlistService;

    @BeforeEach
    void setUp() {
        userWishlistService = new UserWishlistService(wishlistPort, productPort);
    }

    @Test
    void addProductToUserWishlist() {
        when(productPort.isProductExist(PRODUCT_ID)).thenReturn(true);
        when(wishlistPort.isProductInUserWishlist(PRODUCT_ID, USER_ID)).thenReturn(false);

        userWishlistService.addProductToUserWishlist(PRODUCT_ID, USER_ID);

         verify(wishlistPort).addProductToUserWishlist(PRODUCT_ID, USER_ID);
    }

    @Test
    void addProductToUserWishlist_whenProductNotExist_shouldThrowException() {
        when(productPort.isProductExist(PRODUCT_ID)).thenReturn(false);

        assertThrows(MsNotFoundException.class, () -> userWishlistService.addProductToUserWishlist(PRODUCT_ID, USER_ID));
    }

    @Test
    void addProductToUserWishlist_whenProductAlreadyInWishlist_shouldNotAddProductToWishlist() {
        when(productPort.isProductExist(PRODUCT_ID)).thenReturn(true);
        when(wishlistPort.isProductInUserWishlist(PRODUCT_ID, USER_ID)).thenReturn(true);

        userWishlistService.addProductToUserWishlist(PRODUCT_ID, USER_ID);

        verify(wishlistPort, times(0)).addProductToUserWishlist(PRODUCT_ID, USER_ID);
    }

    @Test
    void deleteProductFromUserWishlist() {
        when(wishlistPort.isProductInUserWishlist(PRODUCT_ID, USER_ID)).thenReturn(true);

        userWishlistService.deleteProductFromUserWishlist(PRODUCT_ID, USER_ID);

        verify(wishlistPort).deleteProductFromUserWishlist(PRODUCT_ID, USER_ID);
    }

    @Test
    void deleteProductFromUserWishlist_whenProductNotInWishlist_shouldThrowException() {
        when(wishlistPort.isProductInUserWishlist(PRODUCT_ID, USER_ID)).thenReturn(false);

       assertThrows(MsNotFoundException.class, () -> userWishlistService.deleteProductFromUserWishlist(PRODUCT_ID, USER_ID));
    }

    @Test
    void deleteAllUserProductsFromWishlist() {
        userWishlistService.deleteAllUserProductsFromWishlist(USER_ID);

        verify(wishlistPort).deleteAllUserProductsFromWishlist(USER_ID);
    }

    @Test
    void getUserWishlistProducts() {
        var products = List.of(Product.builder().build());

        when(wishlistPort.getUserWishlistProductIds(USER_ID)).thenReturn(List.of(PRODUCT_ID));
        when(productPort.getProductsByIds(List.of(PRODUCT_ID))).thenReturn(products);

        assertThat(userWishlistService.getUserWishlistProducts(USER_ID)).isEqualTo(products);
    }
}