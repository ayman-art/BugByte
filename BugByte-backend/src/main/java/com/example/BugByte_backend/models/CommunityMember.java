package com.example.BugByte_backend.models;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CommunityMember {
    private Community community;

    private User member;

    private Date joinDate;

    // Constructors
    public CommunityMember(Community community, User member, Date joinDate) {
        this.community = community;
        this.member = member;
        this.joinDate = joinDate;
    }

    public CommunityMember(Community community, User member) {
        this.community = community;
        this.member = member;
        this.joinDate = new Date();
    }
}
