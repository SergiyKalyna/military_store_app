package com.militarystore.user;

import com.militarystore.entity.user.User;
import com.militarystore.exception.UserNotFoundException;
import com.militarystore.exception.MsValidationException;
import com.militarystore.port.in.user.CreateUserUseCase;
import com.militarystore.port.in.user.DeleteUserUseCase;
import com.militarystore.port.in.user.GetUserUseCase;
import com.militarystore.port.out.user.UserPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements CreateUserUseCase, GetUserUseCase, DeleteUserUseCase {

    private final UserPort userPort;
    private final UserValidationService userValidationService;

    public Integer saveUser(User user) {
        checkIfLoginExist(user.login());
        userValidationService.validateNewUser(user);

        var userId = userPort.saveUser(user);
        log.info("User with login '{}' was created with id '{}'", user.login(), userId);

        return userId;
    }

    public void deleteUser(int userId) {
        checkIfUserExist(userId);

        userPort.deleteUser(userId);
        log.info("User with id '{}' was deleted", userId);
    }

    public User getUserById(int userId) {
        var user = userPort.getUserById(userId);

        if (isNull(user)) {
            throw new UserNotFoundException(userId);
        }

        return user;
    }

    public List<User> getUsers() {
        return userPort.getUsers();
    }

    void checkIfUserExist(int userId) {
        var isUserExist = userPort.isUserExist(userId);
        if (!isUserExist) {
            throw new UserNotFoundException(userId);
        }
    }

    private void checkIfLoginExist(String login) {
        var isLoginExists = userPort.isLoginExists(login);
        if (isLoginExists) {
            throw new MsValidationException("User with login [" + login + "] is already exists");
        }
    }
}
