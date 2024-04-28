package com.militarystore.product.mapper;

import com.militarystore.entity.product.Product;
import com.militarystore.entity.product.ProductFeedback;
import com.militarystore.entity.product.ProductStockDetails;
import com.militarystore.entity.product.model.ProductSizeGridType;
import com.militarystore.entity.product.model.ProductTag;
import com.militarystore.jooq.tables.records.ProductsRecord;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.militarystore.jooq.Tables.PRODUCTS;
import static com.militarystore.product.ProductRepository.AVG_RATE;

@Component
public class ProductMapper implements RecordMapper<Record, Product> {

    @Override
    public Product map(Record productRecord) {
        return Product.builder()
            .id(productRecord.get(PRODUCTS.ID))
            .name(productRecord.get(PRODUCTS.NAME))
            .price(productRecord.get(PRODUCTS.PRICE))
            .tag(ProductTag.valueOf(productRecord.get(PRODUCTS.PRODUCT_TAG)))
            .isInStock(productRecord.get(PRODUCTS.IS_IN_STOCK))
            .avgRate(productRecord.get(AVG_RATE, Double.class))
            .build();
    }

    public Product map(
        ProductsRecord productRecord,
        List<ProductStockDetails> productStockDetails,
        double avgRate,
        List<ProductFeedback> productFeedbacks,
        boolean isProductInUserWishlist
    ) {
        return Product.builder()
            .id(productRecord.get(PRODUCTS.ID))
            .name(productRecord.get(PRODUCTS.NAME))
            .description(productRecord.get(PRODUCTS.DESCRIPTION))
            .price(productRecord.get(PRODUCTS.PRICE))
            .subcategoryId(productRecord.get(PRODUCTS.SUBCATEGORY_ID))
            .sizeGridType(ProductSizeGridType.valueOf(productRecord.get(PRODUCTS.SIZE_GRID_TYPE)))
            .tag(ProductTag.valueOf(productRecord.get(PRODUCTS.PRODUCT_TAG)))
            .isInStock(productRecord.get(PRODUCTS.IS_IN_STOCK))
            .stockDetails(productStockDetails)
            .avgRate(avgRate)
            .feedbacks(productFeedbacks)
            .isProductInUserWishlist(isProductInUserWishlist)
            .build();
    }
}
