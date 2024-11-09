package com.example.BugByte_backend.models;

public class User {
    private long id;
    private String user_name;
    private String email;

    private String password;
    private long reputation;
    private boolean is_admin;

    public User(String user_name, String email,String password,boolean is_admin){
        this.user_name = user_name;
        this.email = email;
        this.password = password;
        this.is_admin = is_admin;
    }

    public void setUsername(String user_name) {
        this.user_name = user_name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setReputation(long reputation) {
        this.reputation = reputation;
    }

    public long getId(Long id) {
        return this.id;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return user_name;
    }

    public String getPassword() {
        return password;
    }

    public long getReputation() {
        return reputation;
    }

    public boolean is_adminn() {
        return is_admin;
    }
}
