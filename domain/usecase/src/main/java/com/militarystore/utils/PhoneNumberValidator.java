package com.militarystore.utils;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.militarystore.exception.MsValidationException;

import static java.util.Objects.isNull;

public class PhoneNumberValidator {

    private PhoneNumberValidator() {
    }

    public static void validatePhone(String phone) {
        if (isNull(phone) || phone.isBlank()) {
            throw new MsValidationException("Phone number should not empty, instead was: " + phone);
        }

        var isValidNumber = isValidNumber(phone);
        if (!isValidNumber) {
            throw new MsValidationException("Input phone number is not valid - " + phone);
        }
    }

    private static boolean isValidNumber(String phoneNumber) {
        var numberUtil = PhoneNumberUtil.getInstance();
        try {
            var parsedNumber = numberUtil.parse(phoneNumber, "");
            return numberUtil.isValidNumber(parsedNumber);
        } catch (NumberParseException e) {
            return false;
        }
    }
}
