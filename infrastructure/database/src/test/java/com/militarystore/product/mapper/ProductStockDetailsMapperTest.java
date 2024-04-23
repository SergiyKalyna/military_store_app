package com.militarystore.product.mapper;

import com.militarystore.entity.product.ProductStockDetails;
import com.militarystore.entity.product.model.ProductSize;
import com.militarystore.jooq.tables.records.ProductStockDetailsRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ProductStockDetailsMapperTest {

    private ProductStockDetailsMapper productStockDetailsMapper;

    @BeforeEach
    void setUp() {
        productStockDetailsMapper = new ProductStockDetailsMapper();
    }

    @Test
    void map() {
        var productStockDetailsRecord = new ProductStockDetailsRecord();
        productStockDetailsRecord.setId(1);
        productStockDetailsRecord.setProductId(1);
        productStockDetailsRecord.setProductSize(ProductSize.M.name());
        productStockDetailsRecord.setStockAvailability(10);

        var expectedResult = ProductStockDetails.builder()
            .id(1)
            .productId(1)
            .productSize(ProductSize.M)
            .stockAvailability(10)
            .build();

        assertThat(productStockDetailsMapper.map(productStockDetailsRecord)).isEqualTo(expectedResult);
    }

    @Test
    void toRecordWithProductId_shouldReturnCorrectResult() {
        var productStockDetails = ProductStockDetails.builder()
            .id(1)
            .productId(1)
            .productSize(ProductSize.M)
            .stockAvailability(10)
            .build();

        var expectedResult = new ProductStockDetailsRecord();
        expectedResult.setProductId(1);
        expectedResult.setProductSize(ProductSize.M.name());
        expectedResult.setStockAvailability(10);

        assertThat(productStockDetailsMapper.toRecord(1, productStockDetails)).isEqualTo(expectedResult);
    }

    @Test
    void toRecord_shouldReturnCorrectResult() {
        var productStockDetails = ProductStockDetails.builder()
            .id(1)
            .productId(1)
            .productSize(ProductSize.M)
            .stockAvailability(10)
            .build();

        var expectedResult = new ProductStockDetailsRecord();
        expectedResult.setId(1);
        expectedResult.setProductId(1);
        expectedResult.setProductSize(ProductSize.M.name());
        expectedResult.setStockAvailability(10);

        assertThat(productStockDetailsMapper.toRecord(productStockDetails)).isEqualTo(expectedResult);
    }

    private static Stream<Arguments> productSizeTestSource() {
        return Stream.of(
            Arguments.of("XS", ProductSize.XS),
            Arguments.of("S", ProductSize.S),
            Arguments.of("M", ProductSize.M),
            Arguments.of("L", ProductSize.L),
            Arguments.of("XL", ProductSize.XL),
            Arguments.of("XXL", ProductSize.XXL),
            Arguments.of("XXXL", ProductSize.XXXL),
            Arguments.of("SIZE_37", ProductSize.SIZE_37),
            Arguments.of("SIZE_38", ProductSize.SIZE_38),
            Arguments.of("SIZE_39", ProductSize.SIZE_39),
            Arguments.of("SIZE_40", ProductSize.SIZE_40),
            Arguments.of("SIZE_41", ProductSize.SIZE_41),
            Arguments.of("SIZE_42", ProductSize.SIZE_42),
            Arguments.of("SIZE_43", ProductSize.SIZE_43),
            Arguments.of("SIZE_44", ProductSize.SIZE_44),
            Arguments.of("SIZE_45", ProductSize.SIZE_45),
            Arguments.of("SIZE_46", ProductSize.SIZE_46),
            Arguments.of("SINGLE", ProductSize.SINGLE)
        );
    }

    @ParameterizedTest
    @MethodSource("productSizeTestSource")
    void testToProductSizeConvertation(String size, ProductSize expected) {
        assertThat(ProductSize.valueOf(size)).isEqualTo(expected);
    }
}