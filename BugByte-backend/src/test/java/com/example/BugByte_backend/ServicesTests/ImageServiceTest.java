package com.example.BugByte_backend.ServicesTests;

import com.example.BugByte_backend.configurations.StorageConfig;
import com.example.BugByte_backend.services.ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTest {

    @Mock
    private StorageConfig storageConfig;

    @Mock
    private MultipartFile multipartFile;

    private ImageService imageService;
    private Path tempUploadPath;

    @BeforeEach
    public void setup() throws IOException {
        tempUploadPath = Files.createTempDirectory("test-uploads");

        when(storageConfig.getUploadPath()).thenReturn(tempUploadPath);

        imageService = new ImageService(storageConfig);

        ReflectionTestUtils.setField(imageService, "baseUrl", "http://localhost:8080");
    }
    @Test
    public void testStoreFile_IOException() throws IOException {
        when(multipartFile.getOriginalFilename()).thenReturn("test.jpg");
        when(multipartFile.getInputStream()).thenThrow(new IOException("Simulated IO Error"));

        assertThrows(RuntimeException.class, () -> {
            imageService.storeFile(multipartFile);
        });
    }


    @Test
    public void testLoadFileAsResource_FileNotFound() {
        assertThrows(RuntimeException.class, () -> {
            imageService.loadFileAsResource("non-existent-file.jpg");
        });
    }

    @Test
    public void testStoreFile_Success() throws IOException {
        String originalFilename = "test-image.jpg";
        byte[] content = "test file content".getBytes();

        when(multipartFile.getOriginalFilename()).thenReturn(originalFilename);
        when(multipartFile.getInputStream()).thenReturn(new ByteArrayInputStream(content));

        String storedFileUrl = imageService.storeFile(multipartFile);

        assertNotNull(storedFileUrl);
        assertTrue(storedFileUrl.startsWith("http://localhost:8080/images/"));
        assertTrue(storedFileUrl.endsWith(".jpg"));

        String filename = storedFileUrl.substring(storedFileUrl.lastIndexOf("/") + 1);
        Path storedFilePath = tempUploadPath.resolve(filename);
        assertTrue(Files.exists(storedFilePath));
    }

}