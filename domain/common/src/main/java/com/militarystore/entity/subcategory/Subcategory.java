package com.militarystore.entity.subcategory;

import lombok.Builder;

@Builder
public record Subcategory(Integer id, String name, Integer categoryId) {
}
