package com.militarystore.registration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.militarystore.converter.user.UserConverter;
import com.militarystore.entity.user.User;
import com.militarystore.model.dto.user.GenderDto;
import com.militarystore.model.request.user.UserRegistrationRequest;
import com.militarystore.port.in.user.CreateUserUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RegistrationController.class)
@ContextConfiguration(classes = {RegistrationController.class})
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
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registrationRequest)))
            .andExpect(status().isOk())
            .andExpect(content().string("1"));
    }
}