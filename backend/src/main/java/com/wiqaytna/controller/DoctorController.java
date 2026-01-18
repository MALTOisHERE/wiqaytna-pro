package com.wiqaytna.controller;

import com.wiqaytna.dto.DoctorDTO;
import com.wiqaytna.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Doctor operations
 * Handles doctor-related API endpoints
 * Note: CORS is configured globally in SecurityConfig
 */
@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    /**
     * Get all doctors
     * GET /api/doctors
     */
    @GetMapping
    public ResponseEntity<List<DoctorDTO>> getAllDoctors() {
        List<DoctorDTO> doctors = doctorService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }

    /**
     * Get doctor by ID
     * GET /api/doctors/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getDoctorById(@PathVariable Long id) {
        try {
            DoctorDTO doctor = doctorService.getDoctorById(id);
            return ResponseEntity.ok(doctor);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get doctor by user ID
     * GET /api/doctors/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getDoctorByUserId(@PathVariable Long userId) {
        try {
            DoctorDTO doctor = doctorService.getDoctorByUserId(userId);
            return ResponseEntity.ok(doctor);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Search doctors by keyword (name or specialization)
     * GET /api/doctors/search?keyword=dentiste
     */
    @GetMapping("/search")
    public ResponseEntity<List<DoctorDTO>> searchDoctors(@RequestParam String keyword) {
        List<DoctorDTO> doctors = doctorService.searchDoctors(keyword);
        return ResponseEntity.ok(doctors);
    }

    /**
     * Get doctors by specialization
     * GET /api/doctors/specialization?name=Dentiste
     */
    @GetMapping("/specialization")
    public ResponseEntity<List<DoctorDTO>> getDoctorsBySpecialization(@RequestParam String name) {
        List<DoctorDTO> doctors = doctorService.getDoctorsBySpecialization(name);
        return ResponseEntity.ok(doctors);
    }

    /**
     * Update doctor profile
     * PUT /api/doctors/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDoctor(@PathVariable Long id, @RequestBody DoctorDTO doctorDTO) {
        try {
            DoctorDTO updatedDoctor = doctorService.updateDoctor(id, doctorDTO);
            return ResponseEntity.ok(updatedDoctor);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
