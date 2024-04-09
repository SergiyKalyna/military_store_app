package com.militarystore.product;

import com.militarystore.entity.product.Product;
import com.militarystore.exception.MsNotFoundException;
import com.militarystore.port.in.product.ProductUseCase;
import com.militarystore.port.out.product.ProductPort;
import com.militarystore.port.out.product.ProductStockDetailsPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.militarystore.product.ProductValidator.validateProduct;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService implements ProductUseCase {

    private final ProductPort productPort;
    private final ProductStockDetailsPort productStockDetailsPort;

    @Override
    public Integer addProduct(Product product) {
        validateProduct(product);

        var productId = productPort.addProduct(product);
        productStockDetailsPort.addProductStockDetails(productId, product.stockDetails());

        log.info("New product was added with id - {}", productId);
        return productId;
    }

    @Override
    public Product getProductById(Integer productId) {
        checkProductExisting(productId);

        return productPort.getProductById(productId);
    }

    @Override
    public List<Product> getProductsBySubcategoryId(Integer subcategoryId) {
        return productPort.getProductsBySubcategoryId(subcategoryId);
    }

    @Override
    public List<Product> getProductsByName(String productName) {
        return productPort.getProductsByName(productName);
    }

    @Override
    public void deleteProduct(Integer productId) {
        checkProductExisting(productId);

        productStockDetailsPort.deleteProductStockDetails(productId);
        productPort.deleteProduct(productId);

        log.info("Product with id - '{}' was deleted ", productId);
    }

    @Override
    public void updateProduct(Product product) {
        checkProductExisting(product.id());
        validateProduct(product);

        productPort.updateProduct(product);
        productStockDetailsPort.updateProductStockDetails(product.stockDetails());

        log.info("Product with id - '{}' was updated", product.id());
    }

    private void checkProductExisting(Integer productId) {
        if (!productPort.isProductExist(productId)) {
            throw new MsNotFoundException("Product does not exist with id: " + productId);
        }
    }
}
