package com.example.BugByte_backend.models;


import jakarta.persistence.*;

@Entity
@Table(name="users")
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

    public User() {
        this(0L, "", "", "", 0L, false);
    }

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Long getReputation() {
        return reputation;
    }

    public void setReputation(Long reputation) {
        this.reputation = reputation;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean is_admin) {
        this.isAdmin = is_admin;
    }
}
