package com.militarystore.image;

import com.militarystore.entity.image.ProductImage;
import com.militarystore.port.out.googledrive.GoogleDrivePort;
import com.militarystore.port.out.image.ImagePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

    private static final int PRODUCT_ID = 1;

    @Mock
    private ImagePort imagePort;

    @Mock
    private GoogleDrivePort googleDrivePort;

    private ImageService imageService;

    @BeforeEach
    void setUp() {
        imageService = new ImageService(imagePort, googleDrivePort);
    }

    @Test
    void saveProductImages() {
        var multipartFile = mock(MultipartFile.class);
        var googleDriveId = "googleDriveId";
        var productImages = List.of(
            ProductImage.builder()
                .productId(PRODUCT_ID)
                .googleDriveId(googleDriveId)
                .ordinalNumber(1)
                .build());

        when(googleDrivePort.uploadFiles(List.of(multipartFile))).thenReturn(List.of(googleDriveId));

        imageService.saveProductImages(PRODUCT_ID, List.of(multipartFile));

        verify(imagePort).saveImages(productImages);
    }

    @Test
    void downloadProductImages() {
        var googleDriveImageIds = List.of("googleDriveImageId");
        var images = List.of(new byte[]{1, 2, 3});

        when(imagePort.getImageIdsByProductId(PRODUCT_ID)).thenReturn(googleDriveImageIds);
        when(googleDrivePort.downloadFiles(googleDriveImageIds)).thenReturn(images);

        assertThat(imageService.downloadProductImages(PRODUCT_ID)).isEqualTo(images);
    }

    @Test
    void getPrimaryProductsImages() {
        var productIds = List.of(PRODUCT_ID, 2, 3);
        var productsImageIds = Map.of(
            PRODUCT_ID, "googleDriveImageId",
            2, "googleDriveImageId2"
        );
        var image1 = new byte[]{1, 2, 3};
        var image2 = new byte[]{4, 5, 6};

        when(imagePort.getPrimaryImageIdsByProductIds(productIds)).thenReturn(productsImageIds);
        when(googleDrivePort.downloadFile("googleDriveImageId")).thenReturn(image1);
        when(googleDrivePort.downloadFile("googleDriveImageId2")).thenReturn(image2);

        var expectedResult = Map.of(
            PRODUCT_ID, image1,
            2, image2
        );

        assertThat(imageService.getPrimaryProductsImages(productIds)).isEqualTo(expectedResult);
    }

    @Test
    void deleteProductImages() {
        var googleDriveImageIds = List.of("googleDriveImageId");

        when(imagePort.getImageIdsByProductId(PRODUCT_ID)).thenReturn(googleDriveImageIds);

        imageService.deleteProductImages(PRODUCT_ID);

        verify(googleDrivePort).deleteFiles(googleDriveImageIds);
        verify(imagePort).deleteImagesByProductId(PRODUCT_ID);
    }

    @Test
    void isImageExist() {
        when(imagePort.isImageExist(PRODUCT_ID)).thenReturn(true);

        assertThat(imageService.isImageExist(PRODUCT_ID)).isTrue();
    }
}