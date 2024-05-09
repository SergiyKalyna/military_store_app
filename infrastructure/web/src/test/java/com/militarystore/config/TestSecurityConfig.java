package com.militarystore.config;

import com.militarystore.port.in.user.UserAuthenticationUseCase;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

@Import(WebSecurityConfig.class)
public class TestSecurityConfig {

    @MockBean
    private UserAuthenticationUseCase userAuthenticationUseCase;
}
