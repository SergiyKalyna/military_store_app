package com.militarystore.registration;

import com.militarystore.converter.user.UserConverter;
import com.militarystore.model.request.user.UserRegistrationRequest;
import com.militarystore.port.in.user.CreateUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/registration")
public class RegistrationController {

    private final CreateUserUseCase createUserUseCase;
    private final UserConverter userConverter;

    @GetMapping
    public String getRegistrationForm() {
        return "registration";
    }

    @PostMapping
    public String createUser(@ModelAttribute UserRegistrationRequest registrationRequest) {
        var user = userConverter.convertToUser(registrationRequest);

        createUserUseCase.saveUser(user);

        return "redirect:/";
    }
}
