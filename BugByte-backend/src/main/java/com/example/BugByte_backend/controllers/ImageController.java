package com.example.BugByte_backend.controllers;

import com.example.BugByte_backend.configurations.StorageConfig;
import com.example.BugByte_backend.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/images")
public class ImageController {
    private final ImageService imageService;
    private final Path fileStorageLocation;

    @Autowired
    public ImageController(ImageService imageService, StorageConfig storageConfig) {
        this.imageService = imageService;
        this.fileStorageLocation = storageConfig.getUploadPath();
    }

    @PostMapping("/upload")
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        System.out.println("recieved");
        return imageService.storeFile(file);
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> serveFile(@RequestParam("name") String filename) {
        System.out.println("reciever get");
        try {
            Path filePath = this.fileStorageLocation.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if(resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().build();
        }
    }
}