package com.wiqaytna.service;

import com.wiqaytna.dto.PatientDTO;
import com.wiqaytna.exception.ResourceNotFoundException;
import com.wiqaytna.model.Patient;
import com.wiqaytna.model.User;
import com.wiqaytna.model.UserRole;
import com.wiqaytna.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

    private Patient testPatient;
    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("patient@example.com");
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setPhone("+212612345678");
        testUser.setRole(UserRole.PATIENT);

        testPatient = new Patient();
        testPatient.setId(1L);
        testPatient.setUserId(1L);
        testPatient.setUser(testUser);
        testPatient.setDateOfBirth(LocalDate.of(1990, 1, 1));
        testPatient.setMedicalHistory("No significant history");
        testPatient.setBloodGroup("O+");
        testPatient.setAllergies("None");
    }

    @Test
    void testGetAllPatients_Success() {
        // Given
        Patient patient2 = new Patient();
        patient2.setId(2L);
        patient2.setUserId(2L);

        List<Patient> patients = Arrays.asList(testPatient, patient2);
        when(patientRepository.findAllWithUser()).thenReturn(patients);

        // When
        List<PatientDTO> result = patientService.getAllPatients();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(testUser.getEmail(), result.get(0).getEmail());
        verify(patientRepository).findAllWithUser();
    }

    @Test
    void testGetPatientById_Success() {
        // Given
        when(patientRepository.findById(1L)).thenReturn(Optional.of(testPatient));

        // When
        PatientDTO result = patientService.getPatientById(1L);

        // Then
        assertNotNull(result);
        assertEquals(testPatient.getId(), result.getId());
        assertEquals(testUser.getEmail(), result.getEmail());
        assertEquals(testUser.getFirstName(), result.getFirstName());
        assertEquals(testPatient.getDateOfBirth(), result.getDateOfBirth());
        verify(patientRepository).findById(1L);
    }

    @Test
    void testGetPatientById_NotFound_ThrowsException() {
        // Given
        when(patientRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> patientService.getPatientById(999L));
        verify(patientRepository).findById(999L);
    }

    @Test
    void testGetPatientByUserId_Success() {
        // Given
        when(patientRepository.findByUserId(1L)).thenReturn(Optional.of(testPatient));

        // When
        PatientDTO result = patientService.getPatientByUserId(1L);

        // Then
        assertNotNull(result);
        assertEquals(testPatient.getUserId(), result.getUserId());
        verify(patientRepository).findByUserId(1L);
    }

    @Test
    void testGetPatientByUserId_NotFound_ThrowsException() {
        // Given
        when(patientRepository.findByUserId(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> patientService.getPatientByUserId(999L));
        verify(patientRepository).findByUserId(999L);
    }

    @Test
    void testSavePatient_Success() {
        // Given
        when(patientRepository.save(any(Patient.class))).thenReturn(testPatient);

        // When
        PatientDTO result = patientService.savePatient(testPatient);

        // Then
        assertNotNull(result);
        assertEquals(testPatient.getId(), result.getId());
        verify(patientRepository).save(testPatient);
    }

    @Test
    void testSearchPatients_Success() {
        // Given
        List<Patient> patients = Arrays.asList(testPatient);
        when(patientRepository.searchByName("John")).thenReturn(patients);

        // When
        List<PatientDTO> result = patientService.searchPatients("John");

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
        verify(patientRepository).searchByName("John");
    }

    @Test
    void testSearchPatients_NoResults() {
        // Given
        when(patientRepository.searchByName("Nonexistent")).thenReturn(Arrays.asList());

        // When
        List<PatientDTO> result = patientService.searchPatients("Nonexistent");

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(patientRepository).searchByName("Nonexistent");
    }
}