package com.example.BugByte_backend.services;

import com.example.BugByte_backend.configurations.StorageConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class ImageService {
    private final Path fileStorageLocation;

    @Value("${server.base-url}")
    private String baseUrl;

    @Autowired
    public ImageService(StorageConfig storageConfig) {
        this.fileStorageLocation = storageConfig.getUploadPath();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        try {
            String originalFileName = file.getOriginalFilename();
            String fileExtension = originalFileName != null
                    ? originalFileName.substring(originalFileName.lastIndexOf("."))
                    : "";
            String uniqueFileName = UUID.randomUUID() + fileExtension;

            Path targetLocation = this.fileStorageLocation.resolve(uniqueFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return baseUrl + "/images/" + uniqueFileName;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file. Please try again!", ex);
        }
    }
}