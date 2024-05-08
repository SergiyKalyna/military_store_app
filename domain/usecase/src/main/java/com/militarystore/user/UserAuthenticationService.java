package com.militarystore.user;

import com.militarystore.entity.user.User;
import com.militarystore.exception.MsNotFoundException;
import com.militarystore.exception.MsValidationException;
import com.militarystore.port.in.user.UserAuthenticationUseCase;
import com.militarystore.port.out.user.UserPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserAuthenticationService implements UserAuthenticationUseCase {

    private final UserPort userPort;

    public User getUserByLogin(String login) {
        checkIfLoginExist(login);
        log.info("Fetching user by login [{}]", login);

        return userPort.getUserByLogin(login);
    }

    private void checkIfLoginExist(String login) {
        var isLoginExists = userPort.isLoginExists(login);
        if (!isLoginExists) {
            throw new MsNotFoundException("User with login [" + login + "] doesn't exists");
        }
    }
}
