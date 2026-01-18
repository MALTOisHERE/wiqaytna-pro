package com.wiqaytna.service;

import com.wiqaytna.dto.DoctorDTO;
import com.wiqaytna.exception.DuplicateResourceException;
import com.wiqaytna.exception.ResourceNotFoundException;
import com.wiqaytna.model.Doctor;
import com.wiqaytna.model.User;
import com.wiqaytna.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for Doctor operations
 * Handles business logic for doctor management
 */
@Service
@Transactional
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    /**
     * Get all doctors
     */
    public List<DoctorDTO> getAllDoctors() {
        return doctorRepository.findAllWithUser().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get doctor by ID
     */
    public DoctorDTO getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", id));
        return mapToDTO(doctor);
    }

    /**
     * Get doctor by user ID
     */
    public DoctorDTO getDoctorByUserId(Long userId) {
        Doctor doctor = doctorRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "userId", userId));
        return mapToDTO(doctor);
    }

    /**
     * Search doctors by keyword (name or specialization)
     */
    public List<DoctorDTO> searchDoctors(String keyword) {
        return doctorRepository.searchDoctors(keyword).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get doctors by specialization
     */
    public List<DoctorDTO> getDoctorsBySpecialization(String specialization) {
        return doctorRepository.findBySpecializationContainingIgnoreCase(specialization).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Create or update doctor profile
     */
    public DoctorDTO saveDoctor(Doctor doctor) {
        // Check if license number already exists for other doctors
        doctorRepository.findByLicenseNumber(doctor.getLicenseNumber())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(doctor.getId())) {
                        throw new DuplicateResourceException("Doctor", "licenseNumber", doctor.getLicenseNumber());
                    }
                });

        Doctor savedDoctor = doctorRepository.save(doctor);
        return mapToDTO(savedDoctor);
    }

    /**
     * Update doctor profile
     */
    public DoctorDTO updateDoctor(Long id, DoctorDTO doctorDTO) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", id));

        // Update doctor fields
        if (doctorDTO.getSpecialization() != null) {
            doctor.setSpecialization(doctorDTO.getSpecialization());
        }
        if (doctorDTO.getCabinetAddress() != null) {
            doctor.setCabinetAddress(doctorDTO.getCabinetAddress());
        }
        if (doctorDTO.getBio() != null) {
            doctor.setBio(doctorDTO.getBio());
        }
        if (doctorDTO.getConsultationFee() != null) {
            doctor.setConsultationFee(doctorDTO.getConsultationFee());
        }
        if (doctorDTO.getYearsOfExperience() != null) {
            doctor.setYearsOfExperience(doctorDTO.getYearsOfExperience());
        }

        Doctor updatedDoctor = doctorRepository.save(doctor);
        return mapToDTO(updatedDoctor);
    }

    /**
     * Map Doctor entity to DTO
     */
    private DoctorDTO mapToDTO(Doctor doctor) {
        DoctorDTO dto = new DoctorDTO();
        dto.setId(doctor.getId());
        dto.setUserId(doctor.getUserId());
        dto.setSpecialization(doctor.getSpecialization());
        dto.setLicenseNumber(doctor.getLicenseNumber());
        dto.setCabinetAddress(doctor.getCabinetAddress());
        dto.setBio(doctor.getBio());
        dto.setConsultationFee(doctor.getConsultationFee());
        dto.setYearsOfExperience(doctor.getYearsOfExperience());

        // Set user information if available
        if (doctor.getUser() != null) {
            User user = doctor.getUser();
            dto.setEmail(user.getEmail());
            dto.setFirstName(user.getFirstName());
            dto.setLastName(user.getLastName());
            dto.setPhone(user.getPhone());
        }

        return dto;
    }
}
