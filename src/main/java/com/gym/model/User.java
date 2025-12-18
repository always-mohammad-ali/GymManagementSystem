package com.gym.model;

public class User {
    private String username;
    private String password;
    private String role; // Admin, Member
    private String branch;
    
    public User(String username, String password, String role, String branch) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.branch = branch;
    }
    
    // Getters
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getBranch() { return branch; }
    
    // Setters
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(String role) { this.role = role; }
    public void setBranch(String branch) { this.branch = branch; }
    
    @Override
    public String toString() {
        return String.format("User{username='%s', role='%s', branch='%s'}", 
                           username, role, branch);
    }
}