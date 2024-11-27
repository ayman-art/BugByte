package com.example.BugByte_backend.models;


import jakarta.persistence.*;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String user_name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Long reputation;

    @Column(nullable = false)
    private Boolean is_admin;

    public User(String user_name, String email, String password) {
        this.id = 0L;
        this.user_name = user_name;
        this.email = email;
        this.password = password;
        this.reputation = 0L;
        this.is_admin = false;
    }

    public User(Long id, String user_name, String email, String password) {
        this.id = id;
        this.user_name = user_name;
        this.email = email;
        this.password = password;
        this.reputation = 0L;
        this.is_admin = false;
    }

    public User(Long id, String user_name, String email, String password, Long reputation, Boolean is_admin) {
        this.id = id;
        this.user_name = user_name;
        this.email = email;
        this.password = password;
        this.reputation = reputation;
        this.is_admin = is_admin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String get_user_name() {
        return user_name;
    }

    public void set_user_name(String user_name) {
        this.user_name = user_name;
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

    public Long getReputation() {
        return reputation;
    }

    public void setReputation(Long reputation) {
        this.reputation = reputation;
    }

    public Boolean get_is_admin() {
        return is_admin;
    }

    public void set_is_admin(Boolean is_admin) {
        this.is_admin = is_admin;
    }
}
