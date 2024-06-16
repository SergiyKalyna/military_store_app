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

    @Override
    public Integer addProduct(Product product) {
        return productRepository.addProduct(product);
    }

    @Override
    public Product.ProductBuilder getProductById(Integer productId) {
        var productRecord = productRepository.getProductById(productId);

        return productMapper.map(productRecord);
    }

    @Override
    public List<Product> getProductsBySubcategoryId(Integer subcategoryId) {
        var productRecords = productRepository.getProductsBySubcategoryId(subcategoryId);

        return productRecords.stream()
            .map(productMapper::map)
            .toList();
    }

    @Override
    public List<Product> getProductsByName(String name) {
        var productRecords = productRepository.getProductsByName(name);

        return productRecords.stream()
            .map(productMapper::map)
            .toList();
    }

    @Override
    public List<Product> getProductsByIds(List<Integer> productIds) {
        var productRecords = productRepository.getProductsByIds(productIds);

        return productRecords.stream()
            .map(productMapper::map)
            .toList();
    }

    @Override
    public void updateProduct(Product product) {
        productRepository.updateProduct(product);
    }

    @Override
    public void updateStockAvailability(Integer productId, boolean isInStock) {
        productRepository.updateStockAvailability(productId, isInStock);
    }

    @Override
    public void deleteProduct(Integer productId) {
        productRepository.deleteProduct(productId);
    }

    @Override
    public boolean isProductExist(Integer productId) {
        return productRepository.isProductExist(productId);
    }
}
