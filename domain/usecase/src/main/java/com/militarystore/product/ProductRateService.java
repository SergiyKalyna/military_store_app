package com.militarystore.product;

import com.militarystore.exception.MsNotFoundException;
import com.militarystore.port.in.product.ProductRateUseCase;
import com.militarystore.port.out.product.ProductPort;
import com.militarystore.port.out.product.ProductRatePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductRateService implements ProductRateUseCase {

    private final ProductRatePort productRatePort;
    private final ProductPort productPort;

    @Override
    public void rateProduct(Integer productId, Integer userId, double productRate) {
        checkIfProductExist(productId);
        productRatePort.saveOrUpdateRate(userId, productId, productRate);

        log.info("User with id '{}' rated product with id '{}' with rate '{}'", userId, productId, productRate);
    }

    @Override
    public double getAverageRateByProductId(Integer productId) {
        checkIfProductExist(productId);
        return productRatePort.getAverageRateByProductId(productId);
    }

    private void checkIfProductExist(Integer productId) {
        if (!productPort.isProductExist(productId)) {
            throw new MsNotFoundException("Product with id " + productId + " does not exist");
        }
    }
}
