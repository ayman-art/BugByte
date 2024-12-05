package com.example.BugByte_backend.models;
import jakarta.persistence.*;


@Entity
@Table(name="answers")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "question_id" ,nullable = false)
    private Question question;

    @Column(name = "up_votes" ,nullable = false)
    private Long upVotes;

    @Column(name = "down_votes" ,nullable = false)
    private Long downVotes;

    public Answer(Long id, Question question, Long upVotes, Long downVotes) {
        this.id = id;
        this.question = question;
        this.upVotes = upVotes;
        this.downVotes = downVotes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Long getUpVotes() {
        return upVotes;
    }

    public void setUpVotes(Long upVotes) {
        this.upVotes = upVotes;
    }

    public Long getDownVotes() {
        return downVotes;
    }

    public void setDownVotes(Long downVotes) {
        this.downVotes = downVotes;
    }
}
