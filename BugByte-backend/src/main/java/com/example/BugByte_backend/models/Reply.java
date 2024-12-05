package com.example.BugByte_backend.models;
import jakarta.persistence.*;


@Entity
@Table(name="replies")
public class Reply {
    @Id
    @OneToOne
    @JoinColumn(name = "id" ,nullable = false)
    private Post post;


    @ManyToOne
    @JoinColumn(name = "answer_id", nullable = false)
    private Answer answer;

    public Reply(Post post, Answer answer) {
        this.post = post;
        this.answer = answer;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }
}
