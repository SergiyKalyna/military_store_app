package com.militarystore.product;

import com.militarystore.converter.product.ProductConverter;
import com.militarystore.model.dto.product.ProductDto;
import com.militarystore.model.request.product.ProductRequest;
import com.militarystore.port.in.product.ProductUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductUseCase productUseCase;
    private final ProductConverter productConverter;

    @PostMapping
    public Integer addProduct(@RequestBody ProductRequest request) {
        var product = productConverter.convertToProduct(request);

        return productUseCase.addProduct(product);
    }

    @GetMapping("/{productId}")
    public ProductDto getProductById(@PathVariable("productId") Integer productId) {
        var product = productUseCase.getProductById(productId);

        return productConverter.convertToProductDto(product);
    }

    @GetMapping("/subcategory-id/{subcategoryId}")
    public List<ProductDto> getProductsBySubcategoryId(@PathVariable("subcategoryId") Integer subcategoryId) {
        var products = productUseCase.getProductsBySubcategoryId(subcategoryId);

        return products.stream()
            .map(productConverter::convertToSearchProductDto)
            .toList();
    }

    @GetMapping("/product-name/{productName}")
    public List<ProductDto> getProductsByName(@PathVariable("productName") String productName) {
        var products = productUseCase.getProductsByName(productName);

        return products.stream()
            .map(productConverter::convertToSearchProductDto)
            .toList();
    }

    @PutMapping("/update/{productId}")
    public void updateProduct(@PathVariable("productId") Integer productId, @RequestBody ProductRequest request) {
        var productToUpdate = productConverter.convertToProduct(productId, request);
        productUseCase.updateProduct(productToUpdate);
    }

    @DeleteMapping("/{productId}")
    public void deleteProductById(@PathVariable("productId") Integer productId) {
        productUseCase.deleteProduct(productId);
    }
}
