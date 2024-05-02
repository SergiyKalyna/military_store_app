package com.militarystore.utils;

import java.security.SecureRandom;

public class DiscountProvider {

    private DiscountProvider() {
    }

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int DISCOUNT_CODE_LENGTH = 20;

    public static String generateDiscountCode() {
        StringBuilder discountCode = new StringBuilder(DISCOUNT_CODE_LENGTH);
        for (int i = 0; i < DISCOUNT_CODE_LENGTH; i++) {
            discountCode.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }

        return discountCode.toString();
    }
}
