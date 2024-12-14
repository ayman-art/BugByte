package com.example.BugByte_backend.controllers;

import com.example.BugByte_backend.models.Question;
import com.example.BugByte_backend.services.RecommendationSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/recommendation")
public class RecommendationSystemController {

    @Autowired
    private RecommendationSystemService recommendationSystemService;

    @GetMapping("/feed")
    public ResponseEntity<?> getFeed(@RequestHeader("Authorization") String token) {
        try {
            List<Question> feed = recommendationSystemService.generateFeedForUser(token);
            return new ResponseEntity<>(Map.of("feed", feed), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
