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

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class UpdateUserIntegrationTest extends IntegrationTest {

    @Autowired
    private UpdateUserUseCase updateUserUseCase;

    @Autowired
    private CreateUserUseCase createUserUseCase;

    @Autowired
    private GetUserUseCase getUserUseCase;

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

        assertThat(getUserUseCase.getUserById(userId)).isEqualTo(updatedUser);
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

        assertThat(getUserUseCase.getUserById(userId).password()).isEqualTo("newPassword");
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