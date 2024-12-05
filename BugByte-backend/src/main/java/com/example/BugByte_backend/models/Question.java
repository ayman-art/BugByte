package com.example.BugByte_backend.models;
import jakarta.persistence.*;


@Entity
@Table(name="questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "community_id" ,nullable = false)
    private Community community;

    @Column(name = "up_votes" ,nullable = false)
    private Long upVotes;

    @Column(name = "down_votes" ,nullable = false)
    private Long downVotes;

    @OneToOne
    @JoinColumn(name = "validated_answer" ,nullable = true)
    private Answer validated_answer;

    public Question(Long id, Community community, Long upVotes, Long downVotes, Answer validated_answer) {
        this.id = id;
        this.community = community;
        this.upVotes = upVotes;
        this.downVotes = downVotes;
        this.validated_answer = validated_answer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Community getCommunity() {
        return community;
    }

    public void setCommunity(Community community) {
        this.community = community;
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

    public Answer getValidated_answer() {
        return validated_answer;
    }

    public void setValidated_answer(Answer validated_answer) {
        this.validated_answer = validated_answer;
    }
}
