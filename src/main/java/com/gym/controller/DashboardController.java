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
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
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
        System.out.println("Initializing dashboard for: " + currentUser.getUsername());
        setupUIForRole();
        updateQuickStats();
        showDashboard(); // Show default view
    }
    
    private void setupUIForRole() {
        String username = currentUser.getUsername();
        String role = currentUser.getRole();
        String branch = currentUser.getBranch();
        
        System.out.println("Setting up UI for: " + role + " - " + username);
        
        if (role.equals("Guest")) {
            // Guest user
            userInfoLabel.setText("👤 Guest User");
            welcomeMessage.setText("Welcome Guest! You can view gym information and prices.");
            branchLabel.setText("All Branches");
            
            // Hide admin/member-only features
            manageMembersBtn.setVisible(false);
            manageCoachesBtn.setVisible(false);
            myProfileBtn.setVisible(false);
            quickStatsBox.setVisible(false);
            dashboardBtn.setVisible(false); // Guest doesn't need dashboard
            
        } else if (role.equals("Admin")) {
            // Admin user
            userInfoLabel.setText("👑 Admin: " + username);
            welcomeMessage.setText("Welcome Admin! Manage " + branch + " branch.");
            branchLabel.setText(branch);
            
            // Show all admin features
            manageMembersBtn.setVisible(true);
            manageCoachesBtn.setVisible(true);
            myProfileBtn.setVisible(false); // Admin doesn't need personal profile
            quickStatsBox.setVisible(true);
            dashboardBtn.setVisible(true);
            
        } else if (role.equals("Member")) {
            // Member user
            userInfoLabel.setText("👤 Member: " + username);
            welcomeMessage.setText("Welcome " + username + "! View your schedule and payments.");
            branchLabel.setText(branch);
            
            // Show member-only features
            manageMembersBtn.setVisible(false);
            manageCoachesBtn.setVisible(false);
            myProfileBtn.setVisible(true);
            quickStatsBox.setVisible(false);
            dashboardBtn.setVisible(true);
        }
        
        // Always show gym info and logout
        gymInfoBtn.setVisible(true);
        logoutButton.setVisible(true);
    }
    
    private void updateQuickStats() {
        if (currentUser != null && currentUser.getRole().equals("Admin")) {
            List<String> members = FileHandler.getMembersByBranch(currentUser.getBranch());
            List<String> coaches = FileHandler.getCoachesByBranch(currentUser.getBranch());
            
            memberCountLabel.setText("Members: " + members.size());
            coachCountLabel.setText("Coaches: " + coaches.size());
            
            System.out.println("Stats updated: " + members.size() + " members, " + coaches.size() + " coaches");
        }
    }
    
    @FXML
    private void showDashboard() {
        System.out.println("Showing dashboard...");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gym/dashboardContent.fxml"));
            Parent dashboardContent = loader.load();
            contentArea.getChildren().setAll(dashboardContent);
            statusLabel.setText("Dashboard loaded");
        } catch (IOException e) {
            System.out.println("Dashboard content not found, loading simple view");
            loadSimpleContent("📊 Dashboard", 
                "Welcome to your dashboard!\n" +
                "Role: " + currentUser.getRole() + "\n" +
                "Branch: " + currentUser.getBranch() + "\n\n" +
                getRoleSpecificMessage());
        }
    }
    
    @FXML
    private void showManageMembers() {
        if (currentUser != null && currentUser.getRole().equals("Admin")) {
            System.out.println("Showing manage members for admin...");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gym/manageMembers.fxml"));
                Parent membersContent = loader.load();
                contentArea.getChildren().setAll(membersContent);
                statusLabel.setText("Managing members for " + currentUser.getBranch());
            } catch (IOException e) {
                loadAdminMembersContent();
            }
        }
    }
    
    private void loadAdminMembersContent() {
        VBox content = new VBox(20);
        content.setStyle("-fx-padding: 20;");
        
        Label title = new Label("👥 Manage Members - " + currentUser.getBranch());
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        // Get members for this branch
        List<String> members = FileHandler.getMembersByBranch(currentUser.getBranch());
        
        VBox memberList = new VBox(10);
        memberList.setStyle("-fx-padding: 10; -fx-background-color: white; -fx-background-radius: 5;");
        
        Label header = new Label("Members in " + currentUser.getBranch() + " (" + members.size() + ")");
        header.setStyle("-fx-font-weight: bold;");
        memberList.getChildren().add(header);
        
        for (String memberStr : members) {
            String[] parts = memberStr.split(",");
            if (parts.length >= 2) {
                Label memberLabel = new Label("• " + parts[0] + " - " + parts[1] + " (" + parts[4] + ")");
                memberList.getChildren().add(memberLabel);
            }
        }
        
        // Add member form
        VBox addForm = new VBox(10);
        addForm.setStyle("-fx-padding: 15; -fx-background-color: #f8f9fa; -fx-background-radius: 5;");
        
        Label formTitle = new Label("Add New Member");
        formTitle.setStyle("-fx-font-weight: bold;");
        
        TextField nameField = new TextField();
        nameField.setPromptText("Member Name");
        
        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone Number");
        
        Button addButton = new Button("Add Member");
        addButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");
        addButton.setOnAction(e -> {
            String name = nameField.getText();
            String phone = phoneField.getText();
            if (!name.isEmpty() && !phone.isEmpty()) {
                String memberId = "M" + (members.size() + 101);
                String joinDate = java.time.LocalDate.now().toString();
                FileHandler.addMember(memberId, name, phone, "9AM-11AM", "Active", 
                                     currentUser.getBranch(), joinDate);
                showDashboard(); // Refresh
            }
        });
        
        addForm.getChildren().addAll(formTitle, nameField, phoneField, addButton);
        
        content.getChildren().addAll(title, memberList, addForm);
        contentArea.getChildren().setAll(content);
    }
    
    @FXML
    private void showManageCoaches() {
        if (currentUser != null && currentUser.getRole().equals("Admin")) {
            System.out.println("Showing manage coaches for admin...");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gym/manageCoaches.fxml"));
                Parent coachesContent = loader.load();
                contentArea.getChildren().setAll(coachesContent);
                statusLabel.setText("Managing coaches for " + currentUser.getBranch());
            } catch (IOException e) {
                loadAdminCoachesContent();
            }
        }
    }
    
    private void loadAdminCoachesContent() {
        VBox content = new VBox(20);
        content.setStyle("-fx-padding: 20;");
        
        Label title = new Label("💪 Manage Coaches - " + currentUser.getBranch());
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        // Get coaches for this branch
        List<String> coaches = FileHandler.getCoachesByBranch(currentUser.getBranch());
        
        VBox coachList = new VBox(10);
        coachList.setStyle("-fx-padding: 10; -fx-background-color: white; -fx-background-radius: 5;");
        
        Label header = new Label("Coaches in " + currentUser.getBranch() + " (" + coaches.size() + ")");
        header.setStyle("-fx-font-weight: bold;");
        coachList.getChildren().add(header);
        
        for (String coachStr : coaches) {
            String[] parts = coachStr.split(",");
            if (parts.length >= 3) {
                Label coachLabel = new Label("• " + parts[1] + " - " + parts[2] + " (" + parts[3] + ")");
                coachList.getChildren().add(coachLabel);
            }
        }
        
        content.getChildren().addAll(title, coachList);
        contentArea.getChildren().setAll(content);
    }
    
    @FXML
    private void showMyProfile() {
        if (currentUser != null && currentUser.getRole().equals("Member")) {
            System.out.println("Showing profile for member...");
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gym/memberProfile.fxml"));
                Parent profileContent = loader.load();
                contentArea.getChildren().setAll(profileContent);
                statusLabel.setText("Viewing profile for " + currentUser.getUsername());
            } catch (IOException e) {
                loadMemberProfileContent();
            }
        }
    }
    
    private void loadMemberProfileContent() {
        VBox content = new VBox(20);
        content.setStyle("-fx-padding: 20;");
        
        Label title = new Label("👤 My Profile - " + currentUser.getUsername());
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        // Get member details
        String[] memberData = FileHandler.getMemberById(findMemberId(currentUser.getUsername()));
        
        VBox profileBox = new VBox(10);
        profileBox.setStyle("-fx-padding: 20; -fx-background-color: white; -fx-background-radius: 5;");
        
        if (memberData != null) {
            profileBox.getChildren().addAll(
                createProfileRow("Member ID:", memberData[0]),
                createProfileRow("Name:", memberData[1]),
                createProfileRow("Phone:", memberData[2]),
                createProfileRow("Schedule:", memberData[3]),
                createProfileRow("Status:", memberData[4]),
                createProfileRow("Branch:", memberData[5]),
                createProfileRow("Join Date:", memberData[6])
            );
        } else {
            profileBox.getChildren().add(new Label("Profile information not found"));
        }
        
        // Payment status
        VBox paymentBox = new VBox(10);
        paymentBox.setStyle("-fx-padding: 15; -fx-background-color: #f8f9fa; -fx-background-radius: 5;");
        
        Label paymentTitle = new Label("💰 Payment Status");
        paymentTitle.setStyle("-fx-font-weight: bold;");
        
        List<String> payments = FileHandler.getPaymentsForMember(
            memberData != null ? memberData[0] : ""
        );
        
        if (!payments.isEmpty()) {
            for (String payment : payments) {
                String[] parts = payment.split(",");
                if (parts.length >= 5) {
                    String statusColor = parts[4].equals("Paid") ? "#2ecc71" : "#e74c3c";
                    Label paymentLabel = new Label("• " + parts[3] + ": $" + parts[2] + " - " + parts[4]);
                    paymentLabel.setStyle("-fx-text-fill: " + statusColor + ";");
                    paymentBox.getChildren().add(paymentLabel);
                }
            }
        } else {
            paymentBox.getChildren().add(new Label("No payment records found"));
        }
        
        content.getChildren().addAll(title, profileBox, paymentTitle, paymentBox);
        contentArea.getChildren().setAll(content);
    }
    
    private String findMemberId(String username) {
        // In a real system, you'd have a mapping between usernames and member IDs
        // For now, return a placeholder
        return "M001";
    }
    
    private HBox createProfileRow(String label, String value) {
        HBox row = new HBox(10);
        Label labelL = new Label(label);
        labelL.setStyle("-fx-font-weight: bold; -fx-min-width: 100;");
        Label valueL = new Label(value);
        row.getChildren().addAll(labelL, valueL);
        return row;
    }
    
    @FXML
    private void showGymInfo() {
        System.out.println("Showing gym info...");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gym/gymInfo.fxml"));
            Parent gymInfoContent = loader.load();
            contentArea.getChildren().setAll(gymInfoContent);
            statusLabel.setText("Viewing gym information");
        } catch (IOException e) {
            loadGymInfoContent();
        }
    }
    
    private void loadGymInfoContent() {
        VBox content = new VBox(20);
        content.setStyle("-fx-padding: 20;");
        
        Label title = new Label("🏢 Gym Information");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        // Get all branches
        List<String> branches = FileHandler.getBranches();
        
        VBox branchesBox = new VBox(15);
        branchesBox.setStyle("-fx-padding: 20; -fx-background-color: white; -fx-background-radius: 5;");
        
        Label branchesTitle = new Label("📍 Our Locations");
        branchesTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        branchesBox.getChildren().add(branchesTitle);
        
        for (String branch : branches) {
            String[] parts = branch.split(",");
            if (parts.length >= 4) {
                VBox branchCard = new VBox(5);
                branchCard.setStyle("-fx-padding: 10; -fx-background-color: #f8f9fa; -fx-background-radius: 5;");
                
                Label name = new Label(parts[0]);
                name.setStyle("-fx-font-weight: bold;");
                
                Label address = new Label("📍 " + parts[1]);
                Label phone = new Label("📞 " + parts[2]);
                Label price = new Label("💰 " + parts[3]);
                
                branchCard.getChildren().addAll(name, address, phone, price);
                branchesBox.getChildren().add(branchCard);
            }
        }
        
        // Membership plans
        VBox plansBox = new VBox(15);
        plansBox.setStyle("-fx-padding: 20; -fx-background-color: white; -fx-background-radius: 5;");
        
        Label plansTitle = new Label("💎 Membership Plans");
        plansTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        plansBox.getChildren().add(plansTitle);
        
        String[] plans = {
            "Basic: $50/month - Gym access, Locker room",
            "Standard: $80/month - + Group classes, Sauna",
            "Premium: $120/month - + Personal trainer, 24/7 access",
            "Family: $200/month - 4 members, All premium features"
        };
        
        for (String plan : plans) {
            Label planLabel = new Label("• " + plan);
            plansBox.getChildren().add(planLabel);
        }
        
        content.getChildren().addAll(title, branchesBox, plansBox);
        contentArea.getChildren().setAll(content);
    }
    
    @FXML
    private void handleLogout() {
        System.out.println("Logging out: " + currentUser.getUsername());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gym/login.fxml"));
            Parent root = loader.load();
            
            LoginController controller = loader.getController();
            controller.setStage(stage);
            
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Gym Management System - Login");
            stage.centerOnScreen();
            
            System.out.println("✅ User logged out: " + currentUser.getUsername());
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
    
    private String getRoleSpecificMessage() {
        switch (currentUser.getRole()) {
            case "Admin":
                return "Admin Panel Access:\n" +
                       "• Manage Members (Add/Edit/Delete)\n" +
                       "• Manage Coaches\n" +
                       "• View Branch Statistics\n" +
                       "• Monitor Payments";
            case "Member":
                return "Member Access:\n" +
                       "• View Your Profile\n" +
                       "• Check Schedule\n" +
                       "• View Payment Status\n" +
                       "• Update Personal Info";
            case "Guest":
                return "Guest Access:\n" +
                       "• View Gym Locations\n" +
                       "• See Membership Plans\n" +
                       "• Contact Information\n" +
                       "• No login required";
            default:
                return "";
        }
    }
}