package com.gym.model;

public class Member {
    private String id;
    private String name;
    private String phone;
    private String schedule;
    private String status; // Active, Inactive
    private String branch;
    private String joinDate;
    
    public Member(String id, String name, String phone, String schedule, 
                 String status, String branch, String joinDate) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.schedule = schedule;
        this.status = status;
        this.branch = branch;
        this.joinDate = joinDate;
    }
    
    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getSchedule() { return schedule; }
    public String getStatus() { return status; }
    public String getBranch() { return branch; }
    public String getJoinDate() { return joinDate; }
    
    // Setters
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setSchedule(String schedule) { this.schedule = schedule; }
    public void setStatus(String status) { this.status = status; }
    public void setBranch(String branch) { this.branch = branch; }
    public void setJoinDate(String joinDate) { this.joinDate = joinDate; }
    
    @Override
    public String toString() {
        return String.format("Member{id='%s', name='%s', branch='%s', status='%s'}", 
                           id, name, branch, status);
    }
}