package com.example.BugByte_backend.services;


import com.example.BugByte_backend.repositories.graphql.ImageRepository;
import com.example.BugByte_backend.repositories.graphql.entities.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;


    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }

    public Image saveImage(Image image) {
        return imageRepository.save(image);
    }
}
