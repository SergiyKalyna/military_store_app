package com.militarystore.entity.product.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static com.militarystore.entity.product.model.ProductSize.L;
import static com.militarystore.entity.product.model.ProductSize.M;
import static com.militarystore.entity.product.model.ProductSize.S;
import static com.militarystore.entity.product.model.ProductSize.SINGLE;
import static com.militarystore.entity.product.model.ProductSize.SIZE_37;
import static com.militarystore.entity.product.model.ProductSize.SIZE_38;
import static com.militarystore.entity.product.model.ProductSize.SIZE_39;
import static com.militarystore.entity.product.model.ProductSize.SIZE_40;
import static com.militarystore.entity.product.model.ProductSize.SIZE_41;
import static com.militarystore.entity.product.model.ProductSize.SIZE_42;
import static com.militarystore.entity.product.model.ProductSize.SIZE_43;
import static com.militarystore.entity.product.model.ProductSize.SIZE_44;
import static com.militarystore.entity.product.model.ProductSize.SIZE_45;
import static com.militarystore.entity.product.model.ProductSize.SIZE_46;
import static com.militarystore.entity.product.model.ProductSize.XL;
import static com.militarystore.entity.product.model.ProductSize.XS;
import static com.militarystore.entity.product.model.ProductSize.XXL;
import static com.militarystore.entity.product.model.ProductSize.XXXL;
import static com.militarystore.entity.product.model.ProductSize.XXXXL;
import static com.militarystore.entity.product.model.ProductSizeGridType.CLOTHES;
import static com.militarystore.entity.product.model.ProductSizeGridType.COMMON;
import static com.militarystore.entity.product.model.ProductSizeGridType.GLOVES;
import static com.militarystore.entity.product.model.ProductSizeGridType.HATS;
import static com.militarystore.entity.product.model.ProductSizeGridType.HELMETS;
import static com.militarystore.entity.product.model.ProductSizeGridType.SHOES;
import static org.assertj.core.api.Assertions.assertThat;

class ProductSizeTest {

    private static Stream<Arguments> getProductSizesByGridType() {
        return Stream.of(
                Arguments.of(SHOES, List.of(SIZE_37, SIZE_38, SIZE_39, SIZE_40, SIZE_41, SIZE_42, SIZE_43, SIZE_44, SIZE_45, SIZE_46)),
                Arguments.of(CLOTHES, List.of(XS, S, M, L, XL, XXL, XXXL, XXXXL)),
                Arguments.of(HATS, List.of(M, L, XL)),
                Arguments.of(GLOVES, List.of(M, L, XL)),
                Arguments.of(HELMETS, List.of(M, L, XL)),
                Arguments.of(COMMON, List.of(SINGLE))
        );
    }

    @ParameterizedTest
    @MethodSource("getProductSizesByGridType")
    void getProductSizesByGridType(ProductSizeGridType gridType, List<ProductSize> expectedSizes) {
        assertThat(ProductSize.getProductSizesByGridType(gridType)).isEqualTo(expectedSizes);
    }
}