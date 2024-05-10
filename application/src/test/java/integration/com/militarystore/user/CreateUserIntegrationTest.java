package com.militarystore.user;

import com.militarystore.IntegrationTest;
import com.militarystore.entity.user.User;
import com.militarystore.entity.user.model.Gender;
import com.militarystore.jooq.tables.records.UsersRecord;
import com.militarystore.port.in.user.CreateUserUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static com.militarystore.jooq.Tables.USERS;
import static org.assertj.core.api.Assertions.assertThat;

class CreateUserIntegrationTest extends IntegrationTest {

    @Autowired
    private CreateUserUseCase createUserUseCase;

    @Test
    void saveUser_userShouldBeProperlySavedToDb() {
        var user = User.builder()
            .login("login")
            .password("password")
            .firstName("firstName")
            .secondName("secondName")
            .email("email@gmail.com")
            .phone("+(380)935334711")
            .gender(Gender.FEMALE)
            .birthdayDate(LocalDate.of(2000, 1, 1))
            .build();

        var userId = createUserUseCase.saveUser(user);
        var userFromDb = getUserFromDb(userId);

        assertThat(userId).isNotNull();
        assertThat(userFromDb.get(USERS.LOGIN)).isEqualTo("login");
        assertThat(userFromDb.get(USERS.FIRST_NAME)).isEqualTo("firstName");
        assertThat(userFromDb.get(USERS.SECOND_NAME)).isEqualTo("secondName");
        assertThat(userFromDb.get(USERS.EMAIL)).isEqualTo("email@gmail.com");
        assertThat(userFromDb.get(USERS.PHONE)).isEqualTo("+(380)935334711");
        assertThat(userFromDb.get(USERS.ROLE)).isEqualTo("USER");
        assertThat(userFromDb.get(USERS.GENDER)).isEqualTo("FEMALE");
        assertThat(userFromDb.get(USERS.BIRTHDAY_DATE)).isEqualTo(LocalDate.of(2000, 1, 1));
        assertThat(userFromDb.get(USERS.IS_BANNED)).isFalse();
    }

    private UsersRecord getUserFromDb(int userId) {
        return dslContext.selectFrom(USERS)
            .where(USERS.ID.eq(userId))
            .fetchOne();
    }
}
