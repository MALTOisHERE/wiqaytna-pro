package com.wiqaytna.service;

import com.wiqaytna.dto.AuthResponse;
import com.wiqaytna.dto.LoginRequest;
import com.wiqaytna.dto.RegisterRequest;
import com.wiqaytna.dto.UserDTO;
import com.wiqaytna.exception.DuplicateResourceException;
import com.wiqaytna.exception.ResourceNotFoundException;
import com.wiqaytna.model.Patient;
import com.wiqaytna.model.User;
import com.wiqaytna.model.UserRole;
import com.wiqaytna.repository.UserRepository;
import com.wiqaytna.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PatientService patientService;

    @Mock
    private DoctorService doctorService;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setPassword("encodedPassword");
        testUser.setRole(UserRole.PATIENT);
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setPhone("+212612345678");
        testUser.setIsActive(true);

        registerRequest = new RegisterRequest();
        registerRequest.setEmail("newuser@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setRole(UserRole.PATIENT);
        registerRequest.setFirstName("Jane");
        registerRequest.setLastName("Smith");
        registerRequest.setPhone("+212612345679");
        registerRequest.setDateOfBirth(LocalDate.of(1990, 1, 1));

        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");
    }

    @Test
    void testRegister_Success() {
        // Given
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });
        when(patientService.savePatient(any(Patient.class))).thenReturn(new com.wiqaytna.dto.PatientDTO());

        // When
        UserDTO result = userService.register(registerRequest);

        // Then
        assertNotNull(result);
        assertEquals(registerRequest.getEmail(), result.getEmail());
        assertEquals(registerRequest.getFirstName(), result.getFirstName());
        assertEquals(registerRequest.getLastName(), result.getLastName());
        verify(userRepository).existsByEmail(registerRequest.getEmail());
        verify(userRepository).save(any(User.class));
        verify(patientService).savePatient(any(Patient.class));
    }

    @Test
    void testRegister_EmailAlreadyExists_ThrowsException() {
        // Given
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(true);

        // When & Then
        assertThrows(DuplicateResourceException.class, () -> userService.register(registerRequest));
        verify(userRepository).existsByEmail(registerRequest.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testRegister_DoctorRole_CreatesDoctor() {
        // Given
        registerRequest.setRole(UserRole.DOCTOR);
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });
        when(doctorService.saveDoctor(any(com.wiqaytna.model.Doctor.class)))
                .thenReturn(new com.wiqaytna.dto.DoctorDTO());

        // When
        UserDTO result = userService.register(registerRequest);

        // Then
        assertNotNull(result);
        verify(doctorService).saveDoctor(any(com.wiqaytna.model.Doctor.class));
        verify(patientService, never()).savePatient(any(Patient.class));
    }

    @Test
    void testLogin_Success() {
        // Given
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(testUser));
        when(jwtUtil.generateToken(testUser.getEmail())).thenReturn("jwt-token");

        // When
        AuthResponse result = userService.login(loginRequest);

        // Then
        assertNotNull(result);
        assertEquals("jwt-token", result.getToken());
        assertNotNull(result.getUser());
        assertEquals(testUser.getEmail(), result.getUser().getEmail());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtil).generateToken(testUser.getEmail());
    }

    @Test
    void testLogin_InvalidCredentials_ThrowsException() {
        // Given
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        // When & Then
        assertThrows(BadCredentialsException.class, () -> userService.login(loginRequest));
        verify(userRepository, never()).findByEmail(anyString());
    }

    @Test
    void testLogin_UserNotFound_ThrowsException() {
        // Given
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> userService.login(loginRequest));
    }

    @Test
    void testGetUserByEmail_Success() {
        // Given
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));

        // When
        UserDTO result = userService.getUserByEmail(testUser.getEmail());

        // Then
        assertNotNull(result);
        assertEquals(testUser.getEmail(), result.getEmail());
        assertEquals(testUser.getFirstName(), result.getFirstName());
        verify(userRepository).findByEmail(testUser.getEmail());
    }

    @Test
    void testGetUserByEmail_NotFound_ThrowsException() {
        // Given
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, 
                () -> userService.getUserByEmail("nonexistent@example.com"));
    }

    @Test
    void testGetUserById_Success() {
        // Given
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));

        // When
        UserDTO result = userService.getUserById(testUser.getId());

        // Then
        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        assertEquals(testUser.getEmail(), result.getEmail());
        verify(userRepository).findById(testUser.getId());
    }

    @Test
    void testGetUserById_NotFound_ThrowsException() {
        // Given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(999L));
    }

    @Test
    void testUpdateUser_Success() {
        // Given
        UserDTO updateDTO = new UserDTO();
        updateDTO.setFirstName("Updated");
        updateDTO.setLastName("Name");
        updateDTO.setPhone("+212612345680");

        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        UserDTO result = userService.updateUser(testUser.getId(), updateDTO);

        // Then
        assertNotNull(result);
        verify(userRepository).findById(testUser.getId());
        verify(userRepository).save(any(User.class));
    }
}