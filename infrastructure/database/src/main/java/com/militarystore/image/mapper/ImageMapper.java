package com.militarystore.image.mapper;

import com.militarystore.entity.image.ProductImage;
import com.militarystore.jooq.tables.records.ImagesRecord;
import org.springframework.stereotype.Component;

@Component
public class ImageMapper {

    public ImagesRecord toRecord(ProductImage productImage) {
        var imagesRecord = new ImagesRecord();
        imagesRecord.setProductId(productImage.productId());
        imagesRecord.setOrdinalNumber(productImage.ordinalNumber());
        imagesRecord.setGoogleDriveId(productImage.googleDriveId());

        return imagesRecord;
    }
}
