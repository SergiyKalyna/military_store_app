package com.militarystore.product.stockdetails.mapper;

import com.militarystore.entity.product.ProductStockDetails;
import com.militarystore.entity.product.model.ProductSize;
import com.militarystore.jooq.tables.records.ProductStockDetailsRecord;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.stereotype.Component;

import static com.militarystore.jooq.Tables.PRODUCT_STOCK_DETAILS;

@Component
public class ProductStockDetailsMapper implements RecordMapper<Record, ProductStockDetails> {

    @Override
    public ProductStockDetails map(Record productStockDetailsRecord) {
        return ProductStockDetails.builder()
            .id(productStockDetailsRecord.get(PRODUCT_STOCK_DETAILS.ID))
            .productId(productStockDetailsRecord.get(PRODUCT_STOCK_DETAILS.PRODUCT_ID))
            .stockAvailability(productStockDetailsRecord.get(PRODUCT_STOCK_DETAILS.STOCK_AVAILABILITY))
            .productSize(ProductSize.valueOf(productStockDetailsRecord.get(PRODUCT_STOCK_DETAILS.PRODUCT_SIZE)))
            .build();
    }

    public ProductStockDetailsRecord toRecord(Integer productId, ProductStockDetails productDetails) {
        ProductStockDetailsRecord productStockDetailsRecord = new ProductStockDetailsRecord();
        productStockDetailsRecord.setProductSize(productDetails.productSize().name());
        productStockDetailsRecord.setStockAvailability(productDetails.stockAvailability());
        productStockDetailsRecord.setProductId(productId);

        return productStockDetailsRecord;
    }

    public ProductStockDetailsRecord toRecord(ProductStockDetails productDetails) {
        ProductStockDetailsRecord productStockDetailsRecord = new ProductStockDetailsRecord();
        productStockDetailsRecord.setId(productDetails.id());
        productStockDetailsRecord.setProductSize(productDetails.productSize().name());
        productStockDetailsRecord.setStockAvailability(productDetails.stockAvailability());
        productStockDetailsRecord.setProductId(productDetails.productId());

        return productStockDetailsRecord;
    }
}
