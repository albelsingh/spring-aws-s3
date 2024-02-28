package com.technophile.aws.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploader {

    String uploadFile(MultipartFile file);

    Object deleteFile(String file);

    byte[] downloadFile(String file);
}
