package com.wiqaytna.service;

import com.wiqaytna.dto.DoctorDTO;
import com.wiqaytna.exception.DuplicateResourceException;
import com.wiqaytna.exception.ResourceNotFoundException;
import com.wiqaytna.model.Doctor;
import com.wiqaytna.model.User;
import com.wiqaytna.model.UserRole;
import com.wiqaytna.repository.DoctorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoctorServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private DoctorService doctorService;

    private Doctor testDoctor;
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("doctor@example.com");
        testUser.setFirstName("Dr. John");
        testUser.setLastName("Smith");
        testUser.setPhone("+212612345678");
        testUser.setRole(UserRole.DOCTOR);

        testDoctor = new Doctor();
        testDoctor.setId(1L);
        testDoctor.setUserId(1L);
        testDoctor.setUser(testUser);
        testDoctor.setSpecialization("Cardiologie");
        testDoctor.setLicenseNumber("LIC123");
        testDoctor.setCabinetAddress("123 Medical Street");
        testDoctor.setBio("Experienced cardiologist");
        testDoctor.setConsultationFee(new BigDecimal("500.00"));
        testDoctor.setYearsOfExperience(10);
    }

    @Test
    void testGetAllDoctors_Success() {
        // Given
        Doctor doctor2 = new Doctor();
        doctor2.setId(2L);
        doctor2.setUserId(2L);
        doctor2.setSpecialization("Dentiste");

        List<Doctor> doctors = Arrays.asList(testDoctor, doctor2);
        when(doctorRepository.findAllWithUser()).thenReturn(doctors);

        // When
        List<DoctorDTO> result = doctorService.getAllDoctors();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Cardiologie", result.get(0).getSpecialization());
        verify(doctorRepository).findAllWithUser();
    }

    @Test
    void testGetDoctorById_Success() {
        // Given
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(testDoctor));

        // When
        DoctorDTO result = doctorService.getDoctorById(1L);

        // Then
        assertNotNull(result);
        assertEquals(testDoctor.getId(), result.getId());
        assertEquals(testDoctor.getSpecialization(), result.getSpecialization());
        assertEquals(testUser.getEmail(), result.getEmail());
        verify(doctorRepository).findById(1L);
    }

    @Test
    void testGetDoctorById_NotFound_ThrowsException() {
        // Given
        when(doctorRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> doctorService.getDoctorById(999L));
        verify(doctorRepository).findById(999L);
    }

    @Test
    void testGetDoctorByUserId_Success() {
        // Given
        when(doctorRepository.findByUserId(1L)).thenReturn(Optional.of(testDoctor));

        // When
        DoctorDTO result = doctorService.getDoctorByUserId(1L);

        // Then
        assertNotNull(result);
        assertEquals(testDoctor.getUserId(), result.getUserId());
        verify(doctorRepository).findByUserId(1L);
    }

    @Test
    void testGetDoctorByUserId_NotFound_ThrowsException() {
        // Given
        when(doctorRepository.findByUserId(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> doctorService.getDoctorByUserId(999L));
    }

    @Test
    void testSearchDoctors_Success() {
        // Given
        List<Doctor> doctors = Arrays.asList(testDoctor);
        when(doctorRepository.searchDoctors("cardio")).thenReturn(doctors);

        // When
        List<DoctorDTO> result = doctorService.searchDoctors("cardio");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getSpecialization().toLowerCase().contains("cardio"));
        verify(doctorRepository).searchDoctors("cardio");
    }

    @Test
    void testGetDoctorsBySpecialization_Success() {
        // Given
        List<Doctor> doctors = Arrays.asList(testDoctor);
        when(doctorRepository.findBySpecializationContainingIgnoreCase("Cardiologie"))
                .thenReturn(doctors);

        // When
        List<DoctorDTO> result = doctorService.getDoctorsBySpecialization("Cardiologie");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(doctorRepository).findBySpecializationContainingIgnoreCase("Cardiologie");
    }

    @Test
    void testSaveDoctor_Success() {
        // Given
        when(doctorRepository.findByLicenseNumber(testDoctor.getLicenseNumber()))
                .thenReturn(Optional.empty());
        when(doctorRepository.save(any(Doctor.class))).thenReturn(testDoctor);

        // When
        DoctorDTO result = doctorService.saveDoctor(testDoctor);

        // Then
        assertNotNull(result);
        assertEquals(testDoctor.getId(), result.getId());
        verify(doctorRepository).findByLicenseNumber(testDoctor.getLicenseNumber());
        verify(doctorRepository).save(testDoctor);
    }

    @Test
    void testSaveDoctor_UpdateExisting_Success() {
        // Given
        Doctor existingDoctor = new Doctor();
        existingDoctor.setId(1L);
        existingDoctor.setLicenseNumber("LIC123");

        when(doctorRepository.findByLicenseNumber("LIC123"))
                .thenReturn(Optional.of(existingDoctor));
        when(doctorRepository.save(any(Doctor.class))).thenReturn(testDoctor);

        // When
        DoctorDTO result = doctorService.saveDoctor(testDoctor);

        // Then
        assertNotNull(result);
        verify(doctorRepository).save(testDoctor);
    }

    @Test
    void testSaveDoctor_DuplicateLicenseNumber_ThrowsException() {
        // Given
        Doctor otherDoctor = new Doctor();
        otherDoctor.setId(2L);
        otherDoctor.setLicenseNumber("LIC123");

        when(doctorRepository.findByLicenseNumber("LIC123"))
                .thenReturn(Optional.of(otherDoctor));

        // When & Then
        assertThrows(DuplicateResourceException.class, () -> doctorService.saveDoctor(testDoctor));
        verify(doctorRepository, never()).save(any(Doctor.class));
    }
}