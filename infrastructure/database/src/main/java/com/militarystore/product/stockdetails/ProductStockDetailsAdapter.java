package com.militarystore.product.stockdetails;

import com.militarystore.entity.product.ProductStockDetails;
import com.militarystore.port.out.product.ProductStockDetailsPort;
import com.militarystore.product.mapper.ProductStockDetailsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductStockDetailsAdapter implements ProductStockDetailsPort {

    private final ProductStockDetailsRepository productStockDetailsRepository;
    private final ProductStockDetailsMapper productStockDetailsMapper;

    @Override
    public void addProductStockDetails(Integer productId, List<ProductStockDetails> productStockDetails) {
        var productDetailsRecord = productStockDetails.stream()
            .map(productDetails -> productStockDetailsMapper.toRecord(productId, productDetails))
            .toList();

        productStockDetailsRepository.addProductStockDetails(productDetailsRecord);
    }

    @Override
    public void updateProductStockDetails(List<ProductStockDetails> productStockDetails) {
        var productDetailsRecord = productStockDetails.stream()
            .map(productStockDetailsMapper::toRecord)
            .toList();

        productStockDetailsRepository.updateProductStockDetails(productDetailsRecord);
    }

    @Override
    public void updateProductStockAvailability(Integer productStockDetailsId, Integer orderedProductQuantity) {
        productStockDetailsRepository.updateProductStockAvailability(productStockDetailsId, orderedProductQuantity);
    }

    @Override
    public boolean isProductAvailable(Integer productId) {
        return productStockDetailsRepository.isProductAvailable(productId);
    }

    @Override
    public void deleteProductStockDetails(Integer productId) {
        productStockDetailsRepository.deleteProductStockDetails(productId);
    }

    @Override
    public boolean isEnoughProductStockAvailability(Integer productStockDetailsId, Integer orderedProductQuantity) {
        return productStockDetailsRepository.isEnoughProductStockAvailability(productStockDetailsId, orderedProductQuantity);
    }
}
