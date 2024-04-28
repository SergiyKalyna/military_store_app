package com.militarystore.port.out.product;

import com.militarystore.entity.product.Product;

import java.util.List;

public interface ProductPort {

    Integer addProduct(Product product);

    Product getProductById(Integer productId,  Integer userId);

    List<Product> getProductsBySubcategoryId(Integer subcategoryId);

    List<Product> getProductsByName(String name);

    void updateProduct(Product product);

    void updateStockAvailability(Integer productId, boolean isInStock);

    void deleteProduct(Integer productId);

    boolean isProductExist(Integer productId);

    List<Product> getProductsByIds(List<Integer> productIds);
}
