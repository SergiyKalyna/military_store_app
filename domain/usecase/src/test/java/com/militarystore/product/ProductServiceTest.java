package com.militarystore.product;

import com.militarystore.entity.product.Product;
import com.militarystore.entity.product.ProductStockDetails;
import com.militarystore.entity.product.model.ProductSize;
import com.militarystore.exception.MsNotFoundException;
import com.militarystore.port.out.product.ProductFeedbackPort;
import com.militarystore.port.out.product.ProductPort;
import com.militarystore.port.out.product.ProductRatePort;
import com.militarystore.port.out.product.ProductStockDetailsPort;
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

    @Mock
    private ProductPort productPort;

    @Mock
    private ProductStockDetailsPort productStockDetailsPort;

    @Mock
    private ProductFeedbackPort productFeedbackPort;

    @Mock
    private ProductRatePort productRatePort;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productPort, productStockDetailsPort, productRatePort, productFeedbackPort);
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
        var product = Product.builder().build();

        when(productPort.isProductExist(PRODUCT_ID)).thenReturn(true);
        when(productPort.getProductById(PRODUCT_ID)).thenReturn(product);

        assertThat(productService.getProductById(PRODUCT_ID)).isEqualTo(product);
    }

    @Test
    void getProductById_shouldThrowException_whenProductNotExist() {
        when(productPort.isProductExist(PRODUCT_ID)).thenReturn(false);

        assertThatThrownBy(() -> productService.getProductById(PRODUCT_ID))
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

        verify(productStockDetailsPort).deleteProductStockDetails(PRODUCT_ID);
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