package com.wiqaytna.service;

import com.wiqaytna.dto.PatientDTO;
import com.wiqaytna.exception.ResourceNotFoundException;
import com.wiqaytna.model.Patient;
import com.wiqaytna.model.User;
import com.wiqaytna.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for Patient operations
 * Handles business logic for patient management
 */
@Service
@Transactional
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    /**
     * Get all patients
     */
    public List<PatientDTO> getAllPatients() {
        return patientRepository.findAllWithUser().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get patient by ID
     */
    public PatientDTO getPatientById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", id));
        return mapToDTO(patient);
    }

    /**
     * Get patient by user ID
     */
    public PatientDTO getPatientByUserId(Long userId) {
        Patient patient = patientRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "userId", userId));
        return mapToDTO(patient);
    }

    /**
     * Save or update patient
     */
    public PatientDTO savePatient(Patient patient) {
        Patient savedPatient = patientRepository.save(patient);
        return mapToDTO(savedPatient);
    }

    /**
     * Search patients by name
     */
    public List<PatientDTO> searchPatients(String name) {
        return patientRepository.searchByName(name).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Update patient profile
     */
    public PatientDTO updatePatient(Long id, PatientDTO patientDTO) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", id));

        // Update patient fields
        if (patientDTO.getDateOfBirth() != null) {
            patient.setDateOfBirth(patientDTO.getDateOfBirth());
        }
        if (patientDTO.getMedicalHistory() != null) {
            patient.setMedicalHistory(patientDTO.getMedicalHistory());
        }
        if (patientDTO.getBloodGroup() != null) {
            patient.setBloodGroup(patientDTO.getBloodGroup());
        }
        if (patientDTO.getAllergies() != null) {
            patient.setAllergies(patientDTO.getAllergies());
        }

        Patient updatedPatient = patientRepository.save(patient);
        return mapToDTO(updatedPatient);
    }

    /**
     * Map Patient entity to DTO
     */
    private PatientDTO mapToDTO(Patient patient) {
        PatientDTO dto = new PatientDTO();
        dto.setId(patient.getId());
        dto.setUserId(patient.getUserId());
        dto.setDateOfBirth(patient.getDateOfBirth());
        dto.setMedicalHistory(patient.getMedicalHistory());
        dto.setBloodGroup(patient.getBloodGroup());
        dto.setAllergies(patient.getAllergies());
        dto.setAge(patient.getAge());

        // Set user information if available
        if (patient.getUser() != null) {
            User user = patient.getUser();
            dto.setEmail(user.getEmail());
            dto.setFirstName(user.getFirstName());
            dto.setLastName(user.getLastName());
            dto.setPhone(user.getPhone());
        }

        return dto;
    }
}
