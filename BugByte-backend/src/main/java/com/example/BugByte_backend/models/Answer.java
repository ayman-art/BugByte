package com.example.BugByte_backend.models;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Answer {
    private Post post;

    private Question question;

    private Long upVotes;

    private Long downVotes;

    public Answer(){
        this.post = new Post();
        this.question = new Question();
        this.upVotes = 0L;
        this.downVotes = 0L;
    }

    public Answer(Post post, Long question, Long upVotes, Long downVotes) {
        this.post = post;
        this.question = new Question();
        this.question.getPost().setId(question);
        this.upVotes = upVotes;
        this.downVotes = downVotes;
    }
}
