package com.militarystore.port.out.googledrive;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface GoogleDrivePort {

    String uploadFile(MultipartFile multipartFile);

    List<String> uploadFiles(List<MultipartFile> multipartFiles);

    byte[] downloadFile(String fileId);

    List<byte[]> downloadFiles(List<String> fileIds);

    void deleteFile(String fileId);

    void deleteFiles(List<String> fileIds);
}
