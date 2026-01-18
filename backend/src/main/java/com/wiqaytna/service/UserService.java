package com.wiqaytna.service;

import com.wiqaytna.dto.*;
import com.wiqaytna.exception.DuplicateResourceException;
import com.wiqaytna.exception.ResourceNotFoundException;
import com.wiqaytna.model.Patient;
import com.wiqaytna.model.User;
import com.wiqaytna.model.UserRole;
import com.wiqaytna.repository.UserRepository;
import com.wiqaytna.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service layer for User operations
 * Handles business logic for authentication and user management
 * Follows SOLID principles - Single Responsibility
 */
@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PatientService patientService;

    @Autowired
    private DoctorService doctorService;

    /**
     * Register a new user
     */
    public UserDTO register(RegisterRequest request) {
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("User", "email", request.getEmail());
        }

        // Create new user
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setIsActive(true);

        User savedUser = userRepository.save(user);

        // Create corresponding Patient record if role is PATIENT
        if (savedUser.getRole() == UserRole.PATIENT) {
            Patient patient = new Patient();
            patient.setUserId(savedUser.getId());
            // Set date of birth if provided
            if (request.getDateOfBirth() != null) {
                patient.setDateOfBirth(request.getDateOfBirth());
            }
            patientService.savePatient(patient);
        }

        // Create corresponding Doctor record if role is DOCTOR
        if (savedUser.getRole() == UserRole.DOCTOR) {
            com.wiqaytna.model.Doctor doctor = new com.wiqaytna.model.Doctor();
            doctor.setUserId(savedUser.getId());
            // Set placeholder values for required fields
            // Doctor can complete profile later
            doctor.setSpecialization("Not Specified");
            doctor.setLicenseNumber("TEMP_" + savedUser.getId());
            doctor.setCabinetAddress("Not Specified");
            doctorService.saveDoctor(doctor);
        }

        return mapToDTO(savedUser);
    }

    /**
     * Authenticate user and generate JWT token
     */
    public AuthResponse login(LoginRequest request) {
        // Authenticate user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Generate JWT token
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", request.getEmail()));

        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token, mapToDTO(user));
    }

    /**
     * Get user by email
     */
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        return mapToDTO(user);
    }

    /**
     * Get user by ID
     */
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return mapToDTO(user);
    }

    /**
     * Update user profile
     */
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPhone(userDTO.getPhone());

        User updatedUser = userRepository.save(user);
        return mapToDTO(updatedUser);
    }

    /**
     * Map User entity to DTO
     */
    private UserDTO mapToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPhone(user.getPhone());
        dto.setIsActive(user.getIsActive());
        return dto;
    }
}
