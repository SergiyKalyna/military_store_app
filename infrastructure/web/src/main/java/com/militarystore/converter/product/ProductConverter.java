package com.militarystore.converter.product;

import com.militarystore.entity.product.Product;
import com.militarystore.entity.product.ProductStockDetails;
import com.militarystore.entity.product.model.ProductSize;
import com.militarystore.entity.product.model.ProductSizeGridType;
import com.militarystore.entity.product.model.ProductTag;
import com.militarystore.model.dto.product.ProductDto;
import com.militarystore.model.dto.product.ProductSizeDto;
import com.militarystore.model.dto.product.ProductSizeGridTypeDto;
import com.militarystore.model.dto.product.ProductStockDetailsDto;
import com.militarystore.model.dto.product.ProductTagDto;
import com.militarystore.model.request.product.ProductRequest;
import org.springframework.stereotype.Component;

import java.util.Comparator;

@Component
public class ProductConverter {

    public ProductDto convertToProductDto(Product product) {
        return ProductDto.builder()
            .id(product.id())
            .name(product.name())
            .description(product.description())
            .price(product.price())
            .subcategoryId(product.subcategoryId())
            .sizeGridType(ProductSizeGridTypeDto.valueOf(product.sizeGridType().name()))
            .tag(ProductTagDto.valueOf(product.tag().name()))
            .stockDetails(
                product.stockDetails().stream()
                    .map(this::convertToProductStockDetailsDto)
                    .sorted(Comparator.comparing(dto -> dto.productSize().ordinal()))
                    .toList())
            .isInStock(product.isInStock())
            .build();
    }

    public ProductDto convertToSearchProductDto(Product product) {
        return ProductDto.builder()
            .id(product.id())
            .name(product.name())
            .price(product.price())
            .tag(ProductTagDto.valueOf(product.tag().name()))
            .isInStock(product.isInStock())
            .build();
    }

    public Product convertToProduct(ProductRequest request) {
        return Product.builder()
            .name(request.name())
            .description(request.description())
            .price(request.price())
            .subcategoryId(request.subcategoryId())
            .sizeGridType(ProductSizeGridType.valueOf(request.sizeGridType().name()))
            .tag(ProductTag.valueOf(request.tag().name()))
            .stockDetails(request.stockDetails().stream().map(this::convertToProductStockDetails).toList())
            .build();
    }

    public Product convertToProduct(Integer productId, ProductRequest request) {
        return Product.builder()
            .id(productId)
            .name(request.name())
            .description(request.description())
            .price(request.price())
            .subcategoryId(request.subcategoryId())
            .sizeGridType(ProductSizeGridType.valueOf(request.sizeGridType().name()))
            .tag(ProductTag.valueOf(request.tag().name()))
            .stockDetails(request.stockDetails().stream().map(this::convertToProductStockDetails).toList())
            .build();
    }

    private ProductStockDetails convertToProductStockDetails(ProductStockDetailsDto dto) {
        return ProductStockDetails.builder()
            .id(dto.id())
            .productId(dto.productId())
            .productSize(ProductSize.valueOf(dto.productSize().name()))
            .stockAvailability(dto.stockAvailability())
            .build();
    }

    private ProductStockDetailsDto convertToProductStockDetailsDto(ProductStockDetails stockDetails) {
        return ProductStockDetailsDto.builder()
            .id(stockDetails.id())
            .productId(stockDetails.productId())
            .stockAvailability(stockDetails.stockAvailability())
            .productSize(ProductSizeDto.valueOf(stockDetails.productSize().name()))
            .build();
    }
}
