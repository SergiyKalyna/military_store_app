package com.militarystore.product.mapper;

import com.militarystore.entity.product.Product;
import com.militarystore.entity.product.ProductStockDetails;
import com.militarystore.entity.product.model.ProductSize;
import com.militarystore.entity.product.model.ProductSizeGridType;
import com.militarystore.entity.product.model.ProductTag;
import com.militarystore.jooq.tables.records.ProductsRecord;
import com.militarystore.product.stockdetails.mapper.ProductStockDetailsMapper;
import org.jooq.Record;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

import static com.militarystore.jooq.Tables.PRODUCTS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductMapperTest {

    @Mock
    private ProductStockDetailsMapper productStockDetailsMapper;

    private ProductMapper productMapper;

    @BeforeEach
    void setUp() {
        productMapper = new ProductMapper(productStockDetailsMapper);
    }

    @Test
    void map() {
        var productRecord = new ProductsRecord();
        productRecord.set(PRODUCTS.ID, 1);
        productRecord.set(PRODUCTS.NAME, "product");
        productRecord.set(PRODUCTS.PRICE, 100);
        productRecord.set(PRODUCTS.PRODUCT_TAG, "NEW");
        productRecord.set(PRODUCTS.IS_IN_STOCK, true);

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
        var record = (Record) new ProductsRecord(1, "product", "description", 100, 1, "CLOTHES", "NEW", true);
        List<Record> records = List.of(record);
        var stockDetail = ProductStockDetails.builder()
            .id(1)
            .productId(1)
            .productSize(ProductSize.M)
            .stockAvailability(10)
            .build();

        when(productStockDetailsMapper.map(any())).thenReturn(stockDetail);

        var expectedResult = Product.builder()
            .id(1)
            .name("product")
            .description("description")
            .price(100)
            .subcategoryId(1)
            .sizeGridType(ProductSizeGridType.CLOTHES)
            .tag(ProductTag.NEW)
            .isInStock(true)
            .stockDetails(List.of(stockDetail))
            .build();

        assertThat(productMapper.map(records)).isEqualTo(expectedResult);
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