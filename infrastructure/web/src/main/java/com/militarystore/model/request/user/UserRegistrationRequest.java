package com.militarystore.model.request.user;

import com.militarystore.model.dto.user.GenderDto;

import java.time.LocalDate;

public record UserRegistrationRequest(
    String login,
    String password,
    String firstName,
    String secondName,
    String email,
    String phone,
    GenderDto gender,
    LocalDate birthdayDate
) {
}
