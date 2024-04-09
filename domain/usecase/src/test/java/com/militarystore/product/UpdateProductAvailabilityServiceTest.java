package com.militarystore.product;

import com.militarystore.port.out.product.ProductPort;
import com.militarystore.port.out.product.ProductStockDetailsPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateProductAvailabilityServiceTest {

    @Mock
    private ProductPort productPort;

    @Mock
    private ProductStockDetailsPort productStockDetailsPort;

    private UpdateProductAvailabilityService updateProductAvailabilityService;

    @BeforeEach
    void setUp() {
        updateProductAvailabilityService = new UpdateProductAvailabilityService(productPort, productStockDetailsPort);
    }

    @Test
    void updateProductStockAvailability() {
        var productId = 1;
        var productStockDetailsId = 1;
        var orderedProductQuantity = 10;

        when(productStockDetailsPort.isProductAvailable(productId)).thenReturn(true);

        updateProductAvailabilityService.updateProductStockAvailability(productId, productStockDetailsId, orderedProductQuantity);

        verify(productStockDetailsPort).updateProductStockAvailability(productStockDetailsId, orderedProductQuantity);
        verify(productPort).updateStockAvailability(productId, true);
    }
}