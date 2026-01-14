package com.wiqaytna.dto;

import com.wiqaytna.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for User
 * Prevents exposing internal entity structure
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String email;
    private UserRole role;
    private String firstName;
    private String lastName;
    private String phone;
    private Boolean isActive;
}
