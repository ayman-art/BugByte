package com.example.BugByte_backend.controllers;

import com.example.BugByte_backend.facades.AdministrativeFacade;
import com.example.BugByte_backend.facades.InteractionFacade;
import com.example.BugByte_backend.models.Community;
import com.example.BugByte_backend.models.Question;
import com.example.BugByte_backend.repositories.SearchingFilteringCommunityRepository;
import com.example.BugByte_backend.repositories.SearchingFilteringQuestionRepository;
import com.example.BugByte_backend.services.PostingService;
import com.example.BugByte_backend.services.SearchingFilteringCommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/index")
public class IndexController {

    @Autowired
    private AdministrativeFacade administrativeFacade;
    @Autowired
    private PostingService postingService;

    @Autowired
    private SearchingFilteringCommunityRepository communityRepository;

    @Autowired
    private SearchingFilteringQuestionRepository questionRepository;

    @GetMapping("/all")
    public ResponseEntity<?> indexCommunities(@RequestHeader("Authorization") String token) throws Exception {
        token = token.replace("Bearer ", "");
        List<Community> communities = administrativeFacade.getAllCommunities(token, 1000000, 0);
        System.out.println(communities.size());
        communityRepository.saveAll(communities);

        List<Question> questions = new ArrayList<>();
        for (Community community : communities)
            questions.addAll(postingService.getCommunityQuestions(community.getId(), 1000000, 0));
        System.out.println(questions.size());
        questionRepository.saveAll(questions);

        return new ResponseEntity<>(Map.of("message", "successfully"), HttpStatus.OK);
    }
}
