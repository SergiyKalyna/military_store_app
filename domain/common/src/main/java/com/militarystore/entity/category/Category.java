package com.militarystore.entity.category;

import lombok.Builder;

@Builder
public record Category(Integer id, String name) {
}
