package com.militarystore.entity.product.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static com.militarystore.entity.product.model.ProductSizeGridType.CLOTHES;
import static com.militarystore.entity.product.model.ProductSizeGridType.COMMON;
import static com.militarystore.entity.product.model.ProductSizeGridType.GLOVES;
import static com.militarystore.entity.product.model.ProductSizeGridType.HATS;
import static com.militarystore.entity.product.model.ProductSizeGridType.HELMETS;
import static com.militarystore.entity.product.model.ProductSizeGridType.SHOES;

@RequiredArgsConstructor
@Getter
public enum ProductSize {
    XS("XS", Set.of(CLOTHES)),
    S("S", Set.of(CLOTHES)),
    M("M", Set.of(CLOTHES, HATS, GLOVES, HELMETS)),
    L("L", Set.of(CLOTHES, HATS, GLOVES, HELMETS)),
    XL("XL", Set.of(CLOTHES, HATS, GLOVES, HELMETS)),
    XXL("XXL", Set.of(CLOTHES)),
    XXXL("XXXL", Set.of(CLOTHES)),
    XXXXL("XXXXL", Set.of(CLOTHES)),
    SIZE_37("37", Set.of(SHOES)),
    SIZE_38("38", Set.of(SHOES)),
    SIZE_39("39", Set.of(SHOES)),
    SIZE_40("40", Set.of(SHOES)),
    SIZE_41("41", Set.of(SHOES)),
    SIZE_42("42", Set.of(SHOES)),
    SIZE_43("43", Set.of(SHOES)),
    SIZE_44("44", Set.of(SHOES)),
    SIZE_45("45", Set.of(SHOES)),
    SIZE_46("46", Set.of(SHOES)),
    SINGLE("Single size", Set.of(COMMON));

    private final String title;
    private final Set<ProductSizeGridType> applicableGridTypes;

    public static List<ProductSize> getProductSizesByGridType(ProductSizeGridType gridType) {
        return Stream.of(ProductSize.values())
            .filter(productSize -> productSize.getApplicableGridTypes().contains(gridType))
            .toList();
    }
}
