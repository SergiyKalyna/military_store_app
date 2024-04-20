package com.militarystore.product;

import com.militarystore.exception.MsNotFoundException;
import com.militarystore.port.out.product.ProductPort;
import com.militarystore.port.out.product.ProductRatePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductRateServiceTest {

    private static final Integer PRODUCT_ID = 1;

    @Mock
    private ProductRatePort productRatePort;

    @Mock
    private ProductPort productPort;

    private ProductRateService productRateService;

    @BeforeEach
    void setUp() {
        productRateService = new ProductRateService(productRatePort, productPort);
    }

    @Test
    void rateProduct_whenProductDoesNotExist_shouldThrowException() {
        var userId = 2;
        var productRate = 4.5;

        when(productPort.isProductExist(PRODUCT_ID)).thenReturn(false);

        assertThrows(MsNotFoundException.class, () -> productRateService.rateProduct(PRODUCT_ID, userId, productRate));
    }

    @Test
    void rateProduct() {
        var userId = 2;
        var productRate = 4.5;

        when(productPort.isProductExist(PRODUCT_ID)).thenReturn(true);

        productRateService.rateProduct(PRODUCT_ID, userId, productRate);

        verify(productRatePort).saveOrUpdateRate(userId, PRODUCT_ID, productRate);
    }

    @Test
    void getAverageRateByProductId_whenProductDoesNotExist_shouldThrowException() {
        when(productPort.isProductExist(PRODUCT_ID)).thenReturn(false);

        assertThrows(MsNotFoundException.class, () -> productRateService.getAverageRateByProductId(PRODUCT_ID));
    }

    @Test
    void getAverageRateByProductId() {
        var averageRate = 4.5;

        when(productPort.isProductExist(PRODUCT_ID)).thenReturn(true);
        when(productRatePort.getAverageRateByProductId(PRODUCT_ID)).thenReturn(averageRate);

        assertThat(productRateService.getAverageRateByProductId(PRODUCT_ID)).isEqualTo(averageRate);
    }
}