package com.militarystore.user;

import com.militarystore.entity.user.User;
import com.militarystore.entity.user.model.Role;
import com.militarystore.jooq.tables.records.UsersRecord;
import com.militarystore.user.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserAdapterTest {
    private static final int USER_ID = 1;
    private static final User USER = User.builder().build();

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    private UserAdapter userAdapter;

    @BeforeEach
    void setUp() {
        userAdapter = new UserAdapter(userRepository, userMapper);
    }

    @Test
    void saveUser() {
        when(userRepository.saveUser(USER)).thenReturn(USER_ID);

        assertThat(userAdapter.saveUser(USER)).isEqualTo(USER_ID);
    }

    @Test
    void updateUser() {
        doNothing().when(userRepository).updateUser(USER);

        userAdapter.updateUser(USER);

        verify(userRepository).updateUser(USER);
    }

    @Test
    void deleteUser() {
        doNothing().when(userRepository).deleteUser(USER_ID);

        userAdapter.deleteUser(USER_ID);

        verify(userRepository).deleteUser(USER_ID);
    }

    @Test
    void getUserById() {
        var userRecord = new UsersRecord();

        when(userRepository.getUserById(USER_ID)).thenReturn(userRecord);
        when(userMapper.map(userRecord)).thenReturn(USER);

        assertThat(userAdapter.getUserById(USER_ID)).isEqualTo(USER);
    }

    @Test
    void getUsers() {
        var userRecord = new UsersRecord();

        when(userRepository.getUsers()).thenReturn(List.of(userRecord));
        when(userMapper.map(userRecord)).thenReturn(USER);

        assertThat(userAdapter.getUsers()).isEqualTo(List.of(USER));
    }

    @Test
    void changePassword() {
        var newPassword = "newPassword";

        doNothing().when(userRepository).changePassword(USER_ID, newPassword);

        userAdapter.changePassword(USER_ID, newPassword);

        verify(userRepository).changePassword(USER_ID, newPassword);
    }

    @Test
    void changeRole() {
        var role = Role.ADMIN;

        doNothing().when(userRepository).changeRole(USER_ID, role);

        userAdapter.changeRole(USER_ID, role);

        verify(userRepository).changeRole(USER_ID, role);
    }

    @Test
    void changeBanStatus() {
        var isBanned = true;

        doNothing().when(userRepository).changeBanStatus(USER_ID, isBanned);

        userAdapter.changeBanStatus(USER_ID, isBanned);

        verify(userRepository).changeBanStatus(USER_ID, isBanned);
    }

    @Test
    void isLoginExists() {
        var login = "login";

        when(userRepository.isLoginExists(login)).thenReturn(false);

        assertThat(userAdapter.isLoginExists(login)).isFalse();
    }

    @Test
    void isUserExist() {
        when(userRepository.isUserExist(USER_ID)).thenReturn(true);

        assertThat(userAdapter.isUserExist(USER_ID)).isTrue();
    }

    @Test
    void getUserPassword() {
        var password = "password";

        when(userRepository.getPassword(USER_ID)).thenReturn(password);

        assertThat(userAdapter.getUserPassword(USER_ID)).isEqualTo(password);
    }

    @Test
    void getUserByLogin() {
        var userRecord = new UsersRecord();

        when(userRepository.getUserByLogin("login")).thenReturn(userRecord);
        when(userMapper.map(userRecord)).thenReturn(USER);

        assertThat(userAdapter.getUserByLogin("login")).isEqualTo(USER);
    }
}