package model;

import java.time.LocalDateTime;

public class User {
    private int userId;
    private String username;
    private String passwordHash; // Store the hash, not the actual password.
    private String email;
    private LocalDateTime dateRegistered;

    private boolean isAdmin;
    // Other profile-related fields: first name, last name, profile picture, etc.

    // Constructor for new users (without ID)
    public User(String username, String passwordHash, String email, boolean isAdmin) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.dateRegistered = LocalDateTime.now(); // Assuming you want to set the registration date upon object creation
        this.isAdmin = isAdmin;
    }

    // Constructor for users being populated from the database (with ID)
    public User(int userId, String username, String passwordHash, String email, LocalDateTime dateRegistered, boolean isAdmin) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.dateRegistered = dateRegistered;
        this.isAdmin = isAdmin;
    }

    public int getUserId() {
        return userId;
    }

    // Use this method to set the ID after saving a new user to the database
    public void setUserId(int userId) {
        if (this.userId == 0) { // Only allow setting if ID is null (new user scenario)
            this.userId = userId;
        } else {
            throw new IllegalStateException("UserID is already set");
        }
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

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", email='" + email + '\'' +
                ", dateRegistered=" + dateRegistered +
                ", isAdmin=" + isAdmin +
                '}';
    }
}
