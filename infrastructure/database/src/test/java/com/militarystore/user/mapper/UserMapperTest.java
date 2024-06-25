package com.militarystore.user.mapper;

import com.militarystore.entity.user.User;
import com.militarystore.entity.user.model.Gender;
import com.militarystore.entity.user.model.Role;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.militarystore.jooq.Tables.USERS;
import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    private static final DSLContext DSL_CONTEXT = DSL.using(SQLDialect.POSTGRES);

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper();
    }

    @Test
    void map_whenRecordIsNull_shouldReturnNull() {
        assertThat(userMapper.map(null)).isNull();
    }

    @Test
    void map_shouldCorrectMapRecord() {
        var userRecord = DSL_CONTEXT.newRecord(USERS.fields());
        userRecord.setValue(USERS.ID, 1);
        userRecord.setValue(USERS.LOGIN, "login");
        userRecord.setValue(USERS.PASSWORD, "password");
        userRecord.setValue(USERS.FIRST_NAME, "first_name");
        userRecord.setValue(USERS.SECOND_NAME, "second_name");
        userRecord.setValue(USERS.EMAIL, "email");
        userRecord.setValue(USERS.PHONE, "phone");
        userRecord.setValue(USERS.GENDER, "MALE");
        userRecord.setValue(USERS.ROLE, "ADMIN");
        userRecord.setValue(USERS.BIRTHDAY_DATE, LocalDate.EPOCH);
        userRecord.setValue(USERS.IS_BANNED, false);

        var expectedUser = User.builder()
            .id(1)
            .login("login")
            .password("password")
            .firstName("first_name")
            .secondName("second_name")
            .email("email")
            .phone("phone")
            .gender(Gender.MALE)
            .role(Role.ADMIN)
            .birthdayDate(LocalDate.EPOCH)
            .isBanned(false)
            .build();

        assertThat(userMapper.map(userRecord)).isEqualTo(expectedUser);
    }

    @Test
    void mapToUserForEmail_whenRecordIsNull_shouldReturnNull() {
        assertThat(userMapper.mapToUserForEmail(null)).isNull();
    }

    @Test
    void mapToUserForEmail_shouldCorrectMapRecord() {
        var userRecord = DSL_CONTEXT.newRecord(USERS.fields());
        userRecord.setValue(USERS.FIRST_NAME, "first_name");
        userRecord.setValue(USERS.SECOND_NAME, "second_name");
        userRecord.setValue(USERS.EMAIL, "email");

        var expectedUser = User.builder()
            .firstName("first_name")
            .secondName("second_name")
            .email("email")
            .build();

        assertThat(userMapper.mapToUserForEmail(userRecord)).isEqualTo(expectedUser);
    }
}