package com.militarystore.entity.image;

import lombok.Builder;

@Builder
public record ProductImage(
    Integer id,
    Integer productId,
    Integer ordinalNumber,
    String googleDriveId
) {
}