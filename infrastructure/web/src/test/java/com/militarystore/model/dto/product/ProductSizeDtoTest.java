package com.militarystore.model.dto.product;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static com.militarystore.model.dto.product.ProductSizeDto.L;
import static com.militarystore.model.dto.product.ProductSizeDto.M;
import static com.militarystore.model.dto.product.ProductSizeDto.S;
import static com.militarystore.model.dto.product.ProductSizeDto.SINGLE;
import static com.militarystore.model.dto.product.ProductSizeDto.SIZE_37;
import static com.militarystore.model.dto.product.ProductSizeDto.SIZE_38;
import static com.militarystore.model.dto.product.ProductSizeDto.SIZE_39;
import static com.militarystore.model.dto.product.ProductSizeDto.SIZE_40;
import static com.militarystore.model.dto.product.ProductSizeDto.SIZE_41;
import static com.militarystore.model.dto.product.ProductSizeDto.SIZE_42;
import static com.militarystore.model.dto.product.ProductSizeDto.SIZE_43;
import static com.militarystore.model.dto.product.ProductSizeDto.SIZE_44;
import static com.militarystore.model.dto.product.ProductSizeDto.SIZE_45;
import static com.militarystore.model.dto.product.ProductSizeDto.SIZE_46;
import static com.militarystore.model.dto.product.ProductSizeDto.XL;
import static com.militarystore.model.dto.product.ProductSizeDto.XS;
import static com.militarystore.model.dto.product.ProductSizeDto.XXL;
import static com.militarystore.model.dto.product.ProductSizeDto.XXXL;
import static com.militarystore.model.dto.product.ProductSizeDto.XXXXL;
import static com.militarystore.model.dto.product.ProductSizeGridTypeDto.CLOTHES;
import static com.militarystore.model.dto.product.ProductSizeGridTypeDto.COMMON;
import static com.militarystore.model.dto.product.ProductSizeGridTypeDto.GLOVES;
import static com.militarystore.model.dto.product.ProductSizeGridTypeDto.HATS;
import static com.militarystore.model.dto.product.ProductSizeGridTypeDto.HELMETS;
import static com.militarystore.model.dto.product.ProductSizeGridTypeDto.SHOES;
import static org.assertj.core.api.Assertions.assertThat;

class ProductSizeDtoTest {

    private static Stream<Arguments> getProductSizesDtoByGridType() {
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
    @MethodSource("getProductSizesDtoByGridType")
    void getProductSizesByGridType(ProductSizeGridTypeDto gridType, List<ProductSizeDto> expectedSizes) {
        assertThat(ProductSizeDto.getProductSizesDtoByGridType(gridType)).isEqualTo(expectedSizes);
    }
}