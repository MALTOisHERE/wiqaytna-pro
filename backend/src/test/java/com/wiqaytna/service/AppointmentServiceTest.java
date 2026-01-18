package com.wiqaytna.service;

import com.wiqaytna.dto.AppointmentDTO;
import com.wiqaytna.dto.CreateAppointmentRequest;
import com.wiqaytna.dto.PatientDTO;
import com.wiqaytna.exception.BusinessLogicException;
import com.wiqaytna.exception.ResourceNotFoundException;
import com.wiqaytna.model.Appointment;
import com.wiqaytna.model.AppointmentStatus;
import com.wiqaytna.repository.AppointmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private SMSService smsService;

    @Mock
    private PatientService patientService;

    @InjectMocks
    private AppointmentService appointmentService;

    private Appointment testAppointment;
    private CreateAppointmentRequest createRequest;

    @BeforeEach
    void setUp() {
        createRequest = new CreateAppointmentRequest();
        createRequest.setDoctorId(1L);
        createRequest.setPatientId(1L);
        createRequest.setAppointmentDate(LocalDate.now().plusDays(1));
        createRequest.setAppointmentTime(LocalTime.of(10, 0));
        createRequest.setNotes("Regular checkup");

        testAppointment = new Appointment();
        testAppointment.setId(1L);
        testAppointment.setDoctorId(1L);
        testAppointment.setPatientId(1L);
        testAppointment.setAppointmentDate(createRequest.getAppointmentDate());
        testAppointment.setAppointmentTime(createRequest.getAppointmentTime());
        testAppointment.setStatus(AppointmentStatus.SCHEDULED);
        testAppointment.setNotes(createRequest.getNotes());
        testAppointment.setReminderSent(false);
    }

    @Test
    void testCreateAppointment_Success() {
        // Given
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setId(1L);

        when(appointmentRepository.existsByDoctorIdAndAppointmentDateAndAppointmentTime(
                anyLong(), any(LocalDate.class), any(LocalTime.class))).thenReturn(false);
        // First try getPatientByUserId (will throw), then getPatientById
        when(patientService.getPatientByUserId(1L))
                .thenThrow(new com.wiqaytna.exception.ResourceNotFoundException("Patient", "userId", 1L));
        when(patientService.getPatientById(1L)).thenReturn(patientDTO);
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(testAppointment);
        doNothing().when(smsService).sendAppointmentConfirmation(any(Appointment.class));

        // When
        AppointmentDTO result = appointmentService.createAppointment(createRequest);

        // Then
        assertNotNull(result);
        assertEquals(testAppointment.getId(), result.getId());
        assertEquals(testAppointment.getStatus(), result.getStatus());
        verify(appointmentRepository).save(any(Appointment.class));
        verify(smsService).sendAppointmentConfirmation(any(Appointment.class));
    }

    @Test
    void testCreateAppointment_SlotAlreadyBooked_ThrowsException() {
        // Given
        when(appointmentRepository.existsByDoctorIdAndAppointmentDateAndAppointmentTime(
                anyLong(), any(LocalDate.class), any(LocalTime.class))).thenReturn(true);

        // When & Then
        assertThrows(BusinessLogicException.class, 
                () -> appointmentService.createAppointment(createRequest));
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    void testGetAppointmentById_Success() {
        // Given
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(testAppointment));

        // When
        AppointmentDTO result = appointmentService.getAppointmentById(1L);

        // Then
        assertNotNull(result);
        assertEquals(testAppointment.getId(), result.getId());
        verify(appointmentRepository).findById(1L);
    }

    @Test
    void testGetAppointmentById_NotFound_ThrowsException() {
        // Given
        when(appointmentRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, 
                () -> appointmentService.getAppointmentById(999L));
    }

    @Test
    void testGetAppointmentsByDoctor_Success() {
        // Given
        Appointment appointment2 = new Appointment();
        appointment2.setId(2L);
        appointment2.setDoctorId(1L);

        List<Appointment> appointments = Arrays.asList(testAppointment, appointment2);
        when(appointmentRepository.findByDoctorId(1L)).thenReturn(appointments);

        // When
        List<AppointmentDTO> result = appointmentService.getAppointmentsByDoctor(1L);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(appointmentRepository).findByDoctorId(1L);
    }

    @Test
    void testGetAppointmentsByPatient_Success() {
        // Given
        List<Appointment> appointments = Arrays.asList(testAppointment);
        when(appointmentRepository.findByPatientId(1L)).thenReturn(appointments);

        // When
        List<AppointmentDTO> result = appointmentService.getAppointmentsByPatient(1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(appointmentRepository).findByPatientId(1L);
    }

    @Test
    void testGetUpcomingAppointmentsByDoctor_Success() {
        // Given
        List<Appointment> appointments = Arrays.asList(testAppointment);
        when(appointmentRepository.findUpcomingAppointmentsByDoctor(1L, LocalDate.now()))
                .thenReturn(appointments);

        // When
        List<AppointmentDTO> result = appointmentService.getUpcomingAppointmentsByDoctor(1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(appointmentRepository).findUpcomingAppointmentsByDoctor(1L, LocalDate.now());
    }

    @Test
    void testGetAppointmentsByDoctorAndDate_Success() {
        // Given
        LocalDate date = LocalDate.now().plusDays(1);
        List<Appointment> appointments = Arrays.asList(testAppointment);
        when(appointmentRepository.findByDoctorIdAndAppointmentDate(1L, date))
                .thenReturn(appointments);

        // When
        List<AppointmentDTO> result = appointmentService.getAppointmentsByDoctorAndDate(1L, date);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(appointmentRepository).findByDoctorIdAndAppointmentDate(1L, date);
    }

    @Test
    void testUpdateAppointmentStatus_Success() {
        // Given
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(testAppointment));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(testAppointment);

        // When
        AppointmentDTO result = appointmentService.updateAppointmentStatus(1L, AppointmentStatus.CONFIRMED);

        // Then
        assertNotNull(result);
        assertEquals(AppointmentStatus.CONFIRMED, testAppointment.getStatus());
        verify(appointmentRepository).findById(1L);
        verify(appointmentRepository).save(testAppointment);
    }

    @Test
    void testUpdateAppointmentStatus_NotFound_ThrowsException() {
        // Given
        when(appointmentRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, 
                () -> appointmentService.updateAppointmentStatus(999L, AppointmentStatus.CONFIRMED));
    }

    @Test
    void testCancelAppointment_Success() {
        // Given
        testAppointment.setStatus(AppointmentStatus.SCHEDULED);
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(testAppointment));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(testAppointment);
        doNothing().when(smsService).sendCancellationNotification(any(Appointment.class));

        // When
        AppointmentDTO result = appointmentService.cancelAppointment(1L, "PATIENT");

        // Then
        assertNotNull(result);
        assertEquals(AppointmentStatus.CANCELLED, testAppointment.getStatus());
        verify(appointmentRepository).save(testAppointment);
        verify(smsService).sendCancellationNotification(testAppointment);
    }

    @Test
    void testCancelAppointment_NotFound_ThrowsException() {
        // Given
        when(appointmentRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class,
                () -> appointmentService.cancelAppointment(999L, "PATIENT"));
    }

    @Test
    void testCancelAppointment_NotCancellable_ThrowsException() {
        // Given
        testAppointment.setStatus(AppointmentStatus.COMPLETED);
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(testAppointment));

        // When & Then
        assertThrows(BusinessLogicException.class,
                () -> appointmentService.cancelAppointment(1L, "PATIENT"));
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }
}