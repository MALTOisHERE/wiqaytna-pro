package com.wiqaytna.controller;

import com.wiqaytna.dto.PatientDTO;
import com.wiqaytna.exception.ResourceNotFoundException;
import com.wiqaytna.security.JwtAuthenticationFilter;
import com.wiqaytna.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PatientController.class)
@AutoConfigureMockMvc(addFilters = false)
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private PatientDTO patientDTO;

    @BeforeEach
    void setUp() {
        patientDTO = new PatientDTO();
        patientDTO.setId(1L);
        patientDTO.setUserId(1L);
        patientDTO.setEmail("patient@example.com");
        patientDTO.setFirstName("John");
        patientDTO.setLastName("Doe");
        patientDTO.setPhone("+212612345678");
        patientDTO.setDateOfBirth(LocalDate.of(1990, 1, 1));
        patientDTO.setBloodGroup("O+");
        patientDTO.setAllergies("None");
        patientDTO.setMedicalHistory("No medical history");
        patientDTO.setAge(34);
    }

    @Test
    void testGetAllPatients_Success() throws Exception {
        List<PatientDTO> patients = Arrays.asList(patientDTO);
        when(patientService.getAllPatients()).thenReturn(patients);

        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("patient@example.com"))
                .andExpect(jsonPath("$[0].firstName").value("John"));
    }

    @Test
    void testGetPatientByUserId_Success() throws Exception {
        when(patientService.getPatientByUserId(1L)).thenReturn(patientDTO);

        mockMvc.perform(get("/api/patients/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.email").value("patient@example.com"));
    }

    @Test
    void testGetPatientByUserId_NotFound_ReturnsNotFound() throws Exception {
        when(patientService.getPatientByUserId(999L))
                .thenThrow(new ResourceNotFoundException("Patient", "userId", 999L));

        mockMvc.perform(get("/api/patients/user/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSearchPatients_Success() throws Exception {
        List<PatientDTO> patients = Arrays.asList(patientDTO);
        when(patientService.searchPatients("john")).thenReturn(patients);

        mockMvc.perform(get("/api/patients/search")
                        .param("name", "john"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"));
    }
}