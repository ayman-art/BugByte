package com.example.BugByte_backend.models;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name="questions")
@Getter
@Setter
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
}
