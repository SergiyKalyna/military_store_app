package com.militarystore.entity.category;

import lombok.Builder;

@Builder
public record Category(int id, String name) {
}
