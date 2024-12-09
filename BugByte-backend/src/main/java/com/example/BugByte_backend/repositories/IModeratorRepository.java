package com.example.BugByte_backend.repositories;

public interface IModeratorRepository {

    Boolean makeModerator(Long userId, Long communityId);

    Boolean removeModerator(Long userId, Long communityId);



}
