package com.militarystore.port.in.user;

import com.militarystore.entity.user.User;
import com.militarystore.entity.user.model.Role;

public interface UpdateUserUseCase {

    void updateUser(User user);

    void changePassword(int userId, String oldPassword, String newPassword, String confirmationPassword);

    void changeRole(int userId, Role role);

    void changeBanStatus(int userId, boolean isBanned);
}
