package com.militarystore.user;

import com.militarystore.converter.user.UserConverter;
import com.militarystore.model.dto.user.UserDto;
import com.militarystore.port.in.user.DeleteUserUseCase;
import com.militarystore.port.in.user.GetUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final GetUserUseCase getUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final UserConverter userConverter;

    @GetMapping
    public List<UserDto> getUsers() {
        var users = getUserUseCase.getUsers();

        return users.stream()
            .map(userConverter::convertToUserDto)
            .toList();
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable("userId") Integer userId) {
        var user = getUserUseCase.getUserById(userId);

        return userConverter.convertToUserDto(user);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") Integer userId) {
        deleteUserUseCase.deleteUser(userId);
    }
}
