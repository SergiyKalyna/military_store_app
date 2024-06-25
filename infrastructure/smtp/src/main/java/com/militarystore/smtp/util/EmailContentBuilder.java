package com.militarystore.smtp.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.util.Objects.requireNonNull;

public class EmailContentBuilder {

    private EmailContentBuilder() {
    }

    public static String buildContent(String message, String username) throws IOException {
        var emailTemplate = getEmailTemplate();
        var content = emailTemplate.replace("${username}", username);
        content = content.replace("${message}", message);

        return content;
    }

    private static String getEmailTemplate() throws IOException {
        var path = requireNonNull(EmailContentBuilder.class.getClassLoader().getResource("email-template.html")).getPath();

        return Files.readString(Paths.get(path), StandardCharsets.UTF_8);
    }
}
