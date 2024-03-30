package com.militarystore.model.dto.category;

import lombok.Builder;

@Builder
public record CategoryDto(Integer id, String name) {
}
