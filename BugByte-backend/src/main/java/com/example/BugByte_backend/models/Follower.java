package com.example.BugByte_backend.models;

import jakarta.persistence.*;

@Entity
@IdClass(FollowerId.class)
@Table(name = "followers")
public class Follower {

    @Id
    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower;

    @Id
    @ManyToOne
    @JoinColumn(name = "followed_id", nullable = false)
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
