package com.militarystore.port.in.product;

import com.militarystore.entity.product.Product;
import com.militarystore.entity.product.ProductDetails;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductUseCase {

    Integer addProduct(Product product, List<MultipartFile> images);

    ProductDetails getProductById(Integer productId, Integer userId);

    List<ProductDetails> getProductsBySubcategoryId(Integer subcategoryId);

    List<ProductDetails> getProductsByName(String productName);

    void updateProduct(Product product);

    void deleteProduct(Integer productId);
}
