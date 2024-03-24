package com.militarystore.user;

import com.militarystore.entity.user.User;
import com.militarystore.entity.user.model.Role;
import com.militarystore.port.out.user.UserPort;
import com.militarystore.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserAdapter implements UserPort {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public Integer saveUser(User user) {
        return userRepository.saveUser(user);
    }

    public void updateUser(User user) {
        userRepository.updateUser(user);
    }

    public void deleteUser(int id) {
        userRepository.deleteUser(id);
    }

    public User getUserById(int userId) {
        return userMapper.map(userRepository.getUserById(userId));
    }

    public List<User> getUsers() {
        return userRepository.getUsers().stream()
            .map(userMapper::map)
            .toList();
    }

    public void changePassword(int id, String password) {
        userRepository.changePassword(id, password);
    }

    public void changeRole(int id, Role role) {
        userRepository.changeRole(id, role);
    }

    public void changeBanStatus(int id, boolean isBanned) {
        userRepository.changeBanStatus(id, isBanned);
    }

    public boolean isLoginExists(String login) {
        return userRepository.isLoginExists(login);
    }

    public boolean isUserExist(int id) {
        return userRepository.isUserExist(id);
    }

    public String getUserPassword(int id) {
        return userRepository.getPassword(id);
    }
}
