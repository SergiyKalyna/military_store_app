package com.militarystore.converter.user;

import com.militarystore.entity.user.User;
import com.militarystore.entity.user.model.Gender;
import com.militarystore.model.dto.user.GenderDto;
import com.militarystore.model.dto.user.RoleDto;
import com.militarystore.model.dto.user.UserDto;
import com.militarystore.model.request.user.UserRegistrationRequest;
import com.militarystore.model.request.user.UserUpdateRequest;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    public User convertToUser(UserRegistrationRequest request) {
        return User.builder()
            .login(request.login())
            .password(request.password())
            .firstName(request.firstName())
            .secondName(request.secondName())
            .email(request.email())
            .phone(request.phone())
            .gender(Gender.valueOf(request.gender().name()))
            .birthdayDate(request.birthdayDate())
            .build();
    }

    public UserDto convertToUserDto(User user) {
        return UserDto.builder()
            .id(user.id())
            .login(user.login())
            .firstName(user.firstName())
            .secondName(user.secondName())
            .email(user.email())
            .phone(user.phone())
            .gender(GenderDto.valueOf(user.gender().name()))
            .birthdayDate(user.birthdayDate())
            .role(RoleDto.valueOf(user.role().name()))
            .isBanned(user.isBanned())
            .build();
    }

    public User convertToUser(UserUpdateRequest request, int userId) {
        return User.builder()
            .id(userId)
            .firstName(request.firstName())
            .secondName(request.secondName())
            .email(request.email())
            .phone(request.phone())
            .gender(Gender.valueOf(request.gender().name()))
            .birthdayDate(request.birthdayDate())
            .build();
    }
}
