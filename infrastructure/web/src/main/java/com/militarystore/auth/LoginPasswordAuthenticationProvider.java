package com.militarystore.auth;

import com.militarystore.exception.WrongPasswordException;
import com.militarystore.port.in.user.UserAuthenticationUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Primary
public class LoginPasswordAuthenticationProvider implements AuthenticationProvider {

    private final UserAuthenticationUseCase userAuthenticationUseCase;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var login = authentication.getName();
        var password = authentication.getCredentials().toString();
        var user = userAuthenticationUseCase.getUserByLogin(login);

        if (userAuthenticationUseCase.isPasswordMatches(password, user.password())) {
            return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        } else {
            throw new WrongPasswordException("Was input wrong password");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}