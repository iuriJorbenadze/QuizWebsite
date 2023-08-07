package model;

import java.time.LocalDateTime;

public class User {
    private Long userId;
    private String username;
    private String passwordHash; // Store the hash, not the actual password.
    private String email;
    private LocalDateTime dateRegistered;
    // Other profile-related fields: first name, last name, profile picture, etc.

    // Constructor
    public User(Long userId, String username, String passwordHash, String email, LocalDateTime dateRegistered) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.dateRegistered = dateRegistered;
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(LocalDateTime dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", email='" + email + '\'' +
                ", dateRegistered=" + dateRegistered +
                '}';
    }
}