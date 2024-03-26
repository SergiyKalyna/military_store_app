package com.militarystore.model.request.user;

public record UserUpdatePasswordRequest(
        String oldPassword,
        String newPassword,
        String confirmationPassword
) {
}
