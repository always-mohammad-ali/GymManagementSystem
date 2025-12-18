package com.gym.controller;

import java.io.IOException;
import java.util.List;

import com.gym.model.User;
import com.gym.util.FileHandler;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DashboardController {
    @FXML private VBox sidebar;
    @FXML private Button dashboardBtn;
    @FXML private Button manageMembersBtn;
    @FXML private Button manageCoachesBtn;
    @FXML private Button myProfileBtn;
    @FXML private Button gymInfoBtn;
    @FXML private Button logoutButton;
    @FXML private StackPane contentArea;
    @FXML private Label userInfoLabel;
    @FXML private Label welcomeMessage;
    @FXML private Label branchLabel;
    @FXML private Label statusLabel;
    @FXML private VBox quickStatsBox;
    @FXML private Label memberCountLabel;
    @FXML private Label coachCountLabel;
    
    private User currentUser;
    private Stage stage;
    
    public void setUser(User user) {
        this.currentUser = user;
    }
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    public void initializeDashboard() {
        setupUIForRole();
        updateQuickStats();
        showDashboard(); // Show default view
    }
    
    private void setupUIForRole() {
        if (currentUser == null) {
            // Guest user
            userInfoLabel.setText("Guest User");
            welcomeMessage.setText("Welcome Guest! You can view gym information.");
            branchLabel.setText("All Branches");
            
            // Hide admin-only features
            manageMembersBtn.setVisible(false);
            manageCoachesBtn.setVisible(false);
            myProfileBtn.setVisible(false);
            quickStatsBox.setVisible(false);
            
        } else if (currentUser.getRole().equals("Admin")) {
            userInfoLabel.setText("Admin: " + currentUser.getUsername());
            welcomeMessage.setText("Welcome Admin! Manage " + currentUser.getBranch());
            branchLabel.setText(currentUser.getBranch());
            
            // Show all admin features
            manageMembersBtn.setVisible(true);
            manageCoachesBtn.setVisible(true);
            myProfileBtn.setVisible(false); // Admin doesn't need personal profile
            quickStatsBox.setVisible(true);
            
        } else if (currentUser.getRole().equals("Member")) {
            userInfoLabel.setText("Member: " + currentUser.getUsername());
            welcomeMessage.setText("Welcome " + currentUser.getUsername() + "!");
            branchLabel.setText(currentUser.getBranch());
            
            // Show member-only features
            manageMembersBtn.setVisible(false);
            manageCoachesBtn.setVisible(false);
            myProfileBtn.setVisible(true);
            quickStatsBox.setVisible(false);
        }
    }
    
    private void updateQuickStats() {
        if (currentUser != null && currentUser.getRole().equals("Admin")) {
            List<String> members = FileHandler.getMembersByBranch(currentUser.getBranch());
            List<String> coaches = FileHandler.getCoachesByBranch(currentUser.getBranch());
            
            memberCountLabel.setText("Members: " + members.size());
            coachCountLabel.setText("Coaches: " + coaches.size());
        }
    }
    
    @FXML
    private void showDashboard() {
        try {
            Parent dashboardContent = FXMLLoader.load(getClass().getResource("/com/gym/dashboardContent.fxml"));
            contentArea.getChildren().setAll(dashboardContent);
            statusLabel.setText("Dashboard loaded");
        } catch (IOException e) {
            loadSimpleContent("Dashboard View", "Welcome to the main dashboard!");
        }
    }
    
    @FXML
    private void showManageMembers() {
        if (currentUser != null && currentUser.getRole().equals("Admin")) {
            try {
                Parent membersContent = FXMLLoader.load(getClass().getResource("/com/gym/manageMembers.fxml"));
                contentArea.getChildren().setAll(membersContent);
                statusLabel.setText("Managing members for " + currentUser.getBranch());
            } catch (IOException e) {
                loadSimpleContent("Manage Members", 
                    "List of members for " + currentUser.getBranch() + "\n" +
                    "You can add, edit, and delete members here.");
            }
        }
    }
    
    @FXML
    private void showManageCoaches() {
        if (currentUser != null && currentUser.getRole().equals("Admin")) {
            try {
                Parent coachesContent = FXMLLoader.load(getClass().getResource("/com/gym/manageCoaches.fxml"));
                contentArea.getChildren().setAll(coachesContent);
                statusLabel.setText("Managing coaches for " + currentUser.getBranch());
            } catch (IOException e) {
                loadSimpleContent("Manage Coaches", 
                    "List of coaches for " + currentUser.getBranch() + "\n" +
                    "You can manage coach information here.");
            }
        }
    }
    
    @FXML
    private void showMyProfile() {
        if (currentUser != null && currentUser.getRole().equals("Member")) {
            try {
                Parent profileContent = FXMLLoader.load(getClass().getResource("/com/gym/memberProfile.fxml"));
                contentArea.getChildren().setAll(profileContent);
                statusLabel.setText("Viewing profile for " + currentUser.getUsername());
            } catch (IOException e) {
                loadSimpleContent("My Profile", 
                    "Member: " + currentUser.getUsername() + "\n" +
                    "Branch: " + currentUser.getBranch() + "\n" +
                    "View your schedule, payments, and update your information.");
            }
        }
    }
    
    @FXML
    private void showGymInfo() {
        try {
            Parent gymInfoContent = FXMLLoader.load(getClass().getResource("/com/gym/gymInfo.fxml"));
            contentArea.getChildren().setAll(gymInfoContent);
            statusLabel.setText("Viewing gym information");
        } catch (IOException e) {
            loadSimpleContent("Gym Information", 
                "Available branches, prices, and contact information.\n" +
                "No login required to view this information.");
        }
    }
    
    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gym/login.fxml"));
            Parent root = loader.load();
            
            LoginController controller = loader.getController();
            controller.setStage(stage);
            
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Gym Management System - Login");
            stage.centerOnScreen();
            
            System.out.println("✅ User logged out: " + (currentUser != null ? currentUser.getUsername() : "Guest"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void loadSimpleContent(String title, String content) {
        VBox simpleContent = new VBox(10);
        simpleContent.setStyle("-fx-padding: 20; -fx-alignment: center;");
        
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        Label contentLabel = new Label(content);
        contentLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #7f8c8d; -fx-wrap-text: true;");
        
        simpleContent.getChildren().addAll(titleLabel, contentLabel);
        contentArea.getChildren().setAll(simpleContent);
    }
}