package com.militarystore.image.mapper;

import com.militarystore.entity.image.ProductImage;
import com.militarystore.jooq.tables.records.ImagesRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ImageMapperTest {

    private ImageMapper imageMapper;

    @BeforeEach
    void setUp() {
        imageMapper = new ImageMapper();
    }

    @Test
    void toRecord() {
        var productImage = ProductImage.builder()
            .productId(2)
            .googleDriveId("googleDriveId")
            .ordinalNumber(3)
            .build();

        var imagesRecord = new ImagesRecord();
        imagesRecord.setProductId(2);
        imagesRecord.setGoogleDriveId("googleDriveId");
        imagesRecord.setOrdinalNumber(3);

        assertThat(imageMapper.toRecord(productImage)).isEqualTo(imagesRecord);
    }
}