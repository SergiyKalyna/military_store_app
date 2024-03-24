package com.militarystore.registration;

import com.militarystore.converter.user.UserConverter;
import com.militarystore.model.request.user.UserRegistrationRequest;
import com.militarystore.port.in.user.CreateUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/registration")
public class RegistrationController {

    private final CreateUserUseCase createUserUseCase;
    private final UserConverter userConverter;

    @PostMapping
    public Integer createUser(@RequestBody UserRegistrationRequest registrationRequest) {
        var user = userConverter.convertToUser(registrationRequest);

        return createUserUseCase.saveUser(user);
    }
}
