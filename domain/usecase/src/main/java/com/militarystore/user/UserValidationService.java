package com.militarystore.user;

import com.militarystore.entity.user.User;
import com.militarystore.exception.MsValidationException;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

import static com.militarystore.utils.PhoneNumberValidator.validatePhone;
import static java.util.Objects.isNull;

@Service
public class UserValidationService {

    private static final int MIN_LENGTH = 3;
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";

    public void validateNewUser(User user) {
        validateLogin(user.login());
        validatePassword(user.password());
        validateUserInfo(user.firstName(), user.secondName());
        validateEmail(user.email());
        validatePhone(user.phone());
    }

    public void validateUserToUpdate(User user) {
        validateUserInfo(user.firstName(), user.secondName());
        validateEmail(user.email());
        validatePhone(user.phone());
    }

    private void validateLogin(String login) {
        if (isNull(login) || login.isBlank() || login.length() < MIN_LENGTH) {
            throw new MsValidationException("Login should not be empty or less than 3 characters, "
                + "instead was: " + login);
        }
    }

    void validatePassword(String password) {
        if (isNull(password) || password.isBlank() || password.length() < MIN_LENGTH) {
            throw new MsValidationException("Password should not be empty or less than 3 characters, "
                + "instead was: " + password);
        }
    }

    private void validateUserInfo(String firstName, String secondName) {
        if (isNull(firstName) || firstName.isBlank()) {
            throw new MsValidationException("First name should not empty, instead was: " + firstName);
        }

        if (isNull(secondName) || secondName.isBlank()) {
            throw new MsValidationException("Second name should not empty, instead was: " + secondName);
        }
    }

    private void validateEmail(String email) {
        if (isNull(email) || email.isBlank()) {
            throw new MsValidationException("Email should not empty, instead was: " + email);
        }

        var patternMatcher = Pattern.compile(EMAIL_REGEX).matcher(email);
        if (!patternMatcher.matches()) {
            throw new MsValidationException("Input email is not valid - " + email);
        }
    }
}
