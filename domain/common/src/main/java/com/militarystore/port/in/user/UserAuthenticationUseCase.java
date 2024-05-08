package com.militarystore.port.in.user;

import com.militarystore.entity.user.User;

public interface UserAuthenticationUseCase {

    User getUserByLogin(String login);
}
