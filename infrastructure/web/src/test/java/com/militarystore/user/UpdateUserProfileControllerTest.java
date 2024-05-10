package com.militarystore.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.militarystore.config.TestSecurityConfig;
import com.militarystore.converter.user.UserConverter;
import com.militarystore.entity.user.User;
import com.militarystore.entity.user.model.Role;
import com.militarystore.model.dto.user.GenderDto;
import com.militarystore.model.dto.user.RoleDto;
import com.militarystore.model.request.user.UserUpdatePasswordRequest;
import com.militarystore.model.request.user.UserUpdateRequest;
import com.militarystore.port.in.user.UpdateUserUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UpdateUserProfileController.class)
@ContextConfiguration(classes = {UpdateUserProfileController.class})
@Import(TestSecurityConfig.class)
class UpdateUserProfileControllerTest {

    private static final int USER_ID = 1;

    @MockBean
    private UpdateUserUseCase updateUserUseCase;

    @MockBean
    private UserConverter userConverter;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "USER")
    void updateUser() throws Exception {
        var updateRequest = new UserUpdateRequest(
            "first name",
            "second name",
            "email",
            "phone",
            GenderDto.FEMALE,
            LocalDate.EPOCH
        );

        var user = User.builder().build();

        when(userConverter.convertToUser(updateRequest, USER_ID)).thenReturn(user);

        mockMvc.perform(put("/profile/user")
                .with(user(User.builder().id(USER_ID).role(Role.USER).build()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void changePassword() throws Exception {
        var updatePasswordRequest = new UserUpdatePasswordRequest(
            "old password", "new password", "new password"
        );

        doNothing()
            .when(updateUserUseCase)
            .changePassword(USER_ID, "old password", "new password", "new password");

        mockMvc.perform(put("/profile/user/password")
                .with(user(User.builder().id(USER_ID).role(Role.USER).build()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatePasswordRequest)))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void changeRole_whenUserHasRoleSuperAdmin_shouldChangeRole() throws Exception {
        doNothing().when(updateUserUseCase).changeRole(USER_ID, Role.ADMIN);

        mockMvc.perform(put("/profile/user/1/role")
                .param("roleDto", RoleDto.ADMIN.name()))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void changeRole_whenUserHasRoleAdmin_shouldReturnForbidden() throws Exception {
        doNothing().when(updateUserUseCase).changeRole(USER_ID, Role.USER);

        mockMvc.perform(put("/profile/user/1/role")
                .param("roleDto", RoleDto.USER.name()))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void changeRole_whenUserHasRoleUser_shouldReturnForbidden() throws Exception {
        doNothing().when(updateUserUseCase).changeRole(USER_ID, Role.USER);

        mockMvc.perform(put("/profile/user/1/role")
                .param("roleDto", RoleDto.USER.name()))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void changeBanStatus_whenUserHasRoleSuperAdmin_shouldChangeBanStatus() throws Exception {
        doNothing().when(updateUserUseCase).changeBanStatus(USER_ID, true);

        mockMvc.perform(put("/profile/user/1/ban-status")
                .param("isBanned", "true"))
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void changeBanStatus_whenUserHasRoleAdmin_shouldReturnForbidden() throws Exception {
        doNothing().when(updateUserUseCase).changeBanStatus(USER_ID, true);

        mockMvc.perform(put("/profile/user/1/ban-status")
                .param("isBanned", "true"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void changeBanStatus_whenUserHasRoleUser_shouldReturnForbidden() throws Exception {
        doNothing().when(updateUserUseCase).changeBanStatus(USER_ID, true);

        mockMvc.perform(put("/profile/user/1/ban-status")
                .param("isBanned", "true"))
            .andExpect(status().isForbidden());
    }
}