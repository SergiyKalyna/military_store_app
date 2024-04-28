package com.militarystore.product.mapper;

import com.militarystore.entity.product.Product;
import com.militarystore.entity.product.ProductFeedback;
import com.militarystore.entity.product.ProductStockDetails;
import com.militarystore.entity.product.model.ProductSize;
import com.militarystore.entity.product.model.ProductSizeGridType;
import com.militarystore.entity.product.model.ProductTag;
import com.militarystore.jooq.tables.records.ProductsRecord;
import org.jooq.Record;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static com.militarystore.jooq.Tables.PRODUCTS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProductMapperTest {

    private ProductMapper productMapper;

    @BeforeEach
    void setUp() {
        productMapper = new ProductMapper();
    }

    @Test
    void map() {
        var productRecord = mock(Record.class);

        when(productRecord.get(PRODUCTS.ID)).thenReturn(1);
        when(productRecord.get(PRODUCTS.NAME)).thenReturn("product");
        when(productRecord.get(PRODUCTS.PRICE)).thenReturn(100);
        when(productRecord.get(PRODUCTS.PRODUCT_TAG)).thenReturn("NEW");
        when(productRecord.get(PRODUCTS.IS_IN_STOCK)).thenReturn(true);
        when(productRecord.get("avg_rate", Double.class)).thenReturn(4.5);

        var expectedResult = Product.builder()
            .id(1)
            .name("product")
            .price(100)
            .tag(ProductTag.NEW)
            .isInStock(true)
            .avgRate(4.5)
            .build();

        assertThat(productMapper.map(productRecord)).isEqualTo(expectedResult);
    }

    @Test
    void map_whenAvgRateIsNull() {
        var productRecord = mock(Record.class);

        when(productRecord.get(PRODUCTS.ID)).thenReturn(1);
        when(productRecord.get(PRODUCTS.NAME)).thenReturn("product");
        when(productRecord.get(PRODUCTS.PRICE)).thenReturn(100);
        when(productRecord.get(PRODUCTS.PRODUCT_TAG)).thenReturn("NEW");
        when(productRecord.get(PRODUCTS.IS_IN_STOCK)).thenReturn(true);
        when(productRecord.get("avg_rate", Double.class)).thenReturn(null);

        var expectedResult = Product.builder()
            .id(1)
            .name("product")
            .price(100)
            .tag(ProductTag.NEW)
            .isInStock(true)
            .build();

        assertThat(productMapper.map(productRecord)).isEqualTo(expectedResult);
    }

    @Test
    void map_withListOfRecords() {
        var productRecord = new ProductsRecord();
        productRecord.set(PRODUCTS.ID, 1);
        productRecord.set(PRODUCTS.NAME, "product");
        productRecord.set(PRODUCTS.DESCRIPTION, "description");
        productRecord.set(PRODUCTS.PRICE, 100);
        productRecord.set(PRODUCTS.SUBCATEGORY_ID, 1);
        productRecord.set(PRODUCTS.SIZE_GRID_TYPE, "CLOTHES");
        productRecord.set(PRODUCTS.PRODUCT_TAG, "NEW");
        productRecord.set(PRODUCTS.IS_IN_STOCK, true);

        var stockDetail = ProductStockDetails.builder()
            .id(1)
            .productId(1)
            .productSize(ProductSize.M)
            .stockAvailability(10)
            .build();

        var feedback = ProductFeedback.builder()
            .id(1)
            .productId(1)
            .userId(1)
            .feedback("comment")
            .dateTime(LocalDateTime.of(2021, 1, 1, 0, 0))
            .build();

        var isProductInUserWishlist = true;

        var expectedResult = Product.builder()
            .id(1)
            .name("product")
            .description("description")
            .price(100)
            .subcategoryId(1)
            .sizeGridType(ProductSizeGridType.CLOTHES)
            .tag(ProductTag.NEW)
            .isInStock(true)
            .avgRate(4.5)
            .stockDetails(List.of(stockDetail))
            .feedbacks(List.of(feedback))
            .isProductInUserWishlist(isProductInUserWishlist)
            .build();

        assertThat(productMapper.map(productRecord, List.of(stockDetail), 4.5, List.of(feedback), isProductInUserWishlist))
            .isEqualTo(expectedResult);
    }

    private static Stream<Arguments> productTagTestSource() {
        return Stream.of(
            Arguments.of("NEW", ProductTag.NEW),
            Arguments.of("SALE", ProductTag.SALE),
            Arguments.of("BESTSELLER", ProductTag.BESTSELLER),
            Arguments.of("NONE", ProductTag.NONE)
        );
    }

    @ParameterizedTest
    @MethodSource("productTagTestSource")
    void testToProductTagConvertation(String tag, ProductTag expected) {
        assertThat(ProductTag.valueOf(tag)).isEqualTo(expected);
    }

    private static Stream<Arguments> productSizeGridTypeTestSource() {
        return Stream.of(
            Arguments.of("CLOTHES", ProductSizeGridType.CLOTHES),
            Arguments.of("SHOES", ProductSizeGridType.SHOES),
            Arguments.of("COMMON", ProductSizeGridType.COMMON),
            Arguments.of("HATS", ProductSizeGridType.HATS),
            Arguments.of("HELMETS", ProductSizeGridType.HELMETS),
            Arguments.of("GLOVES", ProductSizeGridType.GLOVES)
        );
    }

    @ParameterizedTest
    @MethodSource("productSizeGridTypeTestSource")
    void testToProductSizeGridTypeConvertation(String sizeGridType, ProductSizeGridType expected) {
        assertThat(ProductSizeGridType.valueOf(sizeGridType)).isEqualTo(expected);
    }
}