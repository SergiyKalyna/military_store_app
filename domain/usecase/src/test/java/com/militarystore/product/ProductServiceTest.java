package com.militarystore.product;

import com.militarystore.entity.product.Product;
import com.militarystore.entity.product.ProductFeedback;
import com.militarystore.entity.product.ProductStockDetails;
import com.militarystore.entity.product.model.ProductSize;
import com.militarystore.exception.MsNotFoundException;
import com.militarystore.port.out.basket.BasketPort;
import com.militarystore.port.out.product.ProductFeedbackPort;
import com.militarystore.port.out.product.ProductPort;
import com.militarystore.port.out.product.ProductRatePort;
import com.militarystore.port.out.product.ProductStockDetailsPort;
import com.militarystore.port.out.wishlist.WishlistPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    private static final int PRODUCT_ID = 1;
    private static final int USER_ID = 1;

    @Mock
    private ProductPort productPort;

    @Mock
    private ProductStockDetailsPort productStockDetailsPort;

    @Mock
    private ProductFeedbackPort productFeedbackPort;

    @Mock
    private ProductRatePort productRatePort;

    @Mock
    private WishlistPort wishlistPort;

    @Mock
    private BasketPort basketPort;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(
            productPort,
            productStockDetailsPort,
            productRatePort,
            productFeedbackPort,
            wishlistPort,
            basketPort
        );
    }

    @Test
    void addProduct_shouldAddProduct() {
        var stockDetails = List.of(
            ProductStockDetails.builder()
                .productSize(ProductSize.M)
                .stockAvailability(10)
                .build()
        );

        var product = Product.builder()
            .name("product")
            .description("description")
            .price(100)
            .subcategoryId(1)
            .stockDetails(stockDetails)
            .build();

        when(productPort.addProduct(product)).thenReturn(PRODUCT_ID);

        assertThat(productService.addProduct(product)).isEqualTo(PRODUCT_ID);
        verify(productStockDetailsPort).addProductStockDetails(PRODUCT_ID, stockDetails);
    }

    @Test
    void getProductById_shouldReturnProduct() {
        var product = Product.builder();
        var productStockDetails = List.of(ProductStockDetails.builder().build());
        var avgRate = 0.0;
        var productFeedbacks = List.of(ProductFeedback.builder().build());
        var isProductInUserWishlist = true;

        when(productPort.isProductExist(PRODUCT_ID)).thenReturn(true);
        when(productPort.getProductById(PRODUCT_ID)).thenReturn(product);
        when(productStockDetailsPort.getProductStockDetailsByProductId(PRODUCT_ID)).thenReturn(productStockDetails);
        when(productRatePort.getAverageRateByProductId(PRODUCT_ID)).thenReturn(avgRate);
        when(productFeedbackPort.getFeedbacksByProductId(PRODUCT_ID)).thenReturn(productFeedbacks);
        when(wishlistPort.isProductInUserWishlist(PRODUCT_ID, USER_ID)).thenReturn(isProductInUserWishlist);

        var expectedProduct = product
            .stockDetails(productStockDetails)
            .avgRate(avgRate)
            .feedbacks(productFeedbacks)
            .isProductInUserWishlist(isProductInUserWishlist)
            .build();

        assertThat(productService.getProductById(PRODUCT_ID, USER_ID)).isEqualTo(expectedProduct);
    }

    @Test
    void getProductById_shouldThrowException_whenProductNotExist() {
        when(productPort.isProductExist(PRODUCT_ID)).thenReturn(false);

        assertThatThrownBy(() -> productService.getProductById(PRODUCT_ID, USER_ID))
            .isInstanceOf(MsNotFoundException.class)
            .hasMessage("Product does not exist with id: 1");
    }

    @Test
    void getProductsBySubcategoryId_shouldReturnProducts() {
        var products = List.of(Product.builder().build());

        when(productPort.getProductsBySubcategoryId(1)).thenReturn(products);

        assertThat(productService.getProductsBySubcategoryId(1)).isEqualTo(products);
    }

    @Test
    void getProductsByName_shouldReturnProducts() {
        var products = List.of(Product.builder().build());

        when(productPort.getProductsByName("product")).thenReturn(products);

        assertThat(productService.getProductsByName("product")).isEqualTo(products);
    }

    @Test
    void deleteProduct_shouldDeleteProduct() {
        when(productPort.isProductExist(PRODUCT_ID)).thenReturn(true);

        productService.deleteProduct(PRODUCT_ID);

        verify(basketPort).deleteProductFromAllBaskets(PRODUCT_ID);
        verify(productStockDetailsPort).deleteProductStockDetails(PRODUCT_ID);
        verify(wishlistPort).deleteProductFromWishlist(PRODUCT_ID);
        verify(productRatePort).deleteRate(PRODUCT_ID);
        verify(productFeedbackPort).deleteFeedbacksByProductId(PRODUCT_ID);
        verify(productPort).deleteProduct(PRODUCT_ID);
    }

    @Test
    void updateProduct() {
        var stockDetails = List.of(
            ProductStockDetails.builder()
                .productSize(ProductSize.M)
                .stockAvailability(10)
                .build()
        );

        var product = Product.builder()
            .id(PRODUCT_ID)
            .name("product")
            .description("description")
            .price(100)
            .subcategoryId(1)
            .stockDetails(stockDetails)
            .build();

        when(productPort.isProductExist(PRODUCT_ID)).thenReturn(true);

        productService.updateProduct(product);

        verify(productPort).updateProduct(product);
        verify(productStockDetailsPort).updateProductStockDetails(stockDetails);
    }
}