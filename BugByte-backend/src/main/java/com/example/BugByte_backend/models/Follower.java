package com.example.BugByte_backend.models;

import jakarta.persistence.*;

@Entity
@Table(name = "followers")
public class Follower {

    @EmbeddedId
    private FollowerId id;

    @MapsId("followerId")
    @ManyToOne
    private User follower;

    @MapsId("followedId")
    @ManyToOne
    private User followed;

    public Follower() {}

    public Follower(User follower, User followed) {
        this.follower = follower;
        this.followed = followed;
    }

    public User getFollower() {
        return follower;
    }

    public void setFollower(User follower) {
        this.follower = follower;
    }

    public User getFollowed() {
        return followed;
    }

    public void setFollowed(User followed) {
        this.followed = followed;
    }
}
