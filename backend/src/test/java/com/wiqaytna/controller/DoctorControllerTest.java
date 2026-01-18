package com.wiqaytna.controller;

import com.wiqaytna.dto.DoctorDTO;
import com.wiqaytna.exception.ResourceNotFoundException;
import com.wiqaytna.security.JwtAuthenticationFilter;
import com.wiqaytna.service.DoctorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = DoctorController.class)
@AutoConfigureMockMvc(addFilters = false)
class DoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DoctorService doctorService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private DoctorDTO doctorDTO;

    @BeforeEach
    void setUp() {
        doctorDTO = new DoctorDTO();
        doctorDTO.setId(1L);
        doctorDTO.setUserId(1L);
        doctorDTO.setEmail("doctor@example.com");
        doctorDTO.setFirstName("Dr. John");
        doctorDTO.setLastName("Smith");
        doctorDTO.setSpecialization("Cardiologie");
        doctorDTO.setLicenseNumber("LIC123");
        doctorDTO.setCabinetAddress("123 Medical Street");
        doctorDTO.setConsultationFee(new BigDecimal("500.00"));
        doctorDTO.setYearsOfExperience(10);
    }

    @Test
    void testGetAllDoctors_Success() throws Exception {
        List<DoctorDTO> doctors = Arrays.asList(doctorDTO);
        when(doctorService.getAllDoctors()).thenReturn(doctors);

        mockMvc.perform(get("/api/doctors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("doctor@example.com"))
                .andExpect(jsonPath("$[0].specialization").value("Cardiologie"));
    }

    @Test
    void testGetDoctorById_Success() throws Exception {
        when(doctorService.getDoctorById(1L)).thenReturn(doctorDTO);

        mockMvc.perform(get("/api/doctors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.specialization").value("Cardiologie"));
    }

    @Test
    void testGetDoctorById_NotFound_ReturnsNotFound() throws Exception {
        when(doctorService.getDoctorById(999L))
                .thenThrow(new ResourceNotFoundException("Doctor", "id", 999L));

        mockMvc.perform(get("/api/doctors/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetDoctorByUserId_Success() throws Exception {
        when(doctorService.getDoctorByUserId(1L)).thenReturn(doctorDTO);

        mockMvc.perform(get("/api/doctors/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1));
    }

    @Test
    void testGetDoctorByUserId_NotFound_ReturnsNotFound() throws Exception {
        when(doctorService.getDoctorByUserId(999L))
                .thenThrow(new ResourceNotFoundException("Doctor", "userId", 999L));

        mockMvc.perform(get("/api/doctors/user/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSearchDoctors_Success() throws Exception {
        List<DoctorDTO> doctors = Arrays.asList(doctorDTO);
        when(doctorService.searchDoctors("cardio")).thenReturn(doctors);

        mockMvc.perform(get("/api/doctors/search")
                        .param("keyword", "cardio"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].specialization").value("Cardiologie"));
    }

    @Test
    void testGetDoctorsBySpecialization_Success() throws Exception {
        List<DoctorDTO> doctors = Arrays.asList(doctorDTO);
        when(doctorService.getDoctorsBySpecialization("Cardiologie")).thenReturn(doctors);

        mockMvc.perform(get("/api/doctors/specialization")
                        .param("name", "Cardiologie"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].specialization").value("Cardiologie"));
    }
}