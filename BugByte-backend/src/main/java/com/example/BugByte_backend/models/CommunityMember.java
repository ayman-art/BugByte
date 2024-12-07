package com.example.BugByte_backend.models;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "community_members")
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

    public Community getCommunity() {
        return community;
    }

    public void setCommunity(Community community) {
        this.community = community;
    }

    public User getMember() {
        return member;
    }

    public void setMember(User member) {
        this.member = member;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }
}
