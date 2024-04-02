package com.militarystore.model.dto.error;

import lombok.Builder;

@Builder
public record ErrorDto(Integer code, String error, String reason) {
}
