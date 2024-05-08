package com.militarystore.auth;

import com.militarystore.entity.user.User;
import com.militarystore.entity.user.model.Role;
import com.militarystore.exception.WrongPasswordException;
import com.militarystore.port.in.user.UserAuthenticationUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginPasswordAuthenticationProviderTest {

    @Mock
    private UserAuthenticationUseCase userAuthenticationUseCase;

    private LoginPasswordAuthenticationProvider loginPasswordAuthenticationProvider;

    @BeforeEach
    void setUp() {
        loginPasswordAuthenticationProvider = new LoginPasswordAuthenticationProvider(userAuthenticationUseCase);
    }

    @Test
    void authenticate_whenUserInputtedPasswordDoesNotMatchWithExisted_shouldThrowException() {
        var authenticationToken = new UsernamePasswordAuthenticationToken("login", "password");
        var user = User.builder().password("encodedPassword").build();

        when(userAuthenticationUseCase.getUserByLogin("login")).thenReturn(user);
        when(userAuthenticationUseCase.isPasswordMatches("password", "encodedPassword"))
            .thenReturn(false);

        assertThrows(WrongPasswordException.class, () -> loginPasswordAuthenticationProvider.authenticate(authenticationToken));
    }

    @Test
    void authenticate_whenUserInputtedPasswordMatchesWithExisted_shouldReturnAuthenticationToken() {
        var authenticationToken = new UsernamePasswordAuthenticationToken("login", "password");
        var user = User.builder().login("login").password("encodedPassword").role(Role.USER).build();

        when(userAuthenticationUseCase.getUserByLogin("login")).thenReturn(user);
        when(userAuthenticationUseCase.isPasswordMatches("password", "encodedPassword"))
            .thenReturn(true);

        var expectedAuthenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        assertThat(loginPasswordAuthenticationProvider.authenticate(authenticationToken)).isEqualTo(expectedAuthenticationToken);
    }

    @Test
    void supports() {
        assertThat(loginPasswordAuthenticationProvider.supports(UsernamePasswordAuthenticationToken.class)).isTrue();
    }
}