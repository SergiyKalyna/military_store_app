package com.militarystore.user;

import com.militarystore.entity.user.User;
import com.militarystore.entity.user.model.Role;
import com.militarystore.exception.MsNotFoundException;
import com.militarystore.exception.MsValidationException;
import com.militarystore.exception.WrongPasswordException;
import com.militarystore.port.out.user.UserPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateUserProfileServiceTest {

    private static final int USER_ID = 1;

    @Mock
    private UserPort userPort;

    @Mock
    private UserValidationService userValidationService;

    @Mock
    private UserService userService;

    private UpdateUserProfileService updateUserProfileService;

    @BeforeEach
    void setUp() {
        updateUserProfileService = new UpdateUserProfileService(userPort, userValidationService, userService);
    }

    @Test
    void updateUser_whenUserDoesntExist_shouldTrowException() {
        var user = User.builder().id(USER_ID).build();

        doThrow(new MsNotFoundException("error message")).when(userService).checkIfUserExist(USER_ID);

        assertThrows(MsNotFoundException.class, () -> updateUserProfileService.updateUser(user));
    }

    @Test
    void update_whenUserIsInvalid_shouldThrowException() {
        var user = User.builder().id(USER_ID).build();

        doNothing().when(userService).checkIfUserExist(USER_ID);
        doThrow(new MsValidationException("Invalid user")).when(userValidationService).validateUserToUpdate(user);

        assertThrows(MsValidationException.class, () -> updateUserProfileService.updateUser(user));
    }

    @Test
    void updateUser_whenUserIsValid_shouldUpdateUser() {
        var user = User.builder().id(USER_ID).build();

        doNothing().when(userService).checkIfUserExist(USER_ID);
        doNothing().when(userValidationService).validateUserToUpdate(user);

        updateUserProfileService.updateUser(user);

        verify(userPort).updateUser(user);
    }

    @Test
    void changePassword_whenNewAndConfirmationPasswordsDifferent_shouldThrowException() {
        assertThrows(
            WrongPasswordException.class,
            () -> updateUserProfileService.changePassword(USER_ID, "old", "new", "confirmation")
        );
    }

    @Test
    void changePassword_whenNewPasswordIsInvalid_shouldThrowException() {
        doThrow(new MsValidationException("Invalid password")).when(userValidationService).validatePassword("new");

        assertThrows(
            MsValidationException.class,
            () -> updateUserProfileService.changePassword(USER_ID, "old", "new", "new")
        );
    }

    @Test
    void changePassword_whenUserDoesntExist_shouldThrowException() {
        when(userPort.getUserPassword(USER_ID)).thenReturn(null);

        assertThrows(
            MsNotFoundException.class,
            () -> updateUserProfileService.changePassword(USER_ID, "old", "new", "new")
        );
    }

    @Test
    void changePassword_whenOldPasswordDoesntMatchDatabasePassword_shouldThrowException() {
        when(userPort.getUserPassword(USER_ID)).thenReturn("databasePassword");

        assertThrows(
            WrongPasswordException.class,
            () -> updateUserProfileService.changePassword(USER_ID, "old", "new", "new")
        );
    }

    @Test
    void changePassword_whenOldPasswordMatchesDatabasePassword_shouldChangePassword() {
        when(userPort.getUserPassword(USER_ID)).thenReturn("old");

        updateUserProfileService.changePassword(USER_ID, "old", "new", "new");

        verify(userPort).changePassword(USER_ID, "new");
    }

    @Test
    void changeRole_whenUserDoesntExist_shouldThrowException() {
        doThrow(new MsNotFoundException("error message")).when(userService).checkIfUserExist(USER_ID);

        assertThrows(
            MsNotFoundException.class,
            () -> updateUserProfileService.changeRole(USER_ID, null)
        );
    }

    @Test
    void changeRole_whenUserExist_shouldChangeRole() {
        doNothing().when(userService).checkIfUserExist(USER_ID);

        updateUserProfileService.changeRole(USER_ID, Role.ADMIN);

        verify(userPort).changeRole(USER_ID, Role.ADMIN);
    }

    @Test
    void changeBanStatus_whenUserDoesntExist_shouldThrowException() {
        doThrow(new MsNotFoundException("error message")).when(userService).checkIfUserExist(USER_ID);

        assertThrows(
            MsNotFoundException.class,
            () -> updateUserProfileService.changeBanStatus(USER_ID, true)
        );
    }

    @Test
    void changeBanStatus_whenUserExist_shouldChangeBanStatus() {
        doNothing().when(userService).checkIfUserExist(USER_ID);

        updateUserProfileService.changeBanStatus(USER_ID, true);

        verify(userPort).changeBanStatus(USER_ID, true);
    }
}