package com.wiqaytna.controller;

import com.wiqaytna.dto.PatientDTO;
import com.wiqaytna.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Patient operations
 * Handles patient-related API endpoints
 * Note: CORS is configured globally in SecurityConfig
 */
@RestController
@RequestMapping("/api/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;

    /**
     * Get all patients
     * GET /api/patients
     */
    @GetMapping
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
        List<PatientDTO> patients = patientService.getAllPatients();
        return ResponseEntity.ok(patients);
    }

    /**
     * Get patient by ID
     * GET /api/patients/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getPatientById(@PathVariable Long id) {
        try {
            PatientDTO patient = patientService.getPatientById(id);
            return ResponseEntity.ok(patient);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get patient by user ID
     * GET /api/patients/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getPatientByUserId(@PathVariable Long userId) {
        try {
            PatientDTO patient = patientService.getPatientByUserId(userId);
            return ResponseEntity.ok(patient);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Search patients by name
     * GET /api/patients/search?name=Ahmed
     */
    @GetMapping("/search")
    public ResponseEntity<List<PatientDTO>> searchPatients(@RequestParam String name) {
        List<PatientDTO> patients = patientService.searchPatients(name);
        return ResponseEntity.ok(patients);
    }

    /**
     * Update patient profile
     * PUT /api/patients/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePatient(@PathVariable Long id, @RequestBody PatientDTO patientDTO) {
        try {
            PatientDTO updatedPatient = patientService.updatePatient(id, patientDTO);
            return ResponseEntity.ok(updatedPatient);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
