package com.militarystore.user;

import com.militarystore.IntegrationTest;
import com.militarystore.entity.user.User;
import com.militarystore.entity.user.model.Gender;
import com.militarystore.entity.user.model.Role;
import com.militarystore.exception.MsNotFoundException;
import com.militarystore.port.in.user.DeleteUserUseCase;
import com.militarystore.port.in.user.GetUserUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static com.militarystore.jooq.Tables.USERS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DeleteUserIntegrationTest extends IntegrationTest {

    @Autowired
    private DeleteUserUseCase deleteUserUseCase;

    @Autowired
    private GetUserUseCase getUserUseCase;

    @Test
    void whenUserNotFound_shouldThrowUserNotFoundException() {
        int userId = 1;

        assertThatThrownBy(() -> deleteUserUseCase.deleteUser(userId))
            .isInstanceOf(MsNotFoundException.class)
            .hasMessage("User with id [1] is not found");
    }

    @Test
    void deleteUser() {
        var user = User.builder()
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

        var userId = createUser(user);

        assertThat(getUserUseCase.getUsers()).isNotEmpty();

        deleteUserUseCase.deleteUser(userId);

        assertThat(getUserUseCase.getUsers()).isEmpty();
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
