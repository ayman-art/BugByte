package com.example.BugByte_backend.models;
import jakarta.persistence.*;


@Entity
@Table(name="questions")
public class Question {
    @Id
    @OneToOne
    @JoinColumn(name = "id", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "community_id" ,nullable = false)
    private Community community;

    @Column(name = "up_votes", nullable = false)
    private Long upVotes;

    @Column(name = "down_votes", nullable = false)
    private Long downVotes;

    @OneToOne
    @JoinColumn(name = "validated_answer")
    private Answer validatedAnswer;

    public Question(Post post, Long community_id, Long upVotes, Long downVotes) {
        this.post = post;
        this.community = new Community();
        this.community.setId(community_id);
        this.upVotes = upVotes;
        this.downVotes = downVotes;
    }

    public Question() {
        this.post = new Post();
        this.community = new Community();
        this.community.setId(1L);
        this.upVotes = 0L;
        this.downVotes = 0L;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
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

    public Answer getValidatedAnswer() {
        return validatedAnswer;
    }

    public void setValidatedAnswer(Answer validatedAnswer) {
        this.validatedAnswer = validatedAnswer;
    }
}
