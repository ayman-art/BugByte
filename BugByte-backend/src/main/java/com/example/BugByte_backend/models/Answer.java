package com.example.BugByte_backend.models;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name="answers")
@Getter
@Setter
public class Answer {
    @Id
    @OneToOne
    @JoinColumn(name = "id", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(name = "up_votes", nullable = false)
    private Long upVotes;

    @Column(name = "down_votes", nullable = false)
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
