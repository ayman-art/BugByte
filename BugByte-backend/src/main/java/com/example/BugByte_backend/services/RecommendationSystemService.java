package com.example.BugByte_backend.services;

import com.example.BugByte_backend.models.Community;
import com.example.BugByte_backend.models.Post;
import com.example.BugByte_backend.models.Question;
import com.example.BugByte_backend.models.User;
import com.example.BugByte_backend.repositories.*;
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
    private PostingRepository postingRepository;

    @Autowired
    private UserRepositoryImp userRepositoryImp;

    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public List<Question> generateFeedForUser(String token, int pageSize) throws Exception {
        Long id = getUserIdFromToken(token);
        String userName = getUserNameFromToken(token);

        String cacheKey = "feed:" + id;

        Long size = redisTemplate.opsForList().size(cacheKey);
        if (size == null || size == 0) {
            List<Question> newFeed = recommendationSystemRepository.generateFeedForUser(id);
            for (Question question : newFeed) {
                question.setTags(tagsRepository.findTagsByQuestion(question.getId()));
                question.setCommunityName(getQuestionCommunity(question.getCommunityId()));
                question.setIsUpVoted(isUpVoted(userName, question.getId()));
                question.setIsDownVoted(isDownVoted(userName, question.getId()));
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

    public Long getUserIdFromToken(String token) {
        if (token == null)
            throw new NullPointerException("Token can't be null");

        token = token.replace("Bearer ", "");
        Claims claim = AuthenticationService.parseToken(token);
        return Long.parseLong(claim.getId());
    }

    private String getUserNameFromToken(String token) {
        if (token == null)
            throw new NullPointerException("Token can't be null");

        token = token.replace("Bearer ", "");
        Claims claim = AuthenticationService.parseToken(token);
        return claim.getSubject();
    }

    private boolean isUpVoted(String userName , long postId) throws Exception {
        User user  = userRepositoryImp.findByIdentity(userName);
        if (user == null){
            throw new Exception("user is null");
        }
        Post post = postingRepository.getPostByID(postId);
        if (post == null){
            throw new Exception("post is null");
        }
        return postingRepository.is_UpVoted(userName , postId);
    }
    private boolean isDownVoted(String userName , long postId) throws Exception {
        User user  = userRepositoryImp.findByIdentity(userName);
        if (user == null){
            throw new Exception("user is null");
        }
        Post post = postingRepository.getPostByID(postId);
        if (post == null){
            throw new Exception("post is null");
        }
        return postingRepository.is_DownVoted(userName , postId);
    }
    private String getQuestionCommunity(long communityId) throws Exception {
        Community community = communityRepository.findCommunityById(communityId);
        if (community == null)
            throw new Exception("community is null");
        return community.getName();
    }

}
