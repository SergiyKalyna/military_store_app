package com.militarystore.product;

import com.militarystore.entity.product.Product;
import com.militarystore.entity.product.ProductStockDetails;
import com.militarystore.exception.MsValidationException;

import java.util.List;

import static java.util.Objects.isNull;

public class ProductValidator {

    private ProductValidator() {
    }

    static void validateProduct(Product product) {
        validateProductGeneralInfo(product);
        validateProductStockDetails(product.stockDetails());
    }

    private static void validateProductGeneralInfo(Product product) {
        if (isNull(product.name()) || product.name().isBlank()) {
            throw new MsValidationException("Product name should not be empty");
        }

        if(isNull(product.description()) || product.description().isBlank()) {
            throw new MsValidationException("Product description should not be empty");
        }

        if (isNull(product.price()) || product.price() < 0) {
            throw new MsValidationException("Product price should not be less than 0");
        }
    }

    private static void validateProductStockDetails(List<ProductStockDetails> stockDetails) {
        if (stockDetails.isEmpty()) {
            throw new MsValidationException("Product stock details should not be empty");
        }

        stockDetails.forEach(stockDetail -> {
            if (stockDetail.stockAvailability() < 0) {
                throw new MsValidationException("Product stock availability count should not be less than 0");
            }
        });
    }
}
