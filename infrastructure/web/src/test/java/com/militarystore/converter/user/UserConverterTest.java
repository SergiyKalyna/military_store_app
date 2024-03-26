package com.militarystore.converter.user;

import com.militarystore.entity.user.User;
import com.militarystore.entity.user.model.Gender;
import com.militarystore.entity.user.model.Role;
import com.militarystore.model.dto.user.GenderDto;
import com.militarystore.model.dto.user.RoleDto;
import com.militarystore.model.dto.user.UserDto;
import com.militarystore.model.request.user.UserRegistrationRequest;
import com.militarystore.model.request.user.UserUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class UserConverterTest {

    private UserConverter userConverter;

    @BeforeEach
    void setUp() {
        userConverter = new UserConverter();
    }

    @Test
    void convertToUser() {
        var userRegistrationRequest = new UserRegistrationRequest(
            "login",
            "password",
            "firstName",
            "secondName",
            "email",
            "phone",
            GenderDto.MALE,
            LocalDate.EPOCH
        );

        var expectedUser = User.builder()
            .login("login")
            .password("password")
            .firstName("firstName")
            .secondName("secondName")
            .email("email")
            .phone("phone")
            .gender(Gender.MALE)
            .birthdayDate(LocalDate.EPOCH)
            .build();

        assertThat(userConverter.convertToUser(userRegistrationRequest)).isEqualTo(expectedUser);
    }

    @Test
    void convertToUserDto() {
        var user = User.builder()
            .id(1)
            .login("login")
            .password("password")
            .firstName("firstName")
            .secondName("secondName")
            .email("email")
            .phone("phone")
            .gender(Gender.FEMALE)
            .birthdayDate(LocalDate.EPOCH)
            .role(Role.USER)
            .isBanned(false)
            .build();

        var expectedUserDto = UserDto.builder()
            .id(1)
            .login("login")
            .firstName("firstName")
            .secondName("secondName")
            .email("email")
            .phone("phone")
            .gender(GenderDto.FEMALE)
            .birthdayDate(LocalDate.EPOCH)
            .role(RoleDto.USER)
            .isBanned(false)
            .build();

        assertThat(userConverter.convertToUserDto(user)).isEqualTo(expectedUserDto);
    }

    @Test
    void testConvertToUser() {
        var userUpdateRequest = new UserUpdateRequest(
            "firstName",
            "secondName",
            "email",
            "phone",
            GenderDto.OTHER,
            LocalDate.EPOCH
        );

        var expectedUser = User.builder()
            .id(1)
            .firstName("firstName")
            .secondName("secondName")
            .email("email")
            .phone("phone")
            .gender(Gender.OTHER)
            .birthdayDate(LocalDate.EPOCH)
            .build();

        assertThat(userConverter.convertToUser(userUpdateRequest, 1)).isEqualTo(expectedUser);
    }

    @ParameterizedTest
    @EnumSource(GenderDto.class)
    void testConvertToGender(GenderDto genderDto) {
        assertDoesNotThrow(() -> Gender.valueOf(genderDto.name()));
    }

    @ParameterizedTest
    @EnumSource(Gender.class)
    void testConvertToGenderDto(Gender gender) {
        assertDoesNotThrow(() -> GenderDto.valueOf(gender.name()));
    }

    @ParameterizedTest
    @EnumSource(RoleDto.class)
    void testConvertToRole(RoleDto roleDto) {
        assertDoesNotThrow(() -> Role.valueOf(roleDto.name()));
    }

    @ParameterizedTest
    @EnumSource(Role.class)
    void testConvertToRoleDto(Role role) {
        assertDoesNotThrow(() -> RoleDto.valueOf(role.name()));
    }
}