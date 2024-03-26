package com.militarystore.user;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.militarystore.entity.user.User;
import com.militarystore.exception.UserValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

import static java.util.Objects.isNull;

@Service
@Slf4j
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
            throw new UserValidationException("Login should not be empty or less than 3 characters, " +
                "instead was: " + login);
        }
    }

    void validatePassword(String password) {
        if (isNull(password) || password.isBlank() || password.length() < MIN_LENGTH) {
            throw new UserValidationException("Password should not be empty or less than 3 characters, " +
                "instead was: " + password);
        }
    }

    private void validateUserInfo(String firstName, String secondName) {
        if (isNull(firstName) || firstName.isBlank()) {
            throw new UserValidationException("First name should not empty, instead was: " + firstName);
        }

        if (isNull(secondName) || secondName.isBlank()) {
            throw new UserValidationException("Second name should not empty, instead was: " + secondName);
        }
    }

    private void validateEmail(String email) {
        if (isNull(email) || email.isBlank()) {
            throw new UserValidationException("Email should not empty, instead was: " + email);
        }

        var patternMatcher = Pattern.compile(EMAIL_REGEX).matcher(email);
        if (!patternMatcher.matches()) {
            throw new UserValidationException("Input email is not valid - " + email);
        }
    }

    private void validatePhone(String phone) {
        if (isNull(phone) || phone.isBlank()) {
            throw new UserValidationException("Phone number should not empty, instead was: " + phone);
        }

        var isValidNumber = isValidNumber(phone);
        if (!isValidNumber) {
            throw new UserValidationException("Input phone number is not valid - " + phone);
        }
    }

    private boolean isValidNumber(String phoneNumber) {
        var numberUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber parsedNumber = numberUtil.parse(phoneNumber, "");
            return numberUtil.isValidNumber(parsedNumber);
        } catch (NumberParseException e) {
            return false;
        }
    }
}
