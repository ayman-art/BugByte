package com.example.BugByte_backend.controllers;


import com.example.BugByte_backend.models.Community;
import com.example.BugByte_backend.models.Question;
import com.example.BugByte_backend.services.AuthenticationService;
import com.example.BugByte_backend.services.SearchingFilteringCommunityService;
import com.example.BugByte_backend.services.SearchingFilteringQuestionService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/search")
public class SearchingFilteringController {
    @Autowired
    SearchingFilteringQuestionService questionService;
    @Autowired
    SearchingFilteringCommunityService communityService;

    @GetMapping("/question")
    public ResponseEntity<?> searchQuestionsByContent(@RequestHeader("Authorization") String token,
                                                      @RequestParam String content,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size) {
        try {
            token = token.replace("Bearer ", "");
            Claims claim = AuthenticationService.parseToken(token);

            System.out.println("Before");
            Page<Question> questionPage = questionService.searchQuestions(content, page, size);
            System.out.println("after");
            List<Question> list = questionPage.getContent();
            System.out.println(list);
            System.out.println("Hello");
            Map<String, Object> data = Map.of("questions", list);
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/filterQuestion")
    public ResponseEntity<?> filterQuestionByTags(@RequestHeader("Authorization") String token,
                                                  @RequestParam List<String> tags,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size) {
        try {
            token = token.replace("Bearer ", "");
            Claims claim = AuthenticationService.parseToken(token);

            Page<Question> questionPage = questionService.getQuestionsByTags(tags, page, size);
            List<Question> list = questionPage.getContent();

            Map<String, Object> data = Map.of("questions", list);
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/community")
    public ResponseEntity<?> searchCommunityByContent(@RequestHeader("Authorization") String token,
                                                      @RequestParam String content,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size) {
        try {
            token = token.replace("Bearer ", "");
            Claims claim = AuthenticationService.parseToken(token);

            Page<Community> communityPage = communityService.searchCommunity(content, page, size);
            List<Community> communityList = communityPage.getContent();

            Map<String, Object> data = Map.of("communities", communityList);
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/filterCommunity")
    public ResponseEntity<?> filterCommunityByTags(@RequestHeader("Authorization") String token,
                                                  @RequestParam List<String> tags,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size) {
        try {
            token = token.replace("Bearer ", "");
            Claims claim = AuthenticationService.parseToken(token);

            Page<Community> communityPage = communityService.getCommunityByTags(tags, 0, 10);
            List<Community> communityList = communityPage.getContent();

            Map<String, Object> data = Map.of("communities", communityList);
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
