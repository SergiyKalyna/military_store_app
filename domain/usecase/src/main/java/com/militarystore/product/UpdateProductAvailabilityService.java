package com.militarystore.product;

import com.militarystore.port.in.product.UpdateProductAvailabilityUseCase;
import com.militarystore.port.out.product.ProductPort;
import com.militarystore.port.out.product.ProductStockDetailsPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateProductAvailabilityService implements UpdateProductAvailabilityUseCase {

    private final ProductPort productPort;
    private final ProductStockDetailsPort productStockDetailsPort;

    @Override
    public void updateProductStockAvailability(Integer productId, Integer productStockDetailsId, Integer orderedProductQuantity) {
        productStockDetailsPort.updateProductStockAvailability(productStockDetailsId, orderedProductQuantity);

        var isInStock = productStockDetailsPort.isProductAvailable(productId);
        productPort.updateStockAvailability(productId, isInStock);
    }
}
