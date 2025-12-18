package com.gym.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import com.gym.util.FileHandler;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignupController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private TextField fullNameField;
    @FXML private TextField phoneField;
    @FXML private ComboBox<String> branchComboBox;
    @FXML private ComboBox<String> typeComboBox;
    @FXML private Label errorLabel;
    @FXML private Button signupButton;
    @FXML private Button backButton;
    
    private Stage stage;
    
    @FXML
    public void initialize() {
        // Load branches
        loadBranches();
        
        // Load account types
        typeComboBox.setItems(FXCollections.observableArrayList("Member", "Coach"));
        typeComboBox.getSelectionModel().selectFirst();
        
        // Clear error on typing
        setupFieldListeners();
    }
    
    private void loadBranches() {
        List<String> branches = FileHandler.getBranches();
        for (String branch : branches) {
            String[] parts = branch.split(",");
            if (parts.length > 0) {
                branchComboBox.getItems().add(parts[0]);
            }
        }
        if (!branchComboBox.getItems().isEmpty()) {
            branchComboBox.getSelectionModel().selectFirst();
        }
    }
    
    private void setupFieldListeners() {
        usernameField.textProperty().addListener((obs, old, newVal) -> errorLabel.setText(""));
        passwordField.textProperty().addListener((obs, old, newVal) -> errorLabel.setText(""));
        confirmPasswordField.textProperty().addListener((obs, old, newVal) -> errorLabel.setText(""));
        fullNameField.textProperty().addListener((obs, old, newVal) -> errorLabel.setText(""));
    }
    
    @FXML
    private void handleSignup() {
        // Get form data
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();
        String fullName = fullNameField.getText().trim();
        String phone = phoneField.getText().trim();
        String branch = branchComboBox.getValue();
        String type = typeComboBox.getValue();
        
        // Validation
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || 
            fullName.isEmpty() || branch == null || type == null) {
            showError("Please fill all required fields");
            return;
        }
        
        if (username.length() < 3) {
            showError("Username must be at least 3 characters");
            return;
        }
        
        if (password.length() < 6) {
            showError("Password must be at least 6 characters");
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match");
            return;
        }
        
        // Check if username exists
        if (FileHandler.findUser(username) != null) {
            showError("Username already exists");
            return;
        }
        
        // Create user account
        String role = type.equals("Coach") ? "Admin" : "Member"; // Coaches are admins
        boolean success = FileHandler.addUser(username, password, role, branch);
        
        if (success) {
            // If member, also add to members file
            if (type.equals("Member")) {
                String memberId = "M" + String.format("%03d", System.currentTimeMillis() % 1000);
                String joinDate = LocalDate.now().toString();
                FileHandler.addMember(memberId, fullName, phone, "Not Assigned", "Active", branch, joinDate);
            }
            
            // If coach, also add to coaches file
            if (type.equals("Coach")) {
                String coachId = "C" + String.format("%03d", System.currentTimeMillis() % 1000);
                FileHandler.addCoach(coachId, fullName, "General", phone, branch);
            }
            
            showSuccess("Account created successfully! Redirecting to login...");
            
            // Return to login after 2 seconds
            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                    javafx.application.Platform.runLater(this::handleBack);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        } else {
            showError("Failed to create account. Please try again.");
        }
    }
    
    @FXML
    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gym/login.fxml"));
            Parent root = loader.load();
            
            LoginController controller = loader.getController();
            controller.setStage(stage);
            
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setStyle("-fx-text-fill: #e74c3c;");
    }
    
    private void showSuccess(String message) {
        errorLabel.setText(message);
        errorLabel.setStyle("-fx-text-fill: #2ecc71;");
        signupButton.setDisable(true);
    }
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}