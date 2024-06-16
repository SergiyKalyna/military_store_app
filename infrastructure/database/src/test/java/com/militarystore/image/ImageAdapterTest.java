package com.militarystore.image;

import com.militarystore.entity.image.ProductImage;
import com.militarystore.image.mapper.ImageMapper;
import com.militarystore.jooq.tables.records.ImagesRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImageAdapterTest {

    private static final int PRODUCT_ID = 1;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private ImageMapper imageMapper;

    private ImageAdapter imageAdapter;

    @BeforeEach
    void setUp() {
        imageAdapter = new ImageAdapter(imageRepository, imageMapper);
    }

    @Test
    void saveImages() {
        var productImage = ProductImage.builder().build();
        var imageRecord = new ImagesRecord();

        when(imageMapper.toRecord(productImage)).thenReturn(imageRecord);

        imageAdapter.saveImages(List.of(productImage));

        verify(imageRepository).saveImages(List.of(imageRecord));
    }

    @Test
    void getImageIdsByProductId() {
        var fileIds = List.of("fileId");

        when(imageRepository.getImagesByProductId(PRODUCT_ID)).thenReturn(fileIds);

        assertThat(imageAdapter.getImageIdsByProductId(PRODUCT_ID)).isEqualTo(fileIds);
    }

    @Test
    void getPrimaryImageIdsByProductIds() {
        var productImageIdMap = Map.of(PRODUCT_ID, "fileId");

        when(imageRepository.getPrimaryImagesByProductIds(List.of(PRODUCT_ID))).thenReturn(productImageIdMap);

        assertThat(imageAdapter.getPrimaryImageIdsByProductIds(List.of(PRODUCT_ID))).isEqualTo(productImageIdMap);
    }

    @Test
    void deleteImagesByProductId() {
        imageAdapter.deleteImagesByProductId(PRODUCT_ID);

        verify(imageRepository).deleteImagesByProductId(PRODUCT_ID);
    }

    @Test
    void isImageExist() {
        when(imageRepository.isImageExist(PRODUCT_ID)).thenReturn(true);

        assertTrue(imageAdapter.isImageExist(PRODUCT_ID));
    }
}