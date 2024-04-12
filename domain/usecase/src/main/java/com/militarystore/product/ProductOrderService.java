package com.militarystore.product;

import com.militarystore.port.in.product.ProductOrderUseCase;
import com.militarystore.port.out.product.ProductPort;
import com.militarystore.port.out.product.ProductStockDetailsPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductOrderService implements ProductOrderUseCase {

    private final ProductPort productPort;
    private final ProductStockDetailsPort productStockDetailsPort;

    @Override
    public void updateProductStockAvailability(Integer productId, Integer productStockDetailsId, Integer orderedProductQuantity) {
        productStockDetailsPort.updateProductStockAvailability(productStockDetailsId, orderedProductQuantity);

        var isInStock = productStockDetailsPort.isProductAvailable(productId);
        productPort.updateStockAvailability(productId, isInStock);
    }

    @Override
    public boolean isEnoughProductStockAvailability(Integer productStockDetailsId, Integer orderedProductQuantity) {
        return productStockDetailsPort.isEnoughProductStockAvailability(productStockDetailsId, orderedProductQuantity);
    }
}
