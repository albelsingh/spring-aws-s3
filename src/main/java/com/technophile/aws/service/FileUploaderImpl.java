package com.technophile.aws.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
@Service
public class FileUploaderImpl implements FileUploader {

    @Autowired
    AmazonS3 s3Client;
    @Value("${application.bucket.name}")
    private String s3Bucket;

    @Override
    public String uploadFile(MultipartFile file) {
        File fileObj = getFile(file);
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        PutObjectResult result = s3Client.putObject(s3Bucket, fileName, fileObj);
        fileObj.delete();
        log.info("Response :: {}", result);
        return "File Uploaded successfully " + fileObj;
    }

    @Override
    public byte[] downloadFile(String file) {
        S3Object s3Object = s3Client.getObject(s3Bucket, file);
        S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();
        try {
            byte[] content = IOUtils.toByteArray(s3ObjectInputStream);
            return content;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String deleteFile(String file) {
        s3Client.deleteObject(s3Bucket, file);
        return file + " file is deleted";
    }

    public File getFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile);) {
            fos.write(file.getBytes());
            return convertedFile;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
