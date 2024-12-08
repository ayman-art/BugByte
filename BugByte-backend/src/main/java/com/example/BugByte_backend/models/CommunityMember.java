package com.example.BugByte_backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "community_members")
@Getter
@Setter
public class CommunityMember {
    @Id
    @ManyToOne
    @JoinColumn(name = "community_id", nullable = false)
    private Community community;

    @Id
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private User member;

    @Column(name="join_date", nullable = false)
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
