package com.example.BugByte_backend.models;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Reply {
    private Post post;

    private Answer answer;

    public Reply(Post post, Long answer) {
        this.post = post;
        this.answer = new Answer();
        this.answer.getPost().setId(answer);
    }
}
