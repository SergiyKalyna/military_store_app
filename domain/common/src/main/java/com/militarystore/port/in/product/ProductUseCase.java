package com.militarystore.port.in.product;

import com.militarystore.entity.product.Product;

import java.util.List;

public interface ProductUseCase {

    Integer addProduct(Product product);

    Product getProductById(Integer productId);

    List<Product> getProductsBySubcategoryId(Integer subcategoryId);

    List<Product> getProductsByName(String productName);

    void updateProduct(Product product);

    void deleteProduct(Integer productId);
}
