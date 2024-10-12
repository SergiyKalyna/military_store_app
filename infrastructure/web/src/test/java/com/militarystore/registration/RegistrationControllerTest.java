package com.militarystore.registration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.militarystore.config.TestSecurityConfig;
import com.militarystore.converter.user.UserConverter;
import com.militarystore.entity.user.User;
import com.militarystore.model.dto.user.GenderDto;
import com.militarystore.model.request.user.UserRegistrationRequest;
import com.militarystore.port.in.user.CreateUserUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RegistrationController.class)
@ContextConfiguration(classes = {RegistrationController.class})
@Import(TestSecurityConfig.class)
class RegistrationControllerTest {

    @MockBean
    private CreateUserUseCase createUserUseCase;

    @MockBean
    private UserConverter userConverter;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createUser() throws Exception {
        var registrationRequest = new UserRegistrationRequest(
            "username",
            "password",
            "first name",
            "last name",
            "email",
            "phone",
            GenderDto.FEMALE,
            LocalDate.EPOCH
        );

        var user = User.builder().build();

        when(userConverter.convertToUser(registrationRequest)).thenReturn(user);
        when(createUserUseCase.saveUser(user)).thenReturn(1);

        mockMvc.perform(post("/registration")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("login", "username")
                .param("password", "password")
                .param("firstName", "first name")
                .param("secondName", "last name")
                .param("email", "email")
                .param("phone", "phone")
                .param("gender", GenderDto.FEMALE.toString())
                .param("birthdayDate", LocalDate.EPOCH.toString()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/"));
    }
}