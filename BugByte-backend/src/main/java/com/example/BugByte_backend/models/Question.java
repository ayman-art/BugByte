package com.example.BugByte_backend.models;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Question {
    private Post post;

    private Community community;

    private Long upVotes;

    private Long downVotes;

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
