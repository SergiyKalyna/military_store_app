package com.militarystore.entity.subcategory;

import lombok.Builder;

@Builder
public record Subcategory(int id, String name, Integer categoryId) {
}
