package com.example.BugByte_backend.repositories;

import com.example.BugByte_backend.models.Answer;
import com.example.BugByte_backend.models.Question;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class PostingRepositoryTest {
    @Autowired
    PostingRepository postingRepository;
    @Test
    public void testRepository() {
        List<Answer> answers = postingRepository.getAnswersByUserName("Rowan Mohammad", 1, 0);
        if (!answers.isEmpty()) {
            System.out.println(answers.get(0).getPost().getCreator().getUserName());
            System.out.println(answers.get(0).getPost().getMd_content());
            System.out.println(answers.get(0).getPost().getPosted_on());
            System.out.println(answers.get(0).getQuestion().getPost().getId());
        } else {
            System.out.println("No questions found.");
        }
    }
}
