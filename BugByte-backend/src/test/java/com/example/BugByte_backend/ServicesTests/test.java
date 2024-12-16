package com.example.BugByte_backend.ServicesTests;

import com.example.BugByte_backend.facades.AdministrativeFacade;
import com.example.BugByte_backend.models.Question;
import com.example.BugByte_backend.services.PostingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class test {

    @Autowired
    AdministrativeFacade administrativeFacade;

    @Autowired
    PostingService postingService;
    @Test
    public void testfun1() throws Exception {
        Question question = new Question();
        question.setTitle("hi");
        question.setCommunityId(1L);
        question.setCreatorUserName("RowanYoussef");
        question.setMdContent("hello");
        ArrayList<String> tags = new ArrayList<>();
//        tags.add("1");
        question.setTags(tags);
        long id = postingService.postQuestion(question);
        System.out.println(id);

    }
    @Test
    public void testfun2() throws Exception {
        Question question = new Question();
        question.setTitle("hi");
        question.setCommunityId(1L);
        question.setCreatorUserName("RowanYoussef");
        question.setMdContent("hello");
        ArrayList<String> tags = new ArrayList<>();
//        tags.add("1");
        question.setTags(tags);
        List<Question> questions = postingService.getUserQuestions("RowanYoussef" , 10 , 0);
        System.out.println(questions.get(0).getMdContent());
        Question id = postingService.getQuestion(21L);
        System.out.println(id);

    }
}
