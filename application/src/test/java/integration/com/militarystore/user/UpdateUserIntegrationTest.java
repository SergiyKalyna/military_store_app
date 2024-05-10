package com.militarystore.user;

import com.militarystore.IntegrationTest;
import com.militarystore.entity.user.User;
import com.militarystore.entity.user.model.Gender;
import com.militarystore.entity.user.model.Role;
import com.militarystore.port.in.user.CreateUserUseCase;
import com.militarystore.port.in.user.GetUserUseCase;
import com.militarystore.port.in.user.UpdateUserUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class UpdateUserIntegrationTest extends IntegrationTest {

    @Autowired
    private UpdateUserUseCase updateUserUseCase;

    @Autowired
    private CreateUserUseCase createUserUseCase;

    @Autowired
    private GetUserUseCase getUserUseCase;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    void updateUser_userShouldBeProperlyUpdated() {
        var user = buildUser();
        var userId = createUserUseCase.saveUser(user);

        var updatedUser = User.builder()
            .id(userId)
            .login("login")
            .password("password")
            .firstName("newFirstName")
            .secondName("newSecondName")
            .email("new-email@gmail.com")
            .phone("+(380)935334711")
            .gender(Gender.FEMALE)
            .role(Role.USER)
            .birthdayDate(LocalDate.EPOCH)
            .isBanned(false)
            .build();

        updateUserUseCase.updateUser(updatedUser);

        var userFromDb = getUserUseCase.getUserById(userId);
        var expectedUser = User.builder()
            .id(userId)
            .login("login")
            .password(userFromDb.password())
            .firstName("newFirstName")
            .secondName("newSecondName")
            .email("new-email@gmail.com")
            .phone("+(380)935334711")
            .gender(Gender.FEMALE)
            .role(Role.USER)
            .birthdayDate(LocalDate.EPOCH)
            .isBanned(false)
            .build();

        assertThat(userFromDb).isEqualTo(expectedUser);
    }

    @Test
    void updateRole() {
        var user = buildUser();
        var userId = createUserUseCase.saveUser(user);

        updateUserUseCase.changeRole(userId, Role.ADMIN);

        assertThat(getUserUseCase.getUserById(userId).role()).isEqualTo(Role.ADMIN);
    }

    @Test
    void updatePassword() {
        var user = buildUser();
        var userId = createUserUseCase.saveUser(user);

        updateUserUseCase.changePassword(userId, "password", "newPassword", "newPassword");

        var passwordFromDb = getUserUseCase.getUserById(userId).password();

        assertThat(passwordEncoder.matches("newPassword", passwordFromDb)).isTrue();
    }

    @Test
    void updateBanStatus() {
        var user = buildUser();
        var userId = createUserUseCase.saveUser(user);

        updateUserUseCase.changeBanStatus(userId, true);

        assertThat(getUserUseCase.getUserById(userId).isBanned()).isTrue();
    }

    private User buildUser() {
        return User.builder()
            .login("login")
            .password("password")
            .firstName("firstName")
            .secondName("secondName")
            .email("email@gmail.com")
            .phone("+(380)935334711")
            .gender(Gender.OTHER)
            .role(Role.USER)
            .birthdayDate(LocalDate.EPOCH)
            .isBanned(false)
            .build();
    }
}