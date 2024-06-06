package com.militarystore.googledrive.service;

import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.militarystore.exception.GoogleDriveException;
import com.militarystore.exception.MsValidationException;
import com.militarystore.port.out.googledrive.GoogleDrivePort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.Collections;
import java.util.List;

import static java.util.Objects.isNull;

@Service
@Slf4j
public class GoogleDriveService implements GoogleDrivePort {

    private final String googleDriveFolderId;
    private final Drive googleDrive;

    public GoogleDriveService(
        @Value("${google.drive.folder.id}") String googleDriveFolderId,
        Drive googleDrive
    ) {
        this.googleDriveFolderId = googleDriveFolderId;
        this.googleDrive = googleDrive;
    }

    public List<byte[]> downloadFiles(List<String> fileIds) {
        return fileIds.stream()
            .map(this::downloadFile)
            .toList();
    }

    public byte[] downloadFile(String fileId) {
        try (var fileContent = googleDrive.files().get(fileId).executeMediaAsInputStream()) {
            var fileBytes = fileContent.readAllBytes();
            log.info("File was downloaded from Google Drive by the ID: {}", fileId);

            return fileBytes;
        } catch (Exception e) {
            throw new GoogleDriveException("Error while downloading file from Google Drive by the ID: " + fileId);
        }
    }

    public List<String> uploadFiles(List<MultipartFile> multipartFiles) {
        return multipartFiles.stream()
            .map(this::uploadFile)
            .toList();
    }

    public String uploadFile(MultipartFile multipartFile) {
        validateMultipartFile(multipartFile);

        var fileName = multipartFile.getOriginalFilename();

        try {
            var fileMetadata = generateFileMetadata(fileName);

            var file = new InputStreamContent(
                multipartFile.getContentType(),
                new ByteArrayInputStream(multipartFile.getBytes())
            );

            var uploadedFileId = googleDrive.files().create(fileMetadata, file)
                .setFields("id")
                .execute()
                .getId();

            log.info("File was uploaded to Google Drive with ID: {}", uploadedFileId);

            return uploadedFileId;
        } catch (Exception e) {
            throw new GoogleDriveException("Error while uploading file to Google Drive with the name: " + fileName);
        }
    }

    public void deleteFile(String fileId) {
        try {
            googleDrive.files().delete(fileId).execute();
            log.info("File was deleted from Google Drive by the ID: {}", fileId);
        } catch (Exception e) {
            throw new GoogleDriveException("Error while deleting file from Google Drive by the ID: " + fileId);
        }
    }

    public void deleteFiles(List<String> fileIds) {
        fileIds.forEach(this::deleteFile);
    }

    private void validateMultipartFile(MultipartFile multipartFile) {
        if (isNull(multipartFile)) {
            throw new MsValidationException("Image file should not be null");
        }

        if (multipartFile.isEmpty()) {
            throw new MsValidationException("Image file should not be empty");
        }
    }

    private com.google.api.services.drive.model.File generateFileMetadata(String filename) {
        var fileMetadata = new com.google.api.services.drive.model.File();
        fileMetadata.setName(filename);
        fileMetadata.setParents(Collections.singletonList(googleDriveFolderId));

        return fileMetadata;
    }
}
