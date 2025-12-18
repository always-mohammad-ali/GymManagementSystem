package com.gym.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    private static final String DATA_DIR = "data/";
    
    static {
        // Ensure data directory exists
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
        } catch (IOException e) {
            System.err.println("Error creating data directory: " + e.getMessage());
        }
    }
    
    // Read all lines from a file
    public static List<String> readAllLines(String filename) {
        List<String> lines = new ArrayList<>();
        try {
            Path filePath = Paths.get(DATA_DIR + filename);
            if (Files.exists(filePath)) {
                lines = Files.readAllLines(filePath);
            }
        } catch (IOException e) {
            System.err.println("Error reading " + filename + ": " + e.getMessage());
        }
        return lines;
    }
    
    // Write a line to a file (append)
    public static boolean writeLine(String filename, String data) {
        try {
            Path filePath = Paths.get(DATA_DIR + filename);
            Files.write(filePath, (data + System.lineSeparator()).getBytes(), 
                      StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            return true;
        } catch (IOException e) {
            System.err.println("Error writing to " + filename + ": " + e.getMessage());
            return false;
        }
    }
    
    // Save all lines to a file (overwrite)
    public static boolean saveAll(String filename, List<String> data) {
        try {
            Path filePath = Paths.get(DATA_DIR + filename);
            Files.write(filePath, data, StandardOpenOption.CREATE, 
                      StandardOpenOption.TRUNCATE_EXISTING);
            return true;
        } catch (IOException e) {
            System.err.println("Error saving " + filename + ": " + e.getMessage());
            return false;
        }
    }
    
    // Parse CSV line
    public static String[] parseCSV(String line) {
        return line.split(",");
    }
    
    // Find user by username
    public static String[] findUser(String username) {
        List<String> users = readAllLines("users.txt");
        for (String user : users) {
            String[] parts = parseCSV(user.trim());
            if (parts.length >= 4 && parts[0].equalsIgnoreCase(username.trim())) {
                return parts;
            }
        }
        return null;
    }
    
    // Add new user
    public static boolean addUser(String username, String password, String role, String branch) {
        if (findUser(username) != null) {
            return false; // Username exists
        }
        String userData = String.join(",", username, password, role, branch);
        return writeLine("users.txt", userData);
    }
    
    // Get all branches
    public static List<String> getBranches() {
        return readAllLines("branches.txt");
    }
    
    // Get members by branch
    public static List<String> getMembersByBranch(String branch) {
        List<String> allMembers = readAllLines("members.txt");
        List<String> filtered = new ArrayList<>();
        
        for (String member : allMembers) {
            String[] parts = parseCSV(member);
            if (parts.length >= 6 && parts[5].equalsIgnoreCase(branch)) {
                filtered.add(member);
            }
        }
        return filtered;
    }
    
    // Get coaches by branch
    public static List<String> getCoachesByBranch(String branch) {
        List<String> allCoaches = readAllLines("coaches.txt");
        List<String> filtered = new ArrayList<>();
        
        for (String coach : allCoaches) {
            String[] parts = parseCSV(coach);
            if (parts.length >= 5 && parts[4].equalsIgnoreCase(branch)) {
                filtered.add(coach);
            }
        }
        return filtered;
    }
    
    // Get member by ID
    public static String[] getMemberById(String memberId) {
        List<String> members = readAllLines("members.txt");
        for (String member : members) {
            String[] parts = parseCSV(member);
            if (parts.length >= 1 && parts[0].equalsIgnoreCase(memberId)) {
                return parts;
            }
        }
        return null;
    }
    
    // Get payments for a member
    public static List<String> getPaymentsForMember(String memberId) {
        List<String> allPayments = readAllLines("payments.txt");
        List<String> filtered = new ArrayList<>();
        
        for (String payment : allPayments) {
            String[] parts = parseCSV(payment);
            if (parts.length >= 1 && parts[0].equalsIgnoreCase(memberId)) {
                filtered.add(payment);
            }
        }
        return filtered;
    }
    
    // Add new member
    public static boolean addMember(String id, String name, String phone, 
                                   String schedule, String status, String branch, String joinDate) {
        String memberData = String.join(",", id, name, phone, schedule, status, branch, joinDate);
        return writeLine("members.txt", memberData);
    }
    
    // Add new coach
    public static boolean addCoach(String id, String name, String specialty, 
                                  String phone, String branch) {
        String coachData = String.join(",", id, name, specialty, phone, branch);
        return writeLine("coaches.txt", coachData);
    }
    
    // Add payment record
    public static boolean addPayment(String memberId, String memberName, 
                                    String amount, String date, String status) {
        String paymentData = String.join(",", memberId, memberName, amount, date, status);
        return writeLine("payments.txt", paymentData);
    }
    
    // Update member status
    public static boolean updateMemberStatus(String memberId, String newStatus) {
        List<String> members = readAllLines("members.txt");
        List<String> updated = new ArrayList<>();
        boolean found = false;
        
        for (String member : members) {
            String[] parts = parseCSV(member);
            if (parts.length >= 5 && parts[0].equalsIgnoreCase(memberId)) {
                parts[4] = newStatus; // Update status
                updated.add(String.join(",", parts));
                found = true;
            } else {
                updated.add(member);
            }
        }
        
        if (found) {
            return saveAll("members.txt", updated);
        }
        return false;
    }
    
    // Search members by name
    public static List<String> searchMembers(String query, String branch) {
        List<String> members = getMembersByBranch(branch);
        List<String> results = new ArrayList<>();
        
        for (String member : members) {
            String[] parts = parseCSV(member);
            if (parts.length >= 2 && parts[1].toLowerCase().contains(query.toLowerCase())) {
                results.add(member);
            }
        }
        return results;
    }
}