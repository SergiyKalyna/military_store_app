package com.militarystore.product;

import com.militarystore.entity.product.Product;
import com.militarystore.jooq.tables.records.ProductsRecord;
import com.militarystore.product.mapper.ProductMapper;
import org.jooq.Record;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductAdapterTest {

    private static final int PRODUCT_ID = 1;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    private ProductAdapter productAdapter;

    @BeforeEach
    void setUp() {
        productAdapter = new ProductAdapter(productRepository, productMapper);
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
        var product = Product.builder().build();

        when(productRepository.getProductById(PRODUCT_ID)).thenReturn(List.of(productRecord));
        when(productMapper.map(anyList())).thenReturn(product);

        assertThat(productAdapter.getProductById(PRODUCT_ID)).isEqualTo(product);
    }

    @Test
    void getProductsBySubcategoryId() {
        List<Record> productRecords = List.of(new ProductsRecord());
        var product = Product.builder().build();

        when(productRepository.getProductsBySubcategoryId(PRODUCT_ID)).thenReturn(productRecords);
        when(productMapper.map(isA(ProductsRecord.class))).thenReturn(product);

        assertThat(productAdapter.getProductsBySubcategoryId(PRODUCT_ID)).isEqualTo(List.of(product));
    }

    @Test
    void getProductsByName() {
        List<Record> productRecords = List.of(new ProductsRecord());
        var product = Product.builder().build();

        when(productRepository.getProductsByName("name")).thenReturn(productRecords);
        when(productMapper.map(isA(ProductsRecord.class))).thenReturn(product);

        assertThat(productAdapter.getProductsByName("name")).isEqualTo(List.of(product));
    }
}