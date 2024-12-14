package com.example.BugByte_backend.services;

import com.example.BugByte_backend.models.Question;
import com.example.BugByte_backend.repositories.RecommendationSystemRepository;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RecommendationSystemService {

    @Autowired
    private RecommendationSystemRepository recommendationSystemRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public List<Question> generateFeedForUser(String token) {
        Long id = getUserIdFromToken(token);

        String cacheKey = "feed:" + id;
        List<Question> cacheFeed = (List<Question>) redisTemplate.opsForValue().get(cacheKey);

        if (cacheFeed != null && !cacheFeed.isEmpty()) {
            System.out.println("CACHED");
            return cacheFeed;
        }
        System.out.println("NOT CACHED");

        List<Question> newFeed = recommendationSystemRepository.generateFeedForUser(id);
        System.out.println("1");
        redisTemplate.opsForValue().set(cacheKey, newFeed, 1, TimeUnit.HOURS);
        System.out.println("2");

        return newFeed;
    }

    private Long getUserIdFromToken(String token) {
        if (token == null)
            throw new NullPointerException("Token can't be null");

        token = token.replace("Bearer ", "");
        Claims claim = AuthenticationService.parseToken(token);
        return Long.parseLong(claim.getId());
    }
}
