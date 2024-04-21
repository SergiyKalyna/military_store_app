package com.militarystore.product.stockdetails;

import com.militarystore.entity.product.ProductStockDetails;
import com.militarystore.jooq.tables.records.ProductStockDetailsRecord;
import com.militarystore.product.mapper.ProductStockDetailsMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductStockDetailsAdapterTest {

    private static final int PRODUCT_ID = 1;

    @Mock
    private ProductStockDetailsRepository productStockDetailsRepository;

    @Mock
    private ProductStockDetailsMapper productStockDetailsMapper;

    private ProductStockDetailsAdapter productStockDetailsAdapter;

    @BeforeEach
    void setUp() {
        productStockDetailsAdapter = new ProductStockDetailsAdapter(productStockDetailsRepository, productStockDetailsMapper);
    }

    @Test
    void addProductStockDetails() {
        var productStockDetails = ProductStockDetails.builder().build();
        var productStockDetailsRecord = new ProductStockDetailsRecord();

        when(productStockDetailsMapper.toRecord(PRODUCT_ID, productStockDetails)).thenReturn(productStockDetailsRecord);

        productStockDetailsAdapter.addProductStockDetails(PRODUCT_ID, List.of(productStockDetails));

        verify(productStockDetailsRepository).addProductStockDetails(List.of(productStockDetailsRecord));
    }

    @Test
    void updateProductStockDetails() {
        var productStockDetails = ProductStockDetails.builder().build();
        var productStockDetailsRecord = new ProductStockDetailsRecord();

        when(productStockDetailsMapper.toRecord(productStockDetails)).thenReturn(productStockDetailsRecord);

        productStockDetailsAdapter.updateProductStockDetails(List.of(productStockDetails));

        verify(productStockDetailsRepository).updateProductStockDetails(List.of(productStockDetailsRecord));
    }

    @Test
    void updateProductStockAvailability() {
        var productStockDetailsId = 11;
        var orderedProductQuantity = 100;

        productStockDetailsAdapter.updateProductStockAvailability(productStockDetailsId, orderedProductQuantity);

        verify(productStockDetailsRepository).updateProductStockAvailability(productStockDetailsId, orderedProductQuantity);
    }

    @Test
    void deleteProductStockDetails() {
        productStockDetailsAdapter.deleteProductStockDetails(PRODUCT_ID);

        verify(productStockDetailsRepository).deleteProductStockDetails(PRODUCT_ID);
    }

    @Test
    void isProductAvailable() {
        when(productStockDetailsRepository.isProductAvailable(PRODUCT_ID)).thenReturn(true);

        assertTrue(productStockDetailsAdapter.isProductAvailable(PRODUCT_ID));
    }

    @Test
    void isEnoughProductStockAvailability() {
        var productStockDetailsId = 11;
        var orderedProductQuantity = 100;

        when(productStockDetailsRepository.isEnoughProductStockAvailability(productStockDetailsId, orderedProductQuantity))
            .thenReturn(true);

        assertTrue(productStockDetailsAdapter.isEnoughProductStockAvailability(productStockDetailsId, orderedProductQuantity));
    }
}