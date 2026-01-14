package com.wiqaytna.service;

import com.wiqaytna.dto.AppointmentDTO;
import com.wiqaytna.dto.CreateAppointmentRequest;
import com.wiqaytna.model.Appointment;
import com.wiqaytna.model.AppointmentStatus;
import com.wiqaytna.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for Appointment operations
 * Handles business logic for appointment booking and management
 */
@Service
@Transactional
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private SMSService smsService;

    @Autowired
    private PatientService patientService;

    /**
     * Create new appointment
     */
    public AppointmentDTO createAppointment(CreateAppointmentRequest request) {
        // Check if slot is available
        boolean slotTaken = appointmentRepository.existsByDoctorIdAndAppointmentDateAndAppointmentTime(
                request.getDoctorId(),
                request.getAppointmentDate(),
                request.getAppointmentTime()
        );

        if (slotTaken) {
            throw new RuntimeException("This time slot is already booked");
        }

        // Resolve patient ID - if the patientId is actually a userId, look it up
        Long actualPatientId = request.getPatientId();
        try {
            // Try to get patient by user ID first (in case frontend sends user_id)
            actualPatientId = patientService.getPatientByUserId(request.getPatientId()).getId();
        } catch (RuntimeException e) {
            // If that fails, assume it's already a patient ID
            // Verify patient exists
            patientService.getPatientById(request.getPatientId());
        }

        // Create appointment
        Appointment appointment = new Appointment();
        appointment.setDoctorId(request.getDoctorId());
        appointment.setPatientId(actualPatientId);
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        appointment.setNotes(request.getNotes());
        appointment.setReminderSent(false);

        Appointment saved = appointmentRepository.save(appointment);

        // Send confirmation SMS
        smsService.sendAppointmentConfirmation(saved);

        return mapToDTO(saved);
    }

    /**
     * Get appointment by ID
     */
    public AppointmentDTO getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
        return mapToDTO(appointment);
    }

    /**
     * Get all appointments for a doctor
     */
    public List<AppointmentDTO> getAppointmentsByDoctor(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get all appointments for a patient
     */
    public List<AppointmentDTO> getAppointmentsByPatient(Long patientId) {
        return appointmentRepository.findByPatientId(patientId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get upcoming appointments for a doctor
     */
    public List<AppointmentDTO> getUpcomingAppointmentsByDoctor(Long doctorId) {
        return appointmentRepository.findUpcomingAppointmentsByDoctor(doctorId, LocalDate.now()).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get upcoming appointments for a patient
     */
    public List<AppointmentDTO> getUpcomingAppointmentsByPatient(Long patientId) {
        return appointmentRepository.findUpcomingAppointmentsByPatient(patientId, LocalDate.now()).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get appointments for a doctor on a specific date
     */
    public List<AppointmentDTO> getAppointmentsByDoctorAndDate(Long doctorId, LocalDate date) {
        return appointmentRepository.findByDoctorIdAndAppointmentDate(doctorId, date).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Update appointment status
     */
    public AppointmentDTO updateAppointmentStatus(Long id, AppointmentStatus status) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        appointment.setStatus(status);
        Appointment updated = appointmentRepository.save(appointment);
        return mapToDTO(updated);
    }

    /**
     * Cancel appointment
     */
    public AppointmentDTO cancelAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (!appointment.isCancellable()) {
            throw new RuntimeException("This appointment cannot be cancelled");
        }

        appointment.cancel();
        Appointment updated = appointmentRepository.save(appointment);

        // Send cancellation SMS
        smsService.sendCancellationNotification(updated);

        return mapToDTO(updated);
    }

    /**
     * Scheduled task to send reminders (runs every hour)
     */
    @Scheduled(cron = "${scheduler.reminder.cron:0 0 * * * *}")
    public void sendUpcomingReminders() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<Appointment> appointmentsNeedingReminder =
                appointmentRepository.findAppointmentsNeedingReminder(tomorrow);

        for (Appointment appointment : appointmentsNeedingReminder) {
            try {
                smsService.sendReminder(appointment);
                appointment.markReminderSent();
                appointmentRepository.save(appointment);
            } catch (Exception e) {
                System.err.println("Failed to send reminder for appointment " + appointment.getId() + ": " + e.getMessage());
            }
        }

        if (!appointmentsNeedingReminder.isEmpty()) {
            System.out.println("Sent " + appointmentsNeedingReminder.size() + " appointment reminders");
        }
    }

    /**
     * Map Appointment entity to DTO
     */
    private AppointmentDTO mapToDTO(Appointment appointment) {
        AppointmentDTO dto = new AppointmentDTO();
        dto.setId(appointment.getId());
        dto.setDoctorId(appointment.getDoctorId());
        dto.setPatientId(appointment.getPatientId());
        dto.setAppointmentDate(appointment.getAppointmentDate());
        dto.setAppointmentTime(appointment.getAppointmentTime());
        dto.setStatus(appointment.getStatus());
        dto.setNotes(appointment.getNotes());
        dto.setReminderSent(appointment.getReminderSent());

        // Add doctor and patient details if loaded
        if (appointment.getDoctor() != null && appointment.getDoctor().getUser() != null) {
            dto.setDoctorName(appointment.getDoctor().getUser().getFullName());
            dto.setDoctorSpecialization(appointment.getDoctor().getSpecialization());
        }

        if (appointment.getPatient() != null && appointment.getPatient().getUser() != null) {
            dto.setPatientName(appointment.getPatient().getUser().getFullName());
            dto.setPatientPhone(appointment.getPatient().getUser().getPhone());
        }

        return dto;
    }
}
