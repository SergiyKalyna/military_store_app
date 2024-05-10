package com.militarystore.user;

import com.militarystore.converter.user.UserConverter;
import com.militarystore.entity.user.User;
import com.militarystore.entity.user.model.Role;
import com.militarystore.model.dto.user.RoleDto;
import com.militarystore.model.request.user.UserUpdatePasswordRequest;
import com.militarystore.model.request.user.UserUpdateRequest;
import com.militarystore.port.in.user.UpdateUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile/user")
@RequiredArgsConstructor
public class UpdateUserProfileController {

    private final UpdateUserUseCase updateUserUseCase;
    private final UserConverter userConverter;

    @PutMapping
    public void updateUser(@AuthenticationPrincipal User userDetails, @RequestBody UserUpdateRequest updateRequest) {
        var user = userConverter.convertToUser(updateRequest, userDetails.id());

        updateUserUseCase.updateUser(user);
    }

    @PutMapping("/password")
    public void changePassword(@AuthenticationPrincipal User userDetails, @RequestBody UserUpdatePasswordRequest request) {
        updateUserUseCase.changePassword(userDetails.id(), request.oldPassword(), request.newPassword(), request.confirmationPassword());
    }

    @PutMapping("/{userId}/role")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public void changeRole(@PathVariable("userId") Integer userId, @RequestParam("roleDto") RoleDto roleDto) {
        var role = Role.valueOf(roleDto.name());

        updateUserUseCase.changeRole(userId, role);
    }

    @PutMapping("/{userId}/ban-status")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public void changeBanStatus(@PathVariable("userId") Integer userId, @RequestParam("isBanned") Boolean isBanned) {
        updateUserUseCase.changeBanStatus(userId, isBanned);
    }
}
