package com.militarystore.product;

import com.militarystore.entity.product.Product;
import com.militarystore.jooq.tables.records.ProductStockDetailsRecord;
import com.militarystore.jooq.tables.records.ProductsRecord;
import com.militarystore.product.feedback.ProductFeedbackRepository;
import com.militarystore.product.mapper.ProductMapper;
import com.militarystore.product.rate.ProductRateRepository;
import com.militarystore.product.stockdetails.ProductStockDetailsRepository;
import org.jooq.Record5;
import org.jooq.Record6;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductAdapterTest {

    private static final int PRODUCT_ID = 1;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductFeedbackRepository productFeedbackRepository;

    @Mock
    private ProductStockDetailsRepository productStockDetailsRepository;

    @Mock
    private ProductRateRepository productRateRepository;

    @Mock
    private ProductMapper productMapper;

    private ProductAdapter productAdapter;

    @BeforeEach
    void setUp() {
        productAdapter = new ProductAdapter(
            productRepository,
            productStockDetailsRepository,
            productRateRepository,
            productFeedbackRepository,
            productMapper
        );
    }

    @Test
    void addProduct() {
        var product = Product.builder().build();

        when(productRepository.addProduct(product)).thenReturn(PRODUCT_ID);

        assertThat(productAdapter.addProduct(product)).isEqualTo(PRODUCT_ID);
    }

    @Test
    void updateProduct() {
        var product = Product.builder().build();

        productAdapter.updateProduct(product);

        verify(productRepository).updateProduct(product);
    }

    @Test
    void updateStockAvailability() {
        productAdapter.updateStockAvailability(PRODUCT_ID, true);

        verify(productRepository).updateStockAvailability(PRODUCT_ID, true);
    }

    @Test
    void deleteProduct() {
        productAdapter.deleteProduct(PRODUCT_ID);

        verify(productRepository).deleteProduct(PRODUCT_ID);
    }

    @Test
    void isProductExists() {
        when(productRepository.isProductExist(PRODUCT_ID)).thenReturn(true);

        assertThat(productAdapter.isProductExist(PRODUCT_ID)).isTrue();
    }

    @Test
    void getProductById() {
        var productRecord = new ProductsRecord();
        var productStockDetailsRecord = List.of(new ProductStockDetailsRecord());
        var avgRate = 0.0;
        var productFeedbacksRecord = mock(Record5.class);
        var product = Product.builder().build();

        when(productRepository.getProductById(PRODUCT_ID)).thenReturn(productRecord);
        when(productStockDetailsRepository.getProductStockDetailsByProductId(PRODUCT_ID)).thenReturn(productStockDetailsRecord);
        when(productRateRepository.getAverageRateByProductId(PRODUCT_ID)).thenReturn(avgRate);
        when(productFeedbackRepository.getFeedbacksByProductId(PRODUCT_ID)).thenReturn(List.of(productFeedbacksRecord));
        when(productMapper.map(productRecord, productStockDetailsRecord, avgRate, List.of(productFeedbacksRecord))).thenReturn(product);

        assertThat(productAdapter.getProductById(PRODUCT_ID)).isEqualTo(product);
    }

    @Test
    void getProductsBySubcategoryId() {
        var productRecord = mock(Record6.class);
        var product = Product.builder().build();

        when(productRepository.getProductsBySubcategoryId(PRODUCT_ID)).thenReturn(List.of(productRecord));
        when(productMapper.map(isA(Record6.class))).thenReturn(product);

        assertThat(productAdapter.getProductsBySubcategoryId(PRODUCT_ID)).isEqualTo(List.of(product));
    }

    @Test
    void getProductsByName() {
        var productRecord = mock(Record6.class);
        var product = Product.builder().build();

        when(productRepository.getProductsByName("name")).thenReturn(List.of(productRecord));
        when(productMapper.map(isA(Record6.class))).thenReturn(product);

        assertThat(productAdapter.getProductsByName("name")).isEqualTo(List.of(product));
    }
}