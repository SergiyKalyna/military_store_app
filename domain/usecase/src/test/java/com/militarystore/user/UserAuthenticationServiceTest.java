package com.militarystore.user;

import com.militarystore.entity.user.User;
import com.militarystore.exception.MsNotFoundException;
import com.militarystore.port.out.user.UserPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserAuthenticationServiceTest {

    @Mock
    private UserPort userPort;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private UserAuthenticationService userAuthenticationService;

    @BeforeEach
    void setUp() {
        userAuthenticationService = new UserAuthenticationService(userPort, bCryptPasswordEncoder);
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

    @Test
    void isPasswordMatches() {
        var password = "password";
        var encodedPassword = "$2a$10$1Q7Zz1Q6";

        when(bCryptPasswordEncoder.matches(password, encodedPassword)).thenReturn(true);

        assertTrue(userAuthenticationService.isPasswordMatches(password, encodedPassword));
    }
}