package com.militarystore.model.dto.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GenderDto {
    MALE("Male"),
    FEMALE("Female"),
    OTHER("Other");

    private final String description;
}
