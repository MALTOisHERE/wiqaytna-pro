package com.wiqaytna.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * User entity - Base class for all users (Doctors, Patients, Assistants)
 * Follows SOLID principles - Single Responsibility (only user data and authentication)
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Column(nullable = false)
    private String password;

    @NotNull(message = "Role is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "user_role")
    private UserRole role;

    @NotBlank(message = "First name is required")
    @Size(max = 100)
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 100)
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotBlank(message = "Phone is required")
    @Size(max = 20)
    @Column(nullable = false)
    private String phone;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_active")
    private Boolean isActive = true;

    /**
     * Returns full name of the user
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Checks if user is a doctor
     */
    public boolean isDoctor() {
        return role == UserRole.DOCTOR;
    }

    /**
     * Checks if user is a patient
     */
    public boolean isPatient() {
        return role == UserRole.PATIENT;
    }

    /**
     * Checks if user is an assistant
     */
    public boolean isAssistant() {
        return role == UserRole.ASSISTANT;
    }
}
