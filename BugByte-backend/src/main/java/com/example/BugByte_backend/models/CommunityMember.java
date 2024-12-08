package com.example.BugByte_backend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommunityMember {
    private Long communityId;

    private Long memberId;

    private Date joinDate = new Date();

    public CommunityMember(Long communityId, Long memberId) {
        this.communityId = communityId;
        this.memberId = memberId;
    }
}
