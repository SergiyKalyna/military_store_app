package com.militarystore.user;

import com.militarystore.entity.user.User;
import com.militarystore.exception.UserNotFoundException;
import com.militarystore.exception.MsValidationException;
import com.militarystore.port.out.user.UserPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private static final int USER_ID = 1;
    private static final User USER = User.builder().id(USER_ID).login("login").build();

    @Mock
    private UserValidationService userValidationService;

    @Mock
    private UserPort userPort;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userPort, userValidationService);
    }

    @Test
    void saveUser_whenLoginAlreadyExist_shouldTrowException() {
        when(userPort.isLoginExists("login")).thenReturn(true);

        assertThatCode(() -> userService.saveUser(USER))
            .isInstanceOf(MsValidationException.class)
            .hasMessageContaining("User with login [login] is already exist");
    }

    @Test
    void saveUser_whenUserIsInvalid_shouldTrowException() {
        when(userPort.isLoginExists("login")).thenReturn(false);
        doThrow(MsValidationException.class).when(userValidationService).validateNewUser(USER);

        assertThatCode(() -> userService.saveUser(USER))
            .isInstanceOf(MsValidationException.class);
    }

    @Test
    void saveUser_whenUserIsValid_shouldSaveUser() {
        when(userPort.isLoginExists("login")).thenReturn(false);
        when(userPort.saveUser(USER)).thenReturn(USER_ID);

        var userId = userService.saveUser(USER);

        assertThat(userId).isEqualTo(USER_ID);
    }

    @Test
    void deleteUser() {
        when(userPort.isUserExist(USER_ID)).thenReturn(true);

        userService.deleteUser(USER_ID);

        verify(userPort).deleteUser(USER_ID);
    }

    @Test
    void deleteUser_whenUserDoesntExist_shouldThrowException() {
        when(userPort.isUserExist(USER_ID)).thenReturn(false);

        assertThatCode(() -> userService.deleteUser(USER_ID))
            .isInstanceOf(UserNotFoundException.class)
            .hasMessageContaining("User with id [" + USER_ID + "] is not found");
    }

    @Test
    void getUserById() {
        when(userPort.getUserById(USER_ID)).thenReturn(USER);

        var user = userService.getUserById(USER_ID);

        assertThat(user).isEqualTo(USER);
    }

    @Test
    void getUserById_whenUserDoesntExist_shouldThrowException() {
        when(userPort.getUserById(USER_ID)).thenReturn(null);

        assertThatCode(() -> userService.getUserById(USER_ID))
            .isInstanceOf(UserNotFoundException.class)
            .hasMessageContaining("User with id [" + USER_ID + "] is not found");
    }

    @Test
    void getUsers() {
        var users = List.of(USER);

        when(userPort.getUsers()).thenReturn(users);

        assertThat(userService.getUsers()).isEqualTo(users);
    }

    @Test
    void getUsers_whenReturnsEmptyList() {
        List<User> users = List.of();

        when(userPort.getUsers()).thenReturn(users);

        assertThat(userService.getUsers()).isEqualTo(users);
    }
}