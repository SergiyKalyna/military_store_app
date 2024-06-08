package com.militarystore.googledrive.config;


import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;

@Configuration
@Slf4j
public class GoogleDriveConfig {

    private static final NetHttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final GsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    @Bean
    public Drive googleDrive(
        @Value("${spring.application.name}") String applicationName,
        @Value("${google.drive.credentials.path}") String credentialsPath
    ) throws IOException {
        var credential = GoogleCredential
            .fromStream(new FileInputStream(credentialsPath))
            .createScoped(Collections.singleton(DriveScopes.DRIVE));

        var drive = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
            .setApplicationName(applicationName)
            .build();

        log.info("Google Drive API initialized");

        return drive;
    }
}
