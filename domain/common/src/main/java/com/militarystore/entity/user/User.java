package com.militarystore.entity.user;

import com.militarystore.entity.user.model.Gender;
import com.militarystore.entity.user.model.Role;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record User(
    Integer id,
    String login,
    String password,
    String firstName,
    String secondName,
    String email,
    String phone,
    Gender gender,
    LocalDate birthdayDate,
    Role role,
    boolean isBanned
) {
}
