package com.militarystore.user.mapper;

import com.militarystore.entity.user.User;
import com.militarystore.entity.user.model.Gender;
import com.militarystore.entity.user.model.Role;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.stereotype.Component;

import static com.militarystore.jooq.Tables.USERS;
import static java.util.Objects.isNull;

@Component
public class UserMapper implements RecordMapper<Record, User> {

    @Override
    public User map(Record userRecord) {
        if (isNull(userRecord)) {
            return null;
        }

        return User.builder()
            .id(userRecord.get(USERS.ID))
            .login(userRecord.get(USERS.LOGIN))
            .password(userRecord.get(USERS.PASSWORD))
            .firstName(userRecord.get(USERS.FIRST_NAME))
            .secondName(userRecord.get(USERS.SECOND_NAME))
            .email(userRecord.get(USERS.EMAIL))
            .phone(userRecord.get(USERS.PHONE))
            .gender(Gender.valueOf(userRecord.get(USERS.GENDER)))
            .role(Role.valueOf(userRecord.get(USERS.ROLE)))
            .birthdayDate(userRecord.get(USERS.BIRTHDAY_DATE))
            .isBanned(userRecord.get(USERS.IS_BANNED))
            .build();
    }

    public User mapToUserForEmail(Record userRecord) {
        if (isNull(userRecord)) {
            return null;
        }

        return User.builder()
            .firstName(userRecord.get(USERS.FIRST_NAME))
            .secondName(userRecord.get(USERS.SECOND_NAME))
            .email(userRecord.get(USERS.EMAIL))
            .build();
    }
}
