package com.militarystore.image;

import com.militarystore.IntegrationTest;
import com.militarystore.entity.image.ProductImage;
import com.militarystore.entity.product.model.ProductSizeGridType;
import com.militarystore.entity.product.model.ProductTag;
import com.militarystore.port.in.image.ImageUseCase;
import com.militarystore.port.out.googledrive.GoogleDrivePort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

import static com.militarystore.jooq.Tables.CATEGORIES;
import static com.militarystore.jooq.Tables.IMAGES;
import static com.militarystore.jooq.Tables.PRODUCTS;
import static com.militarystore.jooq.Tables.SUBCATEGORIES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ImageIntegrationTest extends IntegrationTest {

    private static final int PRODUCT_ID = 1;
    private static final int SUBCATEGORY_ID = 1;
    private static final String GOOGLE_DRIVE_ID = "googleDriveId";

    @Autowired
    private ImageUseCase imageUseCase;

    @MockBean
    private GoogleDrivePort googleDrivePort;

    @Test
    void saveProductImages() {
        initializeCategories();
        initializeProduct();

        var multipartFiles = List.of(mock(MultipartFile.class));
        var googleDriveIds = List.of(GOOGLE_DRIVE_ID);

        when(googleDrivePort.uploadFiles(multipartFiles)).thenReturn(googleDriveIds);

        imageUseCase.saveProductImages(PRODUCT_ID, multipartFiles);

        var expectedResult = ProductImage.builder()
            .productId(PRODUCT_ID)
            .googleDriveId(GOOGLE_DRIVE_ID)
            .ordinalNumber(1)
            .build();
        var imageFromDb = getProductImageFromDb();

        assertThat(imageFromDb).isEqualTo(expectedResult);
    }

    @Test
    void downloadProductImages() {
        initializeCategories();
        initializeProduct();
        initializeImages();

        var googleDriveImageIds = List.of(GOOGLE_DRIVE_ID, "googleDriveId2");
        var images = List.of(new byte[]{1, 2, 3});

        when(googleDrivePort.downloadFiles(googleDriveImageIds)).thenReturn(images);

        assertThat(imageUseCase.downloadProductImages(PRODUCT_ID)).isEqualTo(images);
    }

    @Test
    void getPrimaryProductsImages() {
        initializeCategories();
        initializeProduct();
        initializeImages();

        var productIds = List.of(PRODUCT_ID);
        var imageBytes = new byte[]{1, 2, 3};

        when(googleDrivePort.downloadFile(GOOGLE_DRIVE_ID)).thenReturn(imageBytes);

        var expectedResult = Map.of(PRODUCT_ID, imageBytes);

        assertThat(imageUseCase.getPrimaryProductsImages(productIds)).isEqualTo(expectedResult);
    }

    @Test
    void deleteProductImages() {
        initializeCategories();
        initializeProduct();
        initializeImages();

        imageUseCase.deleteProductImages(PRODUCT_ID);

        assertThat(imageUseCase.isImageExist(PRODUCT_ID)).isFalse();
    }

    @Test
    void isImageExist_whenImageExist_shouldReturnTrue() {
        initializeCategories();
        initializeProduct();
        initializeImages();

        assertThat(imageUseCase.isImageExist(PRODUCT_ID)).isTrue();
    }

    @Test
    void isImageExist_whenImageDoesntExist_shouldReturnFalse() {
        assertThat(imageUseCase.isImageExist(PRODUCT_ID)).isFalse();
    }

    private void initializeCategories() {
        dslContext.insertInto(CATEGORIES)
            .set(CATEGORIES.ID, 1)
            .set(CATEGORIES.NAME, "Category")
            .execute();

        dslContext.insertInto(SUBCATEGORIES)
            .set(SUBCATEGORIES.ID, SUBCATEGORY_ID)
            .set(SUBCATEGORIES.NAME, "Subcategory")
            .set(SUBCATEGORIES.CATEGORY_ID, 1)
            .execute();
    }

    private void initializeProduct() {
        dslContext.insertInto(PRODUCTS)
            .set(PRODUCTS.ID, PRODUCT_ID)
            .set(PRODUCTS.NAME, "Product")
            .set(PRODUCTS.DESCRIPTION, "Product description")
            .set(PRODUCTS.PRICE, 100)
            .set(PRODUCTS.SUBCATEGORY_ID, SUBCATEGORY_ID)
            .set(PRODUCTS.SIZE_GRID_TYPE, ProductSizeGridType.CLOTHES.name())
            .set(PRODUCTS.PRODUCT_TAG, ProductTag.NEW.name())
            .set(PRODUCTS.IS_IN_STOCK, true)
            .execute();
    }

    private void initializeImages() {
        dslContext.insertInto(IMAGES,
                IMAGES.PRODUCT_ID,
                IMAGES.GOOGLE_DRIVE_ID,
                IMAGES.ORDINAL_NUMBER
            )
            .values(PRODUCT_ID, GOOGLE_DRIVE_ID, 1)
            .values(PRODUCT_ID, "googleDriveId2", 2)
            .execute();
    }

    private ProductImage getProductImageFromDb() {
        return dslContext.select(
                IMAGES.PRODUCT_ID,
                IMAGES.GOOGLE_DRIVE_ID,
                IMAGES.ORDINAL_NUMBER
            )
            .from(IMAGES)
            .where(IMAGES.PRODUCT_ID.eq(PRODUCT_ID))
            .fetchOne(imageRecord -> ProductImage.builder()
                .productId(imageRecord.get(IMAGES.PRODUCT_ID))
                .googleDriveId(imageRecord.get(IMAGES.GOOGLE_DRIVE_ID))
                .ordinalNumber(imageRecord.get(IMAGES.ORDINAL_NUMBER))
                .build());
    }
}
