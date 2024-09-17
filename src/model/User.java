package model;

import java.sql.Timestamp;

public class User {
    private int id;
    private String fullname;
    private String email;
    private String password;
    private String imageProfile;
    private String role;
    private String bio;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp lastLoginAt;
    private boolean isActive;

    public User(int id, String fullname, String email, String password, 
                String imageProfile, String role, String bio, 
                Timestamp createdAt, Timestamp updatedAt, 
                Timestamp lastLoginAt, boolean isActive) {
        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.imageProfile = imageProfile;
        this.role = role;
        this.bio = bio;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.lastLoginAt = lastLoginAt;
        this.isActive = isActive;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
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

    public String getImageProfile() {
        return imageProfile;
    }

    public void setImageProfile(String imageProfile) {
        this.imageProfile = imageProfile;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Timestamp getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(Timestamp lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}