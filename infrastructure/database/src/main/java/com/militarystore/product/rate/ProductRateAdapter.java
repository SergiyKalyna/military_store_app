package com.militarystore.product.rate;

import com.militarystore.port.out.product.ProductRatePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductRateAdapter implements ProductRatePort {

    private final ProductRateRepository productRateRepository;

    @Override
    public void saveOrUpdateRate(Integer userId, Integer productId, double productRate) {
        productRateRepository.saveOrUpdateRate(userId, productId, productRate);
    }

    @Override
    public double getAverageRateByProductId(Integer productId) {
        return productRateRepository.getAverageRateByProductId(productId);
    }

    @Override
    public void deleteRate(Integer productId) {
        productRateRepository.deleteRate(productId);
    }
}
