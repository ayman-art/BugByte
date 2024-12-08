package com.example.BugByte_backend.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private Long id;

    private String userName;

    private String email;

    private String password;

    private String bio;

    private Long reputation;

    private Boolean isAdmin;
}
