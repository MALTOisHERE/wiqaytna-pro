package com.wiqaytna.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Data Transfer Object for Doctor
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDTO {
    private Long id;
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String specialization;
    private String licenseNumber;
    private String cabinetAddress;
    private String bio;
    private BigDecimal consultationFee;
    private Integer yearsOfExperience;
}
