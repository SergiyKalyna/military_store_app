package com.militarystore.user;

import com.militarystore.entity.user.User;
import com.militarystore.entity.user.model.Role;
import com.militarystore.exception.MsNotFoundException;
import com.militarystore.exception.WrongPasswordException;
import com.militarystore.port.in.user.UpdateUserUseCase;
import com.militarystore.port.out.user.UserPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateUserProfileService implements UpdateUserUseCase {

    private final UserPort userPort;
    private final UserValidationService userValidationService;
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void updateUser(User user) {
        userService.checkIfUserExist(user.id());
        userValidationService.validateUserToUpdate(user);

        userPort.updateUser(user);
        log.info("User with id '{}' was updated", user.id());
    }

    public void changePassword(int userId, String oldPassword, String newPassword, String confirmationPassword) {
        validatePasswords(newPassword, confirmationPassword);

        var databasePassword = getDataBasePassword(userId);
        matchPasswords(oldPassword, databasePassword);

        var encodedNewPassword = bCryptPasswordEncoder.encode(newPassword);
        userPort.changePassword(userId, encodedNewPassword);
        log.info("Password for user with id '{}' was changed", userId);
    }

    public void changeRole(int userId, Role role) {
        userService.checkIfUserExist(userId);

        userPort.changeRole(userId, role);
        log.info("Role for user with id '{}' was changed to '{}'", userId, role);
    }

    public void changeBanStatus(int userId, boolean isBanned) {
        userService.checkIfUserExist(userId);

        userPort.changeBanStatus(userId, isBanned);
        log.info("Ban status for user with id '{}' became '{}'", userId, isBanned);
    }

    private void validatePasswords(String newPassword, String confirmationPassword) {
        if (!newPassword.equals(confirmationPassword)) {
            throw new WrongPasswordException("New password and confirmation password are not equals");
        }

        userValidationService.validatePassword(newPassword);
    }

    private String getDataBasePassword(int userId) {
        var databasePassword = userPort.getUserPassword(userId);
        if (isNull(databasePassword)) {
            throw new MsNotFoundException(String.format("User with id [%d] is not found", userId));
        }

        return databasePassword;
    }

    private void matchPasswords(String oldPassword, String databasePassword) {
        var isPasswordMatches = bCryptPasswordEncoder.matches(oldPassword, databasePassword);

        if (!isPasswordMatches) {
            throw new WrongPasswordException("Old password is incorrect");
        }
    }
}
