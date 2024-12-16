package com.example.BugByte_backend.controllers;

import com.example.BugByte_backend.models.Question;
import com.example.BugByte_backend.services.RecommendationSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/recommendation")
public class RecommendationSystemController {

    @Autowired
    private RecommendationSystemService recommendationSystemService;

    @GetMapping("/feed")
    public ResponseEntity<?> getFeed(@RequestHeader("Authorization") String token,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size) {
        try {
            List<Question> feed = recommendationSystemService.generateFeedForUser(token, size);
            return new ResponseEntity<>(Map.of("feed", feed), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
