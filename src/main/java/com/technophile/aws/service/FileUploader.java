package com.technophile.aws.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileUploader {

    String uploadFile(MultipartFile file) throws InterruptedException, IOException;

    Object deleteFile(String file);

    byte[] downloadFile(String file);
}
