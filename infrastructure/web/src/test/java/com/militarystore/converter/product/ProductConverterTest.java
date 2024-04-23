package com.militarystore.converter.product;

import com.militarystore.entity.product.Product;
import com.militarystore.entity.product.ProductFeedback;
import com.militarystore.entity.product.ProductStockDetails;
import com.militarystore.entity.product.model.ProductSize;
import com.militarystore.entity.product.model.ProductSizeGridType;
import com.militarystore.entity.product.model.ProductTag;
import com.militarystore.model.dto.product.ProductDto;
import com.militarystore.model.dto.product.ProductFeedbackDto;
import com.militarystore.model.dto.product.ProductSizeDto;
import com.militarystore.model.dto.product.ProductSizeGridTypeDto;
import com.militarystore.model.dto.product.ProductStockDetailsDto;
import com.militarystore.model.dto.product.ProductTagDto;
import com.militarystore.model.request.product.ProductRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ProductConverterTest {

    private ProductConverter productConverter;

    @BeforeEach
    void setUp() {
        productConverter = new ProductConverter(new ProductFeedbackConverter());
    }

    @Test
    void convertToProductDto() {
        var product = Product.builder()
            .id(1)
            .name("Product")
            .description("Product description")
            .price(100)
            .subcategoryId(1)
            .sizeGridType(ProductSizeGridType.CLOTHES)
            .tag(ProductTag.NEW)
            .stockDetails(List.of(
                ProductStockDetails.builder()
                    .id(1)
                    .productId(1)
                    .productSize(ProductSize.M)
                    .stockAvailability(10)
                    .build(),
                ProductStockDetails.builder()
                    .id(2)
                    .productId(1)
                    .productSize(ProductSize.S)
                    .stockAvailability(20)
                    .build()
            ))
            .isInStock(true)
            .feedbacks(List.of(
                ProductFeedback.builder()
                    .id(1)
                    .productId(1)
                    .userId(1)
                    .feedback("Feedback")
                    .dateTime(LocalDateTime.of(2021, 1, 1, 0, 0))
                    .build(),
                ProductFeedback.builder()
                    .id(2)
                    .productId(1)
                    .userId(2)
                    .feedback("Feedback 2")
                    .dateTime(LocalDateTime.of(2022, 1, 2, 0, 0))
                    .build()
            ))
            .build();


        var expectedDto = ProductDto.builder()
            .id(1)
            .name("Product")
            .description("Product description")
            .price(100)
            .subcategoryId(1)
            .sizeGridType(ProductSizeGridTypeDto.CLOTHES)
            .tag(ProductTagDto.NEW)
            .stockDetails(List.of(
                ProductStockDetailsDto.builder()
                    .id(2)
                    .productId(1)
                    .productSize(ProductSizeDto.S)
                    .stockAvailability(20)
                    .build(),
                ProductStockDetailsDto.builder()
                    .id(1)
                    .productId(1)
                    .productSize(ProductSizeDto.M)
                    .stockAvailability(10)
                    .build()
            ))
            .isInStock(true)
            .avgRate(0.0)
            .feedbacks(List.of(
                ProductFeedbackDto.builder()
                    .id(2)
                    .userId(2)
                    .feedback("Feedback 2")
                    .dateTime(LocalDateTime.of(2022, 1, 2, 0, 0))
                    .build(),
                ProductFeedbackDto.builder()
                    .id(1)
                    .userId(1)
                    .feedback("Feedback")
                    .dateTime(LocalDateTime.of(2021, 1, 1, 0, 0))
                    .build()))
            .build();

        assertThat(productConverter.convertToProductDto(product)).isEqualTo(expectedDto);
    }

    @Test
    void convertToProductDto_withAvgRate() {
        var product = Product.builder()
            .id(1)
            .name("Product")
            .description("Product description")
            .price(100)
            .subcategoryId(1)
            .sizeGridType(ProductSizeGridType.CLOTHES)
            .tag(ProductTag.NEW)
            .stockDetails(List.of(
                ProductStockDetails.builder()
                    .id(1)
                    .productId(1)
                    .productSize(ProductSize.M)
                    .stockAvailability(10)
                    .build(),
                ProductStockDetails.builder()
                    .id(2)
                    .productId(1)
                    .productSize(ProductSize.S)
                    .stockAvailability(20)
                    .build()
            ))
            .isInStock(true)
            .avgRate(4.5)
            .feedbacks(List.of())
            .build();

        var expectedDto = ProductDto.builder()
            .id(1)
            .name("Product")
            .description("Product description")
            .price(100)
            .subcategoryId(1)
            .sizeGridType(ProductSizeGridTypeDto.CLOTHES)
            .tag(ProductTagDto.NEW)
            .stockDetails(List.of(
                ProductStockDetailsDto.builder()
                    .id(2)
                    .productId(1)
                    .productSize(ProductSizeDto.S)
                    .stockAvailability(20)
                    .build(),
                ProductStockDetailsDto.builder()
                    .id(1)
                    .productId(1)
                    .productSize(ProductSizeDto.M)
                    .stockAvailability(10)
                    .build()
            ))
            .isInStock(true)
            .avgRate(4.5)
            .feedbacks(List.of())
            .build();

        assertThat(productConverter.convertToProductDto(product)).isEqualTo(expectedDto);
    }

    @Test
    void convertToSearchProductDto() {
        var product = Product.builder()
            .id(1)
            .name("Product")
            .price(100)
            .tag(ProductTag.NEW)
            .isInStock(true)
            .build();

        var expectedDto = ProductDto.builder()
            .id(1)
            .name("Product")
            .price(100)
            .tag(ProductTagDto.NEW)
            .isInStock(true)
            .avgRate(0.0)
            .build();

        assertThat(productConverter.convertToSearchProductDto(product)).isEqualTo(expectedDto);
    }

    @Test
    void convertToSearchProductDto_withAvgRate() {
        var product = Product.builder()
            .id(1)
            .name("Product")
            .price(100)
            .tag(ProductTag.NEW)
            .isInStock(true)
            .avgRate(4.5)
            .build();

        var expectedDto = ProductDto.builder()
            .id(1)
            .name("Product")
            .price(100)
            .tag(ProductTagDto.NEW)
            .isInStock(true)
            .avgRate(4.5)
            .build();

        assertThat(productConverter.convertToSearchProductDto(product)).isEqualTo(expectedDto);
    }

    @Test
    void convertToProduct() {
        var request = new ProductRequest(
            "Product",
            "Product description",
            100,
            1,
            ProductSizeGridTypeDto.CLOTHES,
            ProductTagDto.NEW,
            List.of(
                new ProductStockDetailsDto(1, 1, 10, ProductSizeDto.M),
                new ProductStockDetailsDto(2, 1, 20, ProductSizeDto.S)
            )
        );

        var expectedProduct = Product.builder()
            .name("Product")
            .description("Product description")
            .price(100)
            .subcategoryId(1)
            .sizeGridType(ProductSizeGridType.CLOTHES)
            .tag(ProductTag.NEW)
            .stockDetails(List.of(
                ProductStockDetails.builder()
                    .id(1)
                    .productId(1)
                    .productSize(ProductSize.M)
                    .stockAvailability(10)
                    .build(),
                ProductStockDetails.builder()
                    .id(2)
                    .productId(1)
                    .productSize(ProductSize.S)
                    .stockAvailability(20)
                    .build()
            )).build();

        assertThat(productConverter.convertToProduct(request)).isEqualTo(expectedProduct);
    }

    @Test
    void convertToProductWithId() {
        var request = new ProductRequest(
            "Product",
            "Product description",
            100,
            1,
            ProductSizeGridTypeDto.CLOTHES,
            ProductTagDto.NEW,
            List.of(
                new ProductStockDetailsDto(1, 1, 10, ProductSizeDto.M),
                new ProductStockDetailsDto(2, 1, 20, ProductSizeDto.S)
            )
        );

        var expectedProduct = Product.builder()
            .id(1)
            .name("Product")
            .description("Product description")
            .price(100)
            .subcategoryId(1)
            .sizeGridType(ProductSizeGridType.CLOTHES)
            .tag(ProductTag.NEW)
            .stockDetails(List.of(
                ProductStockDetails.builder()
                    .id(1)
                    .productId(1)
                    .productSize(ProductSize.M)
                    .stockAvailability(10)
                    .build(),
                ProductStockDetails.builder()
                    .id(2)
                    .productId(1)
                    .productSize(ProductSize.S)
                    .stockAvailability(20)
                    .build()
            ))
            .build();

        assertThat(productConverter.convertToProduct(1, request)).isEqualTo(expectedProduct);
    }

    @ParameterizedTest
    @EnumSource(ProductSize.class)
    void convertToProductSizeDto(ProductSize productSize) {
        assertDoesNotThrow(() -> ProductSizeDto.valueOf(productSize.name()));
    }

    @ParameterizedTest
    @EnumSource(ProductSizeGridType.class)
    void convertToProductSizeGridTypeDto(ProductSizeGridType productSizeGridType) {
        assertDoesNotThrow(() -> ProductSizeGridTypeDto.valueOf(productSizeGridType.name()));
    }

    @ParameterizedTest
    @EnumSource(ProductTag.class)
    void convertToProductTagDto(ProductTag productTag) {
        assertDoesNotThrow(() -> ProductTagDto.valueOf(productTag.name()));
    }

    @ParameterizedTest
    @EnumSource(ProductSizeDto.class)
    void convertToProductSize(ProductSizeDto productSizeDto) {
        assertDoesNotThrow(() -> ProductSize.valueOf(productSizeDto.name()));
    }

    @ParameterizedTest
    @EnumSource(ProductSizeGridTypeDto.class)
    void convertToProductSizeGridType(ProductSizeGridTypeDto productSizeGridTypeDto) {
        assertDoesNotThrow(() -> ProductSizeGridType.valueOf(productSizeGridTypeDto.name()));
    }

    @ParameterizedTest
    @EnumSource(ProductTagDto.class)
    void convertToProductTag(ProductTagDto productTagDto) {
        assertDoesNotThrow(() -> ProductTag.valueOf(productTagDto.name()));
    }
}