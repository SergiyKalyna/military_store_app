package com.militarystore.model.dto.category;

import lombok.Builder;

@Builder
public record SubcategoryDto(Integer id, String name, Integer categoryId) {
}
