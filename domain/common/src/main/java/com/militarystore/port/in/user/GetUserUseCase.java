package com.militarystore.port.in.user;

import com.militarystore.entity.user.User;

import java.util.List;

public interface GetUserUseCase {

    User getUserById(int userId);

    List<User> getUsers();
}
