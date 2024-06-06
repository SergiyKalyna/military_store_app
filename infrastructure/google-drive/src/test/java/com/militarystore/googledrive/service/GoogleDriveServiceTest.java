package com.militarystore.googledrive.service;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.militarystore.exception.GoogleDriveException;
import com.militarystore.exception.MsValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GoogleDriveServiceTest {

    private static final String GOOGLE_DRIVE_FOLDER_ID = "google-drive-folder-id";
    private static final String FILE_ID = "file-id";

    @Mock
    private Drive googleDrive;

    private GoogleDriveService googleDriveService;

    @BeforeEach
    void setUp() {
        System.setProperty("google.drive.folder.id", GOOGLE_DRIVE_FOLDER_ID);
        googleDriveService = new GoogleDriveService(GOOGLE_DRIVE_FOLDER_ID, googleDrive);
    }

    @Test
    void downloadFiles_whenFileIdsEmpty_shouldReturnEmptyList() {
        assertThat(googleDriveService.downloadFiles(List.of())).isEmpty();
    }

    @Test
    void downloadFiles_shouldReturnListOfByteArrays() throws IOException {
        var fileIds = List.of("file-id-1", "file-id-2");
        var fileBytes1 = new byte[0];
        var fileBytes2 = new byte[1];
        var driveFiles = mock(Drive.Files.class);
        var file1 = mock(Drive.Files.Get.class);
        var file2 = mock(Drive.Files.Get.class);

        when(googleDrive.files()).thenReturn(driveFiles);
        when(driveFiles.get("file-id-1")).thenReturn(file1);
        when(driveFiles.get("file-id-2")).thenReturn(file2);
        when(file1.executeMediaAsInputStream()).thenReturn(new ByteArrayInputStream(fileBytes1));
        when(file2.executeMediaAsInputStream()).thenReturn(new ByteArrayInputStream(fileBytes2));

        var expectedResult = List.of(fileBytes1, fileBytes2);
        var actualResult = googleDriveService.downloadFiles(fileIds);

        assertThat(actualResult.get(0)).isEqualTo(expectedResult.get(0));
        assertThat(actualResult.get(1)).isEqualTo(expectedResult.get(1));
    }

    @Test
    void downloadFile_whenDuringDownloadingFileHappenedException_shouldThrowGoogleException() throws IOException {
        var driveFiles = mock(Drive.Files.class);
        var file = mock(Drive.Files.Get.class);

        when(googleDrive.files()).thenReturn(driveFiles);
        when(driveFiles.get(anyString())).thenReturn(file);
        when(file.executeMediaAsInputStream()).thenThrow(RuntimeException.class);

        assertThrows(GoogleDriveException.class, () -> googleDriveService.downloadFile(FILE_ID));
    }

    @Test
    void downloadFile_shouldReturnByteArray() throws IOException {
        var driveFiles = mock(Drive.Files.class);
        var file = mock(Drive.Files.Get.class);
        var byteArray = new byte[0];

        when(googleDrive.files()).thenReturn(driveFiles);
        when(driveFiles.get(anyString())).thenReturn(file);
        when(file.executeMediaAsInputStream()).thenReturn(new ByteArrayInputStream(byteArray));

        assertThat(googleDriveService.downloadFile(FILE_ID)).isEqualTo(byteArray);
    }

    @Test
    void uploadFiles() throws IOException {
        var multipartFile1 = mock(MultipartFile.class);
        var multipartFile2 = mock(MultipartFile.class);
        var multipartFiles = List.of(multipartFile1, multipartFile2);
        var fileBytes1 = new byte[0];
        var fileBytes2 = new byte[1];
        var driveFiles = mock(Drive.Files.class);
        var fileCreate = mock(Drive.Files.Create.class);
        var uploadedFile1 = mock(File.class);
        var uploadedFile2 = mock(File.class);

        when(multipartFile1.isEmpty()).thenReturn(false);
        when(multipartFile2.isEmpty()).thenReturn(false);
        when(multipartFile1.getOriginalFilename()).thenReturn("file-name-1");
        when(multipartFile2.getOriginalFilename()).thenReturn("file-name-2");
        when(multipartFile1.getContentType()).thenReturn("file-content-type-1");
        when(multipartFile2.getContentType()).thenReturn("file-content-type-2");
        when(multipartFile1.getBytes()).thenReturn(fileBytes1);
        when(multipartFile2.getBytes()).thenReturn(fileBytes2);
        when(googleDrive.files()).thenReturn(driveFiles);
        when(driveFiles.create(any(), any())).thenReturn(fileCreate);
        when(fileCreate.setFields(anyString())).thenReturn(fileCreate);
        when(fileCreate.execute()).thenReturn(uploadedFile1, uploadedFile2);
        when(uploadedFile1.getId()).thenReturn("file-id-1");
        when(uploadedFile2.getId()).thenReturn("file-id-2");

        var fileIds = List.of("file-id-1", "file-id-2");

        assertThat(googleDriveService.uploadFiles(multipartFiles)).isEqualTo(fileIds);
    }

    @Test
    void uploadFile_whenFileIsNull_shouldThrowValidationException() {
        assertThrows(MsValidationException.class, () -> googleDriveService.uploadFile(null));
    }

    @Test
    void uploadFile_whenFileIsEmpty_shouldThrowValidationException() {
        var multipartFile = mock(MultipartFile.class);

        when(multipartFile.isEmpty()).thenReturn(true);

        assertThrows(MsValidationException.class, () -> googleDriveService.uploadFile(multipartFile));
    }

    @Test
    void uploadFile_whenExceptionOccurredDuringUploadingFile_shouldThrowGoogleException() throws IOException {
        var multipartFile = mock(MultipartFile.class);

        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getOriginalFilename()).thenReturn("file-name");
        when(multipartFile.getContentType()).thenReturn("file-content-type");
        when(multipartFile.getBytes()).thenThrow(RuntimeException.class);

        assertThrows(GoogleDriveException.class, () -> googleDriveService.uploadFile(multipartFile));
    }

    @Test
    void uploadFile() throws IOException {
        var multipartFile = mock(MultipartFile.class);
        var fileBytes = new byte[0];
        var driveFiles = mock(Drive.Files.class);
        var fileCreate = mock(Drive.Files.Create.class);
        var uploadedFile = mock(File.class);

        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getOriginalFilename()).thenReturn("file-name");
        when(multipartFile.getContentType()).thenReturn("file-content-type");
        when(multipartFile.getBytes()).thenReturn(fileBytes);
        when(googleDrive.files()).thenReturn(driveFiles);
        when(driveFiles.create(any(), any())).thenReturn(fileCreate);
        when(fileCreate.setFields(anyString())).thenReturn(fileCreate);
        when(fileCreate.execute()).thenReturn(uploadedFile);
        when(uploadedFile.getId()).thenReturn(FILE_ID);

        assertThat(googleDriveService.uploadFile(multipartFile)).isEqualTo(FILE_ID);
    }

    @Test
    void deleteFile() throws IOException {
        var driveFiles = mock(Drive.Files.class);
        var fileDelete = mock(Drive.Files.Delete.class);

        when(googleDrive.files()).thenReturn(driveFiles);
        when(driveFiles.delete(anyString())).thenReturn(fileDelete);

        assertDoesNotThrow(() -> googleDriveService.deleteFile(FILE_ID));
    }

    @Test
    void deleteFile_whenDuringDeletingFileHappenedException_shouldThrowGoogleException() throws IOException {
        var driveFiles = mock(Drive.Files.class);
        var fileDelete = mock(Drive.Files.Delete.class);

        when(googleDrive.files()).thenReturn(driveFiles);
        when(driveFiles.delete(anyString())).thenReturn(fileDelete);
        when(fileDelete.execute()).thenThrow(RuntimeException.class);

        assertThrows(GoogleDriveException.class, () -> googleDriveService.deleteFile(FILE_ID));
    }

    @Test
    void deleteFiles() throws IOException {
        var fileIds = List.of("file-id-1", "file-id-2");
        var driveFiles = mock(Drive.Files.class);
        var fileDelete1 = mock(Drive.Files.Delete.class);
        var fileDelete2 = mock(Drive.Files.Delete.class);

        when(googleDrive.files()).thenReturn(driveFiles);
        when(driveFiles.delete("file-id-1")).thenReturn(fileDelete1);
        when(driveFiles.delete("file-id-2")).thenReturn(fileDelete2);

        assertDoesNotThrow(() -> googleDriveService.deleteFiles(fileIds));
    }
}