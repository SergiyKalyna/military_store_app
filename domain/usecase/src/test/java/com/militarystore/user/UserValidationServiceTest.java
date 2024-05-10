package com.militarystore.user;

import com.militarystore.entity.user.User;
import com.militarystore.exception.MsValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserValidationServiceTest {

    private UserValidationService userValidationService;

    @BeforeEach
    void setUp() {
        userValidationService = new UserValidationService();
    }

    @Test
    void validateUser_ifUserDataValid_shouldNothingThrow() {
        var user = User.builder()
            .login("login")
            .password("password")
            .firstName("firstName")
            .secondName("secondName")
            .email("user@gmail.com")
            .phone("+(380)675863281")
            .build();

        assertDoesNotThrow(() -> userValidationService.validateNewUser(user));
    }

    @Test
    void validateUser_whenUserLoginIsBlank_shouldThrowUserValidationException() {
        var user = User.builder()
            .login("")
            .build();

        assertThrows(MsValidationException.class, () -> userValidationService.validateNewUser(user));
    }

    @Test
    void validateUser_whenUserLoginIsNull_shouldThrowUserValidationException() {
        var user = User.builder().build();

        assertThrows(MsValidationException.class, () -> userValidationService.validateNewUser(user));
    }

    @Test
    void validateUser_whenUserLoginIncludeOnlySpaces_shouldThrowUserValidationException() {
        var user = User.builder().login("        ").build();

        assertThrows(MsValidationException.class, () -> userValidationService.validateNewUser(user));
    }

    @Test
    void validateUser_whenUserLoginLessThan3Chars_shouldThrowUserValidationException() {
        var user = User.builder().login("ab").build();

        assertThrows(MsValidationException.class, () -> userValidationService.validateNewUser(user));
    }

    @Test
    void validateUser_whenUserPasswordIsBlank_shouldThrowUserValidationException() {
        var user = User.builder()
            .login("login")
            .password("")
            .build();

        assertThrows(MsValidationException.class, () -> userValidationService.validateNewUser(user));
    }

    @Test
    void validateUser_whenUserPasswordIsNull_shouldThrowUserValidationException() {
        var user = User.builder()
            .login("login")
            .build();

        assertThrows(MsValidationException.class, () -> userValidationService.validateNewUser(user));
    }

    @Test
    void validateUser_whenUserPasswordIncludeOnlySpaces_shouldThrowUserValidationException() {
        var user = User.builder()
            .login("login")
            .password("        ")
            .build();

        assertThrows(MsValidationException.class, () -> userValidationService.validateNewUser(user));
    }

    @Test
    void validateUser_whenUserPasswordLessThan6Chars_shouldThrowUserValidationException() {
        var user = User.builder()
            .login("login")
            .password("ab")
            .build();

        assertThrows(MsValidationException.class, () -> userValidationService.validateNewUser(user));
    }

    @Test
    void validateUser_whenUserFirstNameIsBlank_shouldThrowUserValidationException() {
        var user = User.builder()
            .login("login")
            .password("password")
            .firstName("  ")
            .build();

        assertThrows(MsValidationException.class, () -> userValidationService.validateNewUser(user));
    }

    @Test
    void validateUser_whenUserFirstNameIsNull_shouldThrowUserValidationException() {
        var user = User.builder()
            .login("login")
            .password("password")
            .build();

        assertThrows(MsValidationException.class, () -> userValidationService.validateNewUser(user));
    }

    @Test
    void validateUser_whenUserSecondNameIsBlank_shouldThrowUserValidationException() {
        var user = User.builder()
            .login("login")
            .password("password")
            .firstName("firstName")
            .secondName("  ")
            .build();

        assertThrows(MsValidationException.class, () -> userValidationService.validateNewUser(user));
    }

    @Test
    void validateUser_whenUserSecondNameIsNull_shouldThrowUserValidationException() {
        var user = User.builder()
            .login("login")
            .password("password")
            .firstName("firstName")
            .build();

        assertThrows(MsValidationException.class, () -> userValidationService.validateNewUser(user));
    }

    @Test
    void validateUser_whenUserEmailIsBlank_shouldThrowUserValidationException() {
        var user = User.builder()
            .login("login")
            .password("password")
            .firstName("firstName")
            .secondName("secondName")
            .email("  ")
            .build();

        assertThrows(MsValidationException.class, () -> userValidationService.validateNewUser(user));
    }

    @Test
    void validateUser_whenUserEmailIsNull_shouldThrowUserValidationException() {
        var user = User.builder()
            .login("login")
            .password("password")
            .firstName("firstName")
            .secondName("secondName")
            .build();

        assertThrows(MsValidationException.class, () -> userValidationService.validateNewUser(user));
    }

    @Test
    void validateUser_whenUserEmailIsInvalid_shouldThrowUserValidationException() {
        var user = User.builder()
            .login("login")
            .password("password")
            .firstName("firstName")
            .secondName("secondName")
            .email("usergmail.com")
            .build();

        assertThrows(MsValidationException.class, () -> userValidationService.validateNewUser(user));
    }

    @Test
    void validateUser_whenUserPhoneIsBlank_shouldThrowUserValidationException() {
        var user = User.builder()
            .login("login")
            .password("password")
            .firstName("firstName")
            .secondName("secondName")
            .email("user@gmail.com")
            .phone("  ")
            .build();

        assertThrows(MsValidationException.class, () -> userValidationService.validateNewUser(user));
    }

    @Test
    void validateUser_whenUserPhoneIsNull_shouldThrowUserValidationException() {
        var user = User.builder()
            .login("login")
            .password("password")
            .firstName("firstName")
            .secondName("secondName")
            .email("user@gmail.com")
            .build();

        assertThrows(MsValidationException.class, () -> userValidationService.validateNewUser(user));
    }

    @Test
    void validateUser_whenUserPhoneIsInvalid_shouldThrowUserValidationException() {
        var user = User.builder()
            .login("login")
            .password("password")
            .firstName("firstName")
            .secondName("secondName")
            .email("user123@ukr.net")
            .phone("80675863281")
            .build();

        assertThrows(MsValidationException.class, () -> userValidationService.validateNewUser(user));
    }
}