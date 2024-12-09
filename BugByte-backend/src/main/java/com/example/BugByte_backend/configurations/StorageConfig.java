package com.example.BugByte_backend.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class StorageConfig {
    @Value("${file.upload-dir}")
    private String uploadDir;

    public Path getUploadPath() {
        return Paths.get(uploadDir).toAbsolutePath().normalize();
    }
}