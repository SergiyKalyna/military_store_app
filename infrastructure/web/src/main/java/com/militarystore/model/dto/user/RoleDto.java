package com.militarystore.model.dto.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleDto {
    SUPER_ADMIN("Super Admin"),
    ADMIN("Admin"),
    USER("User");

    private final String description;
}
