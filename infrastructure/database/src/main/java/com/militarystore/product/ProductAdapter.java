package com.militarystore.product;

import com.militarystore.entity.product.Product;
import com.militarystore.port.out.product.ProductPort;
import com.militarystore.product.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductAdapter implements ProductPort {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public Integer addProduct(Product product) {
        return productRepository.addProduct(product);
    }

    public Product getProductById(Integer productId) {
        var productRecord = productRepository.getProductById(productId);

        return productMapper.map(productRecord);
    }

    public List<Product> getProductsBySubcategoryId(Integer subcategoryId) {
        var productRecords = productRepository.getProductsBySubcategoryId(subcategoryId);

        return productRecords.stream()
            .map(productMapper::map)
            .toList();
    }

    public List<Product> getProductsByName(String name) {
        var productRecords = productRepository.getProductsByName(name);

        return productRecords.stream()
            .map(productMapper::map)
            .toList();
    }

    public void updateProduct(Product product) {
        productRepository.updateProduct(product);
    }

    public void updateStockAvailability(Integer productId, boolean isInStock) {
        productRepository.updateStockAvailability(productId, isInStock);
    }

    public void deleteProduct(Integer productId) {
        productRepository.deleteProduct(productId);
    }

    public boolean isProductExist(Integer productId) {
        return productRepository.isProductExist(productId);
    }
}
