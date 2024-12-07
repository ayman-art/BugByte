package com.example.BugByte_backend.models;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="users")
@Getter
@Setter
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String bio;

    @Column(nullable = false)
    private Long reputation;

    @Column(nullable = false)
    private Boolean isAdmin;

    public User(String userName, String email, String password) {
        this(0L, userName, email, password, "", 0L, false);
    }

    public User(Long id, String userName, String email, String password) {
        this(id, userName, email, password, "", 0L, false);
    }

    public User(Long id, String userName, String email, String password, String bio) {
        this(id, userName, email, password, bio, 0L, false);
    }

    public User(Long id, String userName, String email, String password, Long reputation, Boolean isAdmin) {
        this(id, userName, email, password, "", reputation, isAdmin);
    }

    public User(Long id, String userName, String email, String password, String bio, Long reputation, Boolean isAdmin) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.bio = bio;
        this.reputation = reputation;
        this.isAdmin = isAdmin;
    }
}
