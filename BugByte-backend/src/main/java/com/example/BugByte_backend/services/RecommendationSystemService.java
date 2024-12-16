package com.example.BugByte_backend.services;

import com.example.BugByte_backend.models.Question;
import com.example.BugByte_backend.models.User;
import com.example.BugByte_backend.repositories.RecommendationSystemRepository;
import com.example.BugByte_backend.repositories.TagsRepository;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecommendationSystemService {

    @Autowired
    private RecommendationSystemRepository recommendationSystemRepository;

    @Autowired
    private TagsRepository tagsRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public List<Question> generateFeedForUser(String token, int pageSize) {
        Long id = getUserIdFromToken(token);

        String cacheKey = "feed:" + id;

        Long size = redisTemplate.opsForList().size(cacheKey);
        if (size == null || size == 0) {
            List<Question> newFeed = recommendationSystemRepository.generateFeedForUser(id);
            for (Question question : newFeed) {
                question.setTags(tagsRepository.findTagsByQuestion(question.getId()));
                redisTemplate.opsForList().rightPush(cacheKey, question);
            }
        }

        return getPaginatedFeed(id, pageSize);
    }

    public void updateUsersFeed(List<User> users, Question question) {
        for (User user : users)
            updateFeedForUser(user.getId(), question);
    }

    private void updateFeedForUser(Long userId, Question question) {
        String cacheKey = "feed:" + userId;

        redisTemplate.opsForList().leftPush(cacheKey, question);
        redisTemplate.opsForList().trim(cacheKey, 0, 99);
    }

    private List<Question> getPaginatedFeed(Long userId, int pageSize) {
        String cacheKey = "feed:" + userId;

        List<Question> paginatedFeed = new ArrayList<>();
        for (int i = 0; i < pageSize; ++i) {
            Question post = (Question) redisTemplate.opsForList().leftPop(cacheKey);
            if (post != null)
                paginatedFeed.add(post);
            else
                break;
        }

        return paginatedFeed;
    }

    private Long getUserIdFromToken(String token) {
        if (token == null)
            throw new NullPointerException("Token can't be null");

        token = token.replace("Bearer ", "");
        Claims claim = AuthenticationService.parseToken(token);
        return Long.parseLong(claim.getId());
    }
}
