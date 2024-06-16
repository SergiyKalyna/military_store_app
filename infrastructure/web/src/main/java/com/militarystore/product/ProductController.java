package com.militarystore.product;

import com.militarystore.converter.product.ProductConverter;
import com.militarystore.entity.user.User;
import com.militarystore.model.dto.product.ProductDto;
import com.militarystore.model.request.product.ProductRequest;
import com.militarystore.port.in.product.ProductUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static java.util.Objects.nonNull;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private static final int UNAUTHORIZED_USER_ID = 0;

    private final ProductUseCase productUseCase;
    private final ProductConverter productConverter;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public Integer addProduct(
        @RequestPart("request") ProductRequest request,
        @RequestPart("images") List<MultipartFile> images
    ) {
        var product = productConverter.convertToProduct(request);

        return productUseCase.addProduct(product, images);
    }

    @GetMapping("/{productId}")
    public ProductDto getProductById(
        @PathVariable("productId") Integer productId,
        @AuthenticationPrincipal User user
    ) {
        var userId = nonNull(user) ? user.id() : UNAUTHORIZED_USER_ID;
        var productDetails = productUseCase.getProductById(productId, userId);

        return productConverter.convertToProductDto(productDetails);
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
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public void updateProduct(@PathVariable("productId") Integer productId, @RequestBody ProductRequest request) {
        var productToUpdate = productConverter.convertToProduct(productId, request);
        productUseCase.updateProduct(productToUpdate);
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public void deleteProductById(@PathVariable("productId") Integer productId) {
        productUseCase.deleteProduct(productId);
    }
}
