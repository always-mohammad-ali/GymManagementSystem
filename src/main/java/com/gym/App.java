package com.gym;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("🚀 Starting Gym Management System...");
        
        // Initialize data directory
        initializeDataFiles();
        
        // Load login screen
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gym/login.fxml"));
        Parent root = loader.load();
        
        // Pass stage to controller
        com.gym.controller.LoginController controller = loader.getController();
        controller.setStage(primaryStage);
        
        Scene scene = new Scene(root, 900, 650);
        
        // Set stage properties
        primaryStage.setTitle("🏋️ Gym Management System");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        primaryStage.show();
        
        System.out.println("✅ Application started successfully!");
        System.out.println("👤 Default Admin: admin/admin123 (Downtown Branch)");
        System.out.println("👤 Default Member: john/john123 (Uptown Branch)");
    }
    
    private void initializeDataFiles() {
        try {
            java.io.File dataDir = new java.io.File("data");
            if (!dataDir.exists()) {
                dataDir.mkdirs();
                System.out.println("📁 Created data directory");
                
                // Create branches file with more details
                java.nio.file.Files.write(
                    java.nio.file.Paths.get("data/branches.txt"),
                    java.util.Arrays.asList(
                        "Downtown Branch,123 Main St,555-0101,$50/month",
                        "Uptown Branch,456 Oak Ave,555-0102,$60/month",
                        "Westside Branch,789 Pine Rd,555-0103,$55/month",
                        "Eastside Branch,321 Elm Blvd,555-0104,$65/month"
                    )
                );
                
                // Create users file with admin and members
                java.nio.file.Files.write(
                    java.nio.file.Paths.get("data/users.txt"),
                    java.util.Arrays.asList(
                        "admin,admin123,Admin,Downtown Branch",
                        "john,john123,Member,Uptown Branch",
                        "sarah,sarah123,Member,Downtown Branch",
                        "coach1,coach123,Admin,Westside Branch"
                    )
                );
                
                // Create members file
                java.nio.file.Files.write(
                    java.nio.file.Paths.get("data/members.txt"),
                    java.util.Arrays.asList(
                        "M001,John Doe,555-1001,9AM-11AM,Active,Uptown Branch,2024-01-15",
                        "M002,Sarah Smith,555-1002,11AM-1PM,Active,Downtown Branch,2024-02-01",
                        "M003,Mike Johnson,555-1003,3PM-5PM,Active,Westside Branch,2024-01-20",
                        "M004,Emily Davis,555-1004,5PM-7PM,Active,Downtown Branch,2024-01-25"
                    )
                );
                
                // Create coaches file
                java.nio.file.Files.write(
                    java.nio.file.Paths.get("data/coaches.txt"),
                    java.util.Arrays.asList(
                        "C001,Alex Trainer,Yoga,555-2001,Downtown Branch",
                        "C002,Emma Coach,Weightlifting,555-2002,Uptown Branch",
                        "C003,David Instructor,Cardio,555-2003,Westside Branch"
                    )
                );
                
                // Create payments file
                java.nio.file.Files.write(
                    java.nio.file.Paths.get("data/payments.txt"),
                    java.util.Arrays.asList(
                        "M001,John Doe,50,2024-01-15,Paid",
                        "M002,Sarah Smith,50,2024-02-01,Paid",
                        "M003,Mike Johnson,55,2024-01-20,Unpaid",
                        "M004,Emily Davis,50,2024-01-25,Paid"
                    )
                );
                
                System.out.println("📄 Sample data files created");
            } else {
                System.out.println("📁 Data directory already exists");
            }
        } catch (Exception e) {
            System.err.println("❌ Error initializing data files: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        System.out.println("🎯 Gym Management System v1.0");
        System.out.println("📋 Features:");
        System.out.println("  • File-based storage (no database)");
        System.out.println("  • Multi-branch support");
        System.out.println("  • Three user roles: Admin, Member, Guest");
        System.out.println("  • Role-based access control");
        System.out.println("  • Signup/Login system");
        launch(args);
    }
}