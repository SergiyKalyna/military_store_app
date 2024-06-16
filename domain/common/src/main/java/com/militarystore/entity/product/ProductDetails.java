package com.militarystore.entity.product;

import lombok.Builder;

import java.util.List;

@Builder
public record ProductDetails(
    Product product,
    List<byte[]> images
) {
}