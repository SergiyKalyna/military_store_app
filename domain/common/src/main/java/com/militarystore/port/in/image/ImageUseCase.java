package com.militarystore.port.in.image;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ImageUseCase {

    void saveProductImages(Integer productId, List<MultipartFile> images);

    List<byte[]> downloadProductImages(Integer productId);

    Map<Integer, byte[]> getPrimaryProductsImages(List<Integer> productIds);

    void deleteProductImages(Integer productId);
}