package com.militarystore.image;

import com.militarystore.entity.image.ProductImage;
import com.militarystore.image.mapper.ImageMapper;
import com.militarystore.port.out.image.ImagePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ImageAdapter implements ImagePort {

    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;

    @Override
    public void saveImages(List<ProductImage> productImages) {
        var imagesRecords = productImages.stream()
            .map(imageMapper::toRecord)
            .toList();

        imageRepository.saveImages(imagesRecords);
    }

    @Override
    public List<String> getImageIdsByProductId(Integer productId) {
        return imageRepository.getImagesByProductId(productId);
    }

    @Override
    public Map<Integer, String> getPrimaryImageIdsByProductIds(List<Integer> productIds) {
        return imageRepository.getPrimaryImagesByProductIds(productIds);
    }

    @Override
    public void deleteImagesByProductId(Integer productId) {
        imageRepository.deleteImagesByProductId(productId);
    }

    @Override
    public boolean isImageExist(Integer productId) {
        return imageRepository.isImageExist(productId);
    }
}
