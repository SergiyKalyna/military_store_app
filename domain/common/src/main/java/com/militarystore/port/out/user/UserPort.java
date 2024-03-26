package com.militarystore.port.out.user;

import com.militarystore.entity.user.User;
import com.militarystore.entity.user.model.Role;

import java.util.List;

public interface UserPort {

    Integer saveUser(User user);

    void updateUser(User user);

    void deleteUser(int id);

    User getUserById(int userId);

    List<User> getUsers();

    void changePassword(int id, String password);

    void changeRole(int id, Role role);

    void changeBanStatus(int id, boolean isBanned);

    boolean isLoginExists(String login);

    boolean isUserExist(int id);

    String getUserPassword(int id);
}
