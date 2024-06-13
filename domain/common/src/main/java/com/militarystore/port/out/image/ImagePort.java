package com.militarystore.port.out.image;

import com.militarystore.entity.image.ProductImage;

import java.util.List;
import java.util.Map;

public interface ImagePort {

    void saveImages(List<ProductImage> productImages);

    List<String> getImageIdsByProductId(Integer productId);

    Map<Integer, String> getPrimaryImageIdsByProductIds(List<Integer> productIds);

    void deleteImagesByProductId(Integer productId);

    boolean isImageExist(Integer productId);
}
