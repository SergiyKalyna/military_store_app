package com.militarystore.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.militarystore.config.TestSecurityConfig;
import com.militarystore.converter.user.UserConverter;
import com.militarystore.entity.user.User;
import com.militarystore.model.dto.user.UserDto;
import com.militarystore.port.in.user.DeleteUserUseCase;
import com.militarystore.port.in.user.GetUserUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@ContextConfiguration(classes = {UserController.class})
@Import(TestSecurityConfig.class)
class UserControllerTest {

    @MockBean
    private GetUserUseCase getUserUseCase;

    @MockBean
    private DeleteUserUseCase deleteUserUseCase;

    @MockBean
    private UserConverter userConverter;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "ADMIN")
    void getUsers_whenUserHasRoleAdmin_shouldReturnUsers() throws Exception {
        var user = User.builder().id(1).build();
        var users = List.of(user);

        var userDto = UserDto.builder().id(1).build();
        var usersDto = List.of(userDto);

        when(getUserUseCase.getUsers()).thenReturn(users);
        when(userConverter.convertToUserDto(user)).thenReturn(userDto);

        mockMvc.perform(get("/users"))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(usersDto)));
    }

    @Test
    @WithMockUser(roles = "USER")
    void getUsers_whenUserHasRoleUser_shouldReturnForbidden() throws Exception {
        mockMvc.perform(get("/users"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void getUsers_whenUserHasRoleSuperAdmin_shouldReturnUsers() throws Exception {
        var user = User.builder().id(1).build();
        var users = List.of(user);

        var userDto = UserDto.builder().id(1).build();
        var usersDto = List.of(userDto);

        when(getUserUseCase.getUsers()).thenReturn(users);
        when(userConverter.convertToUserDto(user)).thenReturn(userDto);

        mockMvc.perform(get("/users"))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(usersDto)));
    }

    @Test
    @WithMockUser
    void getUser() throws Exception {
        var user = User.builder().id(1).build();
        var userDto = UserDto.builder().id(1).build();

        when(getUserUseCase.getUserById(1)).thenReturn(user);
        when(userConverter.convertToUserDto(user)).thenReturn(userDto);

        mockMvc.perform(get("/users/1"))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(userDto)));
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void deleteUser_whenUserHasRoleSuperAdmin_shouldDeleteUser() throws Exception {
        doNothing().when(deleteUserUseCase).deleteUser(1);

        mockMvc.perform(delete("/users/1"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteUser_whenUserHasRoleAdmin_shouldReturnForbidden() throws Exception {
        mockMvc.perform(delete("/users/1"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void deleteUser_whenUserHasRoleUser_shouldReturnForbidden() throws Exception {
        mockMvc.perform(delete("/users/1"))
            .andExpect(status().isForbidden());
    }
}