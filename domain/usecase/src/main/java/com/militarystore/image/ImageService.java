package com.militarystore.image;

import com.militarystore.entity.image.ProductImage;
import com.militarystore.port.in.image.ImageUseCase;
import com.militarystore.port.out.googledrive.GoogleDrivePort;
import com.militarystore.port.out.image.ImagePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService implements ImageUseCase {

    private final ImagePort imagePort;
    private final GoogleDrivePort googleDrivePort;

    @Override
    public void saveProductImages(Integer productId, List<MultipartFile> images) {
        var googleDriveIds = googleDrivePort.uploadFiles(images);
        var productImages = toProductImages(googleDriveIds, productId);

        imagePort.saveImages(productImages);
        log.info("Images for product with id {} saved", productId);
    }

    @Override
    public List<byte[]> downloadProductImages(Integer productId) {
        var googleDriveImageIds = imagePort.getImageIdsByProductId(productId);
        var images = googleDrivePort.downloadFiles(googleDriveImageIds);

        log.info("Images for product with id {} downloaded", productId);

        return images;
    }

    @Override
    public Map<Integer, byte[]> getPrimaryProductsImages(List<Integer> productIds) {
        var productsImageIds = imagePort.getPrimaryImageIdsByProductIds(productIds);

        var primaryProductsImages = productIds.stream()
            .filter(productsImageIds::containsKey)
            .collect(toMap(
                productId -> productId,
                productId -> googleDrivePort.downloadFile(productsImageIds.get(productId)))
            );

        log.info("Primary images for products with ids {} downloaded", productIds);

        return primaryProductsImages;
    }

    @Override
    public void deleteProductImages(Integer productId) {
        var googleDriveImageIds = imagePort.getImageIdsByProductId(productId);

        googleDrivePort.deleteFiles(googleDriveImageIds);
        imagePort.deleteImagesByProductId(productId);

        log.info("Images for product with id {} deleted", productId);
    }

    private List<ProductImage> toProductImages(List<String> googleDriveIds, Integer productId) {
        return IntStream.range(0, googleDriveIds.size())
            .mapToObj(index -> ProductImage.builder()
                .productId(productId)
                .googleDriveId(googleDriveIds.get(index))
                .ordinalNumber(index + 1)
                .build())
            .toList();
    }
}
