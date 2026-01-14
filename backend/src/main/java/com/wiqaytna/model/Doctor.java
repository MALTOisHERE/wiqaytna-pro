package com.wiqaytna.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Doctor entity - Extends User with doctor-specific information
 * Composition pattern: has a reference to User rather than inheritance
 */
@Entity
@Table(name = "doctors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "User ID is required")
    @Column(name = "user_id", unique = true, nullable = false)
    private Long userId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @NotBlank(message = "Specialization is required")
    @Size(max = 100)
    @Column(nullable = false)
    private String specialization;

    @NotBlank(message = "License number is required")
    @Size(max = 50)
    @Column(name = "license_number", unique = true, nullable = false)
    private String licenseNumber;

    @NotBlank(message = "Cabinet address is required")
    @Column(name = "cabinet_address", nullable = false, columnDefinition = "TEXT")
    private String cabinetAddress;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @DecimalMin(value = "0.0", inclusive = false, message = "Consultation fee must be positive")
    @Column(name = "consultation_fee", precision = 10, scale = 2)
    private BigDecimal consultationFee;

    @Min(value = 0, message = "Years of experience cannot be negative")
    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /**
     * Returns doctor's full name from associated user
     */
    public String getFullName() {
        return user != null ? user.getFullName() : "";
    }

    /**
     * Returns doctor's email from associated user
     */
    public String getEmail() {
        return user != null ? user.getEmail() : "";
    }

    /**
     * Returns doctor's phone from associated user
     */
    public String getPhone() {
        return user != null ? user.getPhone() : "";
    }
}
