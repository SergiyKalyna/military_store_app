package com.militarystore.user;

import com.militarystore.IntegrationTest;
import com.militarystore.entity.user.User;
import com.militarystore.entity.user.model.Gender;
import com.militarystore.entity.user.model.Role;
import com.militarystore.exception.MsNotFoundException;
import com.militarystore.port.in.user.UserAuthenticationUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;

import static com.militarystore.jooq.Tables.USERS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserAuthenticationIntegrationTest extends IntegrationTest {

    private static final int USER_ID = 1;
    private static final String LOGIN = "login";

    @Autowired
    private UserAuthenticationUseCase userAuthenticationUseCase;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    void getUserByLogin() {
        initializeUser();

        var user = userAuthenticationUseCase.getUserByLogin(LOGIN);

        var expectedUser = User.builder()
            .id(USER_ID)
            .login(LOGIN)
            .password("password")
            .firstName("firstName")
            .secondName("secondName")
            .email("email")
            .phone("+(380)935334711")
            .gender(Gender.FEMALE)
            .role(Role.USER)
            .birthdayDate(LocalDate.EPOCH)
            .isBanned(false)
            .build();

        assertThat(user).isEqualTo(expectedUser);
    }

    @Test
    void getUserByLogin_whenUserDoesNotExist() {
        assertThrows(MsNotFoundException.class, () -> userAuthenticationUseCase.getUserByLogin(LOGIN));
    }

    @Test
    void isPasswordMatches() {
        var password = "password";
        var encodedPassword = passwordEncoder.encode(password);

        assertThat(userAuthenticationUseCase.isPasswordMatches(password, encodedPassword)).isTrue();
    }

    @Test
    void isPasswordMatches_whenPasswordsDoNotMatch() {
        var password = "password";
        var encodedPassword = passwordEncoder.encode("anotherPassword");

        assertThat(userAuthenticationUseCase.isPasswordMatches(password, encodedPassword)).isFalse();
    }

    private void initializeUser() {
        dslContext.insertInto(USERS)
            .set(USERS.ID, USER_ID)
            .set(USERS.LOGIN, LOGIN)
            .set(USERS.PASSWORD, "password")
            .set(USERS.EMAIL, "email")
            .set(USERS.FIRST_NAME, "firstName")
            .set(USERS.SECOND_NAME, "secondName")
            .set(USERS.PHONE, "+(380)935334711")
            .set(USERS.BIRTHDAY_DATE, LocalDate.EPOCH)
            .set(USERS.ROLE, Role.USER.name())
            .set(USERS.GENDER, Gender.FEMALE.name())
            .set(USERS.IS_BANNED, false)
            .execute();
    }
}
