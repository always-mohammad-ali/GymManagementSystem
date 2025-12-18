package com.gym.model;

public class Coach {
    private String id;
    private String name;
    private String specialty;
    private String phone;
    private String branch;
    
    public Coach(String id, String name, String specialty, String phone, String branch) {
        this.id = id;
        this.name = name;
        this.specialty = specialty;
        this.phone = phone;
        this.branch = branch;
    }
    
    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getSpecialty() { return specialty; }
    public String getPhone() { return phone; }
    public String getBranch() { return branch; }
    
    // Setters
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setBranch(String branch) { this.branch = branch; }
    
    @Override
    public String toString() {
        return String.format("Coach{id='%s', name='%s', specialty='%s', branch='%s'}", 
                           id, name, specialty, branch);
    }
}