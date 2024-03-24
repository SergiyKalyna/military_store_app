package com.militarystore.user;

import com.militarystore.entity.user.User;
import com.militarystore.entity.user.model.Role;
import com.militarystore.jooq.tables.records.UsersRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.militarystore.jooq.Tables.USERS;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final DSLContext dslContext;

    public Integer saveUser(User user) {
        return dslContext.insertInto(USERS)
            .set(USERS.LOGIN, user.login())
            .set(USERS.PASSWORD, user.password())
            .set(USERS.FIRST_NAME, user.firstName())
            .set(USERS.SECOND_NAME, user.secondName())
            .set(USERS.EMAIL, user.email())
            .set(USERS.PHONE, user.phone())
            .set(USERS.GENDER, user.gender().name())
            .set(USERS.BIRTHDAY_DATE, user.birthdayDate())
            .returningResult(USERS.ID)
            .fetchOne(USERS.ID);
    }

    public void updateUser(User user) {
        dslContext.update(USERS)
            .set(USERS.FIRST_NAME, user.firstName())
            .set(USERS.SECOND_NAME, user.secondName())
            .set(USERS.EMAIL, user.email())
            .set(USERS.PHONE, user.phone())
            .set(USERS.GENDER, user.gender().name())
            .set(USERS.BIRTHDAY_DATE, user.birthdayDate())
            .where(USERS.ID.eq(user.id()))
            .execute();
    }

    public void deleteUser(int id) {
        dslContext.deleteFrom(USERS)
            .where(USERS.ID.eq(id))
            .execute();
    }

    public UsersRecord getUserById(int userId) {
        return dslContext.selectFrom(USERS)
            .where(USERS.ID.eq(userId))
            .fetchOne();
    }

    public List<UsersRecord> getUsers() {
        return dslContext.selectFrom(USERS)
            .fetch();
    }

    public void changePassword(int id, String password) {
        dslContext.update(USERS)
            .set(USERS.PASSWORD, password)
            .where(USERS.ID.eq(id))
            .execute();
    }

    public String getPassword(int id) {
        return dslContext.select(USERS.PASSWORD)
            .from(USERS)
            .where(USERS.ID.eq(id))
            .fetchOne(USERS.PASSWORD);
    }

    public void changeRole(int id, Role role) {
        dslContext.update(USERS)
            .set(USERS.ROLE, role.name())
            .where(USERS.ID.eq(id))
            .execute();
    }

    public void changeBanStatus(int id, boolean isBanned) {
        dslContext.update(USERS)
            .set(USERS.IS_BANNED, isBanned)
            .where(USERS.ID.eq(id))
            .execute();
    }

    public boolean isLoginExists(String login) {
        return dslContext.fetchExists(dslContext.selectFrom(USERS)
                .where(USERS.LOGIN.eq(login))
        );
    }

    public boolean isUserExist(int id) {
        return dslContext.fetchExists(dslContext.selectFrom(USERS)
                .where(USERS.ID.eq(id))
        );
    }
}
