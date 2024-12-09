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
        return imageService.storeFile(file);
    }

    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            Resource resource = imageService.loadFileAsResource(filename);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

}