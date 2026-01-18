package com.wiqaytna.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wiqaytna.dto.AppointmentDTO;
import com.wiqaytna.dto.CreateAppointmentRequest;
import com.wiqaytna.exception.ResourceNotFoundException;
import com.wiqaytna.model.AppointmentStatus;
import com.wiqaytna.security.JwtAuthenticationFilter;
import com.wiqaytna.service.AppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AppointmentController.class)
@AutoConfigureMockMvc(addFilters = false)
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AppointmentService appointmentService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private AppointmentDTO appointmentDTO;
    private CreateAppointmentRequest createRequest;

    @BeforeEach
    void setUp() {
        appointmentDTO = new AppointmentDTO();
        appointmentDTO.setId(1L);
        appointmentDTO.setDoctorId(1L);
        appointmentDTO.setPatientId(1L);
        appointmentDTO.setAppointmentDate(LocalDate.now().plusDays(1));
        appointmentDTO.setAppointmentTime(LocalTime.of(14, 0));
        appointmentDTO.setStatus(AppointmentStatus.SCHEDULED);
        appointmentDTO.setNotes("Regular checkup");

        createRequest = new CreateAppointmentRequest();
        createRequest.setDoctorId(1L);
        createRequest.setPatientId(1L);
        createRequest.setAppointmentDate(LocalDate.now().plusDays(1));
        createRequest.setAppointmentTime(LocalTime.of(14, 0));
    }

    @Test
    void testCreateAppointment_Success() throws Exception {
        when(appointmentService.createAppointment(any(CreateAppointmentRequest.class))).thenReturn(appointmentDTO);

        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.patientId").value(1L))
                .andExpect(jsonPath("$.status").value("SCHEDULED"));
    }

    @Test
    void testCreateAppointment_SlotAlreadyBooked_ReturnsBadRequest() throws Exception {
        when(appointmentService.createAppointment(any(CreateAppointmentRequest.class)))
                .thenThrow(new RuntimeException("Slot already booked"));

        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetAppointmentById_Success() throws Exception {
        when(appointmentService.getAppointmentById(1L)).thenReturn(appointmentDTO);

        mockMvc.perform(get("/api/appointments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("SCHEDULED"));
    }

    @Test
    void testGetAppointmentById_NotFound_ReturnsNotFound() throws Exception {
        when(appointmentService.getAppointmentById(999L))
                .thenThrow(new ResourceNotFoundException("Appointment", "id", 999L));

        mockMvc.perform(get("/api/appointments/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAppointmentsByDoctor_Success() throws Exception {
        List<AppointmentDTO> appointments = Arrays.asList(appointmentDTO);
        when(appointmentService.getAppointmentsByDoctor(1L)).thenReturn(appointments);

        mockMvc.perform(get("/api/appointments/doctor/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].doctorId").value(1));
    }

    @Test
    void testGetUpcomingAppointmentsByDoctor_Success() throws Exception {
        List<AppointmentDTO> appointments = Arrays.asList(appointmentDTO);
        when(appointmentService.getUpcomingAppointmentsByDoctor(1L)).thenReturn(appointments);

        mockMvc.perform(get("/api/appointments/doctor/1/upcoming"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("SCHEDULED"));
    }

    @Test
    void testGetAppointmentsByDoctorAndDate_Success() throws Exception {
        LocalDate date = LocalDate.now().plusDays(1);
        List<AppointmentDTO> appointments = Arrays.asList(appointmentDTO);
        when(appointmentService.getAppointmentsByDoctorAndDate(1L, date)).thenReturn(appointments);

        mockMvc.perform(get("/api/appointments/doctor/1/date")
                        .param("date", date.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].appointmentDate").value(date.toString()));
    }

    @Test
    void testGetAppointmentsByPatient_Success() throws Exception {
        List<AppointmentDTO> appointments = Arrays.asList(appointmentDTO);
        when(appointmentService.getAppointmentsByPatient(1L)).thenReturn(appointments);

        mockMvc.perform(get("/api/appointments/patient/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].patientId").value(1));
    }

    @Test
    void testUpdateAppointmentStatus_Success() throws Exception {
        appointmentDTO.setStatus(AppointmentStatus.CONFIRMED);
        when(appointmentService.updateAppointmentStatus(1L, AppointmentStatus.CONFIRMED))
                .thenReturn(appointmentDTO);

        mockMvc.perform(put("/api/appointments/1/status")
                        .param("status", "CONFIRMED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    void testCancelAppointment_Success() throws Exception {
        appointmentDTO.setStatus(AppointmentStatus.CANCELLED);
        when(appointmentService.cancelAppointment(eq(1L), any(String.class))).thenReturn(appointmentDTO);

        mockMvc.perform(delete("/api/appointments/1")
                        .param("cancelledBy", "PATIENT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }

    @Test
    void testCancelAppointment_NotFound_ReturnsBadRequest() throws Exception {
        when(appointmentService.cancelAppointment(eq(999L), any(String.class)))
                .thenThrow(new ResourceNotFoundException("Appointment", "id", 999L));

        mockMvc.perform(delete("/api/appointments/999")
                        .param("cancelledBy", "PATIENT"))
                .andExpect(status().isBadRequest());
    }
}