package com.example.BugByte_backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "followers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Follower {

    @EmbeddedId
    private FollowerId id;

    @MapsId("followerId")
    @ManyToOne
    private User follower;

    @MapsId("followedId")
    @ManyToOne
    private User followed;
}
