package com.militarystore.product;

import com.militarystore.port.out.product.ProductPort;
import com.militarystore.port.out.product.ProductStockDetailsPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductOrderServiceTest {

    @Mock
    private ProductPort productPort;

    @Mock
    private ProductStockDetailsPort productStockDetailsPort;

    private ProductOrderService productOrderService;

    @BeforeEach
    void setUp() {
        productOrderService = new ProductOrderService(productPort, productStockDetailsPort);
    }

    @Test
    void updateProductStockAvailability() {
        var productId = 1;
        var productStockDetailsId = 1;
        var orderedProductQuantity = 10;

        when(productStockDetailsPort.isProductAvailable(productId)).thenReturn(true);

        productOrderService.updateProductStockAvailability(productId, productStockDetailsId, orderedProductQuantity);

        verify(productStockDetailsPort).updateProductStockAvailability(productStockDetailsId, orderedProductQuantity);
        verify(productPort).updateStockAvailability(productId, true);
    }

    @Test
    void isEnoughProductStockAvailability() {
        var productStockDetailsId = 1;
        var orderedProductQuantity = 10;

        when(productStockDetailsPort.isEnoughProductStockAvailability(productStockDetailsId, orderedProductQuantity))
            .thenReturn(true);

        assertTrue(productOrderService.isEnoughProductStockAvailability(productStockDetailsId, orderedProductQuantity));
    }
}