package com.example.BugByte_backend.models;

import jakarta.persistence.*;

@Entity
@Table(name="validation_code")
public class ValidationCode {

    @Id
    @OneToOne
    @JoinColumn(name = "id", nullable = false)
    private User user;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    public ValidationCode() {}

    public ValidationCode(User user, String code) {
        this.user = user;
        this.code = code;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
