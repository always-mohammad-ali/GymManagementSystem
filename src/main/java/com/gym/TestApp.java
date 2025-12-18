package com.gym;

public class TestApp {
    public static void main(String[] args) {
        System.out.println("✅ Java version: " + System.getProperty("java.version"));
        System.out.println("✅ JavaFX dependencies will be checked at runtime");
        System.out.println("✅ Environment check complete!");
        
        // Check if we can access classes
        try {
            Class.forName("javafx.application.Application");
            System.out.println("✅ JavaFX classes available");
        } catch (ClassNotFoundException e) {
            System.out.println("❌ JavaFX not found in classpath");
        }
    }
}