package com.militarystore.product.rate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductRateAdapterTest {

    @Mock
    private ProductRateRepository productRateRepository;

    private ProductRateAdapter productRateAdapter;

    @BeforeEach
    void setUp() {
        productRateAdapter = new ProductRateAdapter(productRateRepository);
    }

    @Test
    void saveOrUpdateRate() {
        var productId = 1;
        var userId = 3;
        var rate = 4.5;

        productRateAdapter.saveOrUpdateRate(userId, productId, rate);

        verify(productRateRepository).saveOrUpdateRate(userId, productId, rate);
    }

    @Test
    void getAverageRateByProductId() {
        var productId = 1;

        when(productRateRepository.getAverageRateByProductId(productId)).thenReturn(4.5);

        assertThat(productRateAdapter.getAverageRateByProductId(productId)).isEqualTo(4.5);
    }

    @Test
    void deleteRate() {
        var productId = 1;

        productRateAdapter.deleteRate(productId);

        verify(productRateRepository).deleteRate(productId);
    }
}