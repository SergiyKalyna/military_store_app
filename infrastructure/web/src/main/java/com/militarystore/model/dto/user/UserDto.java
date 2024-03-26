package com.militarystore.model.dto.user;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record UserDto(
    Integer id,
    String login,
    String firstName,
    String secondName,
    String email,
    String phone,
    GenderDto gender,
    LocalDate birthdayDate,
    RoleDto role,
    boolean isBanned
) {
}
