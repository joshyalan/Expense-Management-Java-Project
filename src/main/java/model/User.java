package model;

import java.sql.Timestamp;

/**
 * POJO (Plain Old Java Object) representing a User in the system.
 * Demonstrates the OOP concept of Encapsulation using private fields and public getters/setters.
 */
public class User {
    private int id;
    private String username;
    private String passwordHash;
    private String email;
    private String fullName;
    private String role;
    private Timestamp createdAt;

    // Default Constructor
    public User() {
    }

    // Parameterized Constructor
    public User(int id, String username, String passwordHash, String email, String fullName, String role, Timestamp createdAt) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.createdAt = createdAt;
    }

    // Constructor without ID (used when creating a new user before saving to DB)
    public User(String username, String passwordHash, String email, String fullName, String role) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
    }

    // Getters and Setters (Encapsulation)

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
