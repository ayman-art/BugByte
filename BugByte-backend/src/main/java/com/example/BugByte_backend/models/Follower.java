package com.example.BugByte_backend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class Follower {

    private Long followerId;

    private Long followedId;
}
