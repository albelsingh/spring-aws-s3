package com.technophile.aws.controller;

import com.technophile.aws.service.FileUploader;
import org.apache.tomcat.util.file.ConfigurationSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    FileUploader fileUploader;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam(value = "file") MultipartFile  file ){
       return new ResponseEntity<>(fileUploader.uploadFile(file), HttpStatus.OK);
    }

    @GetMapping("/download/{file}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable  String  file ){
        byte [] content= fileUploader.downloadFile(file);
        ByteArrayResource byteArrayResource=new ByteArrayResource(content);
        return ResponseEntity.ok().contentLength(content.length)
                .header("Content-type","application/octet-stream")
                .header("Content-disposition","attachment; filename=\""+file+"\"")
                .body(byteArrayResource);
    }
    @DeleteMapping("/delete/{file}")
    public ResponseEntity<Object> deleteFile(@PathVariable ("file") String file ) {
        return new ResponseEntity<>(fileUploader.deleteFile(file), HttpStatus.OK);
    }

}
