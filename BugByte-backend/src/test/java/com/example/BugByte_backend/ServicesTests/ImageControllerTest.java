package com.example.BugByte_backend.ServicesTests;

import com.example.BugByte_backend.configurations.StorageConfig;
import com.example.BugByte_backend.controllers.ImageController;
import com.example.BugByte_backend.services.ImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Path;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(ImageController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ImageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImageService imageService;

    @MockBean
    private StorageConfig storageConfig;

    private MockMultipartFile testImage;

    @BeforeEach
    public void setup() {
        // Create a mock image file for testing
        testImage = new MockMultipartFile(
                "file",
                "test-image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test image content".getBytes()
        );

        // Mock the upload path
        when(storageConfig.getUploadPath()).thenReturn(Path.of("/test/upload/path"));
    }

    @Test
    public void testSuccessfulImageUpload() throws Exception {
        // Mock the service to return a URL
        when(imageService.storeFile(any())).thenReturn("http://localhost:8080/download/test-image.jpg");

        // Perform the upload
        mockMvc.perform(multipart("/images/upload")
                        .file(testImage))
                .andExpect(status().isOk())
                .andExpect(content().string("http://localhost:8080/download/test-image.jpg"));
    }

    @Test
    public void testImageUploadWithEmptyFile() throws Exception {
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file",
                "empty.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                new byte[0]
        );

        when(imageService.storeFile(any())).thenReturn("http://localhost:8080/download/empty.jpg");

        mockMvc.perform(multipart("/images/upload")
                        .file(emptyFile))
                .andExpect(status().isOk());
    }

    @Test
    public void testImageRetrieval() throws Exception {
        Resource mockResource = new ByteArrayResource("dummy content".getBytes()) {
            @Override
            public String getFilename() {
                return "test-image.jpg";
            }

            @Override
            public boolean exists() {
                return true;
            }

            @Override
            public boolean isReadable() {
                return true;
            }
        };

        when(imageService.loadFileAsResource("test-image.jpg")).thenReturn(mockResource);

        mockMvc.perform(get("/images/download/test-image.jpg"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG));
    }




    @Test
    public void testNonExistentImageRetrieval() throws Exception {
        mockMvc.perform(get("/images/download/non-existent.jpg"))
                .andExpect(status().isNotFound());
    }
}