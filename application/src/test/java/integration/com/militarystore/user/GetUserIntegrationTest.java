package com.militarystore.user;

import com.militarystore.IntegrationTest;
import com.militarystore.entity.user.User;
import com.militarystore.entity.user.model.Gender;
import com.militarystore.entity.user.model.Role;
import com.militarystore.exception.UserNotFoundException;
import com.militarystore.port.in.user.GetUserUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static com.militarystore.jooq.Tables.USERS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GetUserIntegrationTest extends IntegrationTest {

    @Autowired
    private GetUserUseCase getUserUseCase;

    @Test
    void getUser_userShouldBeProperlyReturned() {
        var user = User.builder()
            .id(1)
            .login("johny_iva")
            .password("password")
            .firstName("firstName")
            .secondName("secondName")
            .email("john@ukr.net")
            .phone("+(380)935334711")
            .gender(Gender.MALE)
            .birthdayDate(LocalDate.of(2000, 1, 1))
            .role(Role.ADMIN)
            .isBanned(false)
            .build();

        var userId = createUser(user);

        assertThat(getUserUseCase.getUserById(userId)).isEqualTo(user);
    }

    @Test
    void getUser_whenUserNotFound_shouldThrowUserNotFoundException() {
        assertThatThrownBy(() -> getUserUseCase.getUserById(1))
            .isInstanceOf(UserNotFoundException.class)
            .hasMessage("User with id [1] is not found");
    }

    @Test
    void getUsers_shouldReturnAllUsers() {
        assertThat(getUserUseCase.getUsers()).isEmpty();

        var user1 = User.builder()
            .id(1)
            .login("johny_iva")
            .password("password")
            .firstName("firstName")
            .secondName("secondName")
            .email("email")
            .gender(Gender.MALE)
            .phone("+(380)935334711")
            .role(Role.USER)
            .birthdayDate(LocalDate.of(2000, 1, 1))
            .isBanned(true)
            .build();

        var user2 = User.builder()
            .id(2)
            .login("johny_dima")
            .password("super_password")
            .firstName("firstName2")
            .secondName("secondName2")
            .email("email2")
            .gender(Gender.OTHER)
            .phone("+(380)935311711")
            .isBanned(false)
            .birthdayDate(LocalDate.of(2011, 11, 11))
            .role(Role.ADMIN)
            .build();

        createUser(user1);
        createUser(user2);

        assertThat(getUserUseCase.getUsers()).isEqualTo(List.of(user1, user2));
    }

    private Integer createUser(User user) {
        return dslContext.insertInto(USERS)
            .set(USERS.ID, user.id())
            .set(USERS.LOGIN, user.login())
            .set(USERS.PASSWORD, user.password())
            .set(USERS.FIRST_NAME, user.firstName())
            .set(USERS.SECOND_NAME, user.secondName())
            .set(USERS.EMAIL, user.email())
            .set(USERS.PHONE, user.phone())
            .set(USERS.GENDER, user.gender().name())
            .set(USERS.BIRTHDAY_DATE, user.birthdayDate())
            .set(USERS.ROLE, user.role().name())
            .set(USERS.IS_BANNED, user.isBanned())
            .returningResult(USERS.ID)
            .fetchOne(USERS.ID);
    }
}
