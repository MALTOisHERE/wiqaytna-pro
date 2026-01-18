package com.wiqaytna;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Wiqaytna Pro - Main Application Class
 * Medical Appointment Management Platform for Independent Medical Practices in Morocco
 *
 * @author Wiqaytna Pro Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableScheduling
public class WiqaytnaProApplication {

    public static void main(String[] args) {
        SpringApplication.run(WiqaytnaProApplication.class, args);
        System.out.println("\n========================================");
        System.out.println("   Wiqaytna Pro API Started");
        System.out.println("   Access at: http://localhost:8080");
        System.out.println("   API Docs: http://localhost:8080/api");
        System.out.println("========================================\n");
    }
}
