package com.militarystore.port.in.user;

import com.militarystore.entity.user.User;

public interface CreateUserUseCase {

    Integer saveUser(User user);
}
