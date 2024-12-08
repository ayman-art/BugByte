package com.example.BugByte_backend.models;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name="replies")
@Getter
@Setter
public class Reply {
    @Id
    @OneToOne
    @JoinColumn(name = "id" ,nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "answer_id", nullable = false)
    private Answer answer;

    public Reply(Post post, Long answer) {
        this.post = post;
        this.answer = new Answer();
        this.answer.getPost().setId(answer);
    }
}
