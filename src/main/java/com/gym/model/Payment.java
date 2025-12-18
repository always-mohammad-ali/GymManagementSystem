package com.gym.model;

public class Payment {
    private String memberId;
    private String memberName;
    private String amount;
    private String date;
    private String status; // Paid, Unpaid
    
    public Payment(String memberId, String memberName, String amount, 
                  String date, String status) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.amount = amount;
        this.date = date;
        this.status = status;
    }
    
    // Getters
    public String getMemberId() { return memberId; }
    public String getMemberName() { return memberName; }
    public String getAmount() { return amount; }
    public String getDate() { return date; }
    public String getStatus() { return status; }
    
    // Setters
    public void setMemberId(String memberId) { this.memberId = memberId; }
    public void setMemberName(String memberName) { this.memberName = memberName; }
    public void setAmount(String amount) { this.amount = amount; }
    public void setDate(String date) { this.date = date; }
    public void setStatus(String status) { this.status = status; }
    
    @Override
    public String toString() {
        return String.format("Payment{member='%s', amount='%s', date='%s', status='%s'}", 
                           memberName, amount, date, status);
    }
}