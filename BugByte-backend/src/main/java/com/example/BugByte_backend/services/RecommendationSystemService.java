package com.example.BugByte_backend.services;

import com.example.BugByte_backend.models.Question;
import com.example.BugByte_backend.repositories.RecommendationSystemRepository;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendationSystemService {

    @Autowired
    private RecommendationSystemRepository recommendationSystemRepository;

    public List<Question> generateFeedForUser(String token) {
        if (token == null)
            throw new NullPointerException("Token can't be null");

        token = token.replace("Bearer ", "");
        Claims claim = AuthenticationService.parseToken(token);
        Long id = Long.parseLong(claim.getId());
        System.out.println(id);

        return recommendationSystemRepository.generateFeedForUser(id);
    }
}
