package com.militarystore.user;

import com.militarystore.entity.user.User;
import com.militarystore.exception.MsNotFoundException;
import com.militarystore.port.out.user.UserPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserAuthenticationServiceTest {

    @Mock
    private UserPort userPort;

    private UserAuthenticationService userAuthenticationService;

    @BeforeEach
    void setUp() {
        userAuthenticationService = new UserAuthenticationService(userPort);
    }

    @Test
    void getUserByLogin_whenUserExistsWithThisLogin() {
        var login = "login";
        var user = User.builder().login(login).build();

        when(userPort.isLoginExists(login)).thenReturn(true);
        when(userPort.getUserByLogin(login)).thenReturn(user);

        assertThat(userAuthenticationService.getUserByLogin(login)).isEqualTo(user);
    }

    @Test
    void getUserByLogin_whenUserDoesNotExistWithThisLogin() {
        var login = "login";

        when(userPort.isLoginExists(login)).thenReturn(false);

        assertThrows(MsNotFoundException.class, () -> userAuthenticationService.getUserByLogin(login));
    }
}