package com.example.BugByte_backend.models;

import com.example.BugByte_backend.models.User;
import jakarta.persistence.*;

@Entity
@Table(name="followers")
public class Follower {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower_id;

    @ManyToOne
    @JoinColumn(name = "followed_id", nullable = false)
    private User followed_id;


    public Follower(User follower, User followed) {
        this.follower_id = follower;
        this.followed_id = followed;
    }


    public User getFollower() {
        return follower_id;
    }

    public void setFollower(User follower) {
        this.follower_id = follower;
    }

    public User getFollowed() {
        return followed_id;
    }

    public void setFollowed(User followed) {
        this.followed_id = followed;
    }

}
