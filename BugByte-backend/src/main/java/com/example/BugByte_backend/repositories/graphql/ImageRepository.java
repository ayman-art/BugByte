package com.example.BugByte_backend.repositories.graphql;

import com.example.BugByte_backend.repositories.graphql.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
