package com.militarystore.product.mapper;

import com.militarystore.entity.product.Product;
import com.militarystore.entity.product.model.ProductSizeGridType;
import com.militarystore.entity.product.model.ProductTag;
import com.militarystore.product.stockdetails.mapper.ProductStockDetailsMapper;
import lombok.RequiredArgsConstructor;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.militarystore.jooq.Tables.PRODUCTS;

@Component
@RequiredArgsConstructor
public class ProductMapper implements RecordMapper<Record, Product> {

    private final ProductStockDetailsMapper productStockDetailsMapper;

    @Override
    public Product map(Record productRecord) {
        return Product.builder()
            .id(productRecord.get(PRODUCTS.ID))
            .name(productRecord.get(PRODUCTS.NAME))
            .price(productRecord.get(PRODUCTS.PRICE))
            .tag(ProductTag.valueOf(productRecord.get(PRODUCTS.PRODUCT_TAG)))
            .isInStock(productRecord.get(PRODUCTS.IS_IN_STOCK))
            .build();
    }

    public Product map(List<Record> productRecords) {
        var productRecord = productRecords.get(0);
        var stockDetails = productRecords.stream()
            .map(productStockDetailsMapper::map)
            .toList();

        return Product.builder()
            .id(productRecord.get(PRODUCTS.ID))
            .name(productRecord.get(PRODUCTS.NAME))
            .description(productRecord.get(PRODUCTS.DESCRIPTION))
            .price(productRecord.get(PRODUCTS.PRICE))
            .subcategoryId(productRecord.get(PRODUCTS.SUBCATEGORY_ID))
            .sizeGridType(ProductSizeGridType.valueOf(productRecord.get(PRODUCTS.SIZE_GRID_TYPE)))
            .tag(ProductTag.valueOf(productRecord.get(PRODUCTS.PRODUCT_TAG)))
            .isInStock(productRecord.get(PRODUCTS.IS_IN_STOCK))
            .stockDetails(stockDetails)
            .build();
    }
}
