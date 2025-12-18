package com.gym.controller;

import com.gym.model.User;
import com.gym.util.FileHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private CheckBox rememberMe;
    @FXML private Label errorLabel;
    @FXML private Button loginButton;
    @FXML private Button signupButton;
    @FXML private Button guestButton;
    
    private Stage stage;
    
    @FXML
    public void initialize() {
        // Clear error when user starts typing
        usernameField.textProperty().addListener((obs, old, newVal) -> errorLabel.setText(""));
        passwordField.textProperty().addListener((obs, old, newVal) -> errorLabel.setText(""));
    }
    
    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        
        // Validation
        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter both username and password");
            return;
        }
        
        // Check credentials
        String[] userData = FileHandler.findUser(username);
        if (userData != null && userData[1].equals(password)) {
            // Successful login
            User user = new User(userData[0], userData[1], userData[2], userData[3]);
            openDashboard(user);
        } else {
            showError("Invalid username or password");
        }
    }
    
    @FXML
    private void handleSignup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gym/signup.fxml"));
            Parent root = loader.load();
            
            SignupController controller = loader.getController();
            controller.setStage(stage);
            
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
        } catch (IOException e) {
            showError("Cannot load signup form: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleGuestLogin() {
        // Create guest user
        User guest = new User("Guest", "", "Guest", "All Branches");
        openDashboard(guest);
    }
    
    private void openDashboard(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gym/dashboard.fxml"));
            Parent root = loader.load();
            
            DashboardController controller = loader.getController();
            controller.setUser(user);
            controller.setStage(stage);
            controller.initializeDashboard();
            
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Gym Management System - Dashboard");
            stage.centerOnScreen();
            
            // Show success message
            System.out.println("✅ Login successful for: " + user.getUsername());
        } catch (IOException e) {
            showError("Cannot load dashboard: " + e.getMessage());
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
    }
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}